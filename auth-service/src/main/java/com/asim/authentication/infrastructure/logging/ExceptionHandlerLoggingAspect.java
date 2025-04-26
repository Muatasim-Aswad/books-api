package com.asim.authentication.infrastructure.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Aspect for logging exceptions handled by controllers.
 * As handled exceptions the severity is lower, so warn level is used.
 */
@Aspect
@Component
@Slf4j
public class ExceptionHandlerLoggingAspect {

    @AfterReturning("within(@org.springframework.web.bind.annotation.RestControllerAdvice *) && " +
            "@annotation(org.springframework.web.bind.annotation.ExceptionHandler) && " +
            "execution(* *(..))")
    public void logException(JoinPoint joinPoint) {
        try {
            // Get exception info from method arguments
            Exception ex = null;
            for (Object arg : joinPoint.getArgs()) {
                if (arg instanceof Exception) {
                    ex = (Exception) arg;
                    break;
                }
            }

            if (ex == null) {
                log.warn("An exception was handled, but no exception object was found");
                return;
            }

            // Get exception info
            String name = ex.getClass().getSimpleName();
            String message = ex.getMessage();

            // Get info from current request
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                log.warn("Cannot access request attributes when logging exception: {}", name);
                return;
            }

            HttpServletRequest request = attributes.getRequest();
            String requestId = (String) request.getAttribute("requestId");
            String errorId = (String) request.getAttribute("errorId");

            requestId = requestId == null ? "unknown" : requestId;
            errorId = errorId == null ? "unknown" : errorId;

            // Log the exception: if !500 warn, else error
            if (errorId.equals("unknown")) {
                log.warn("[{}] -- handled exception: {}, message: {}",
                        requestId,
                        name,
                        message
                );
            } else {
                log.error("[{}] -- unexpected exception: {}, ID: [{}], message: {}, details:",
                        requestId,
                        name,
                        errorId,
                        message,
                        ex
                );
            }
        } catch (Exception e) {
            log.error("Error in logging exception", e);
        }
    }
}