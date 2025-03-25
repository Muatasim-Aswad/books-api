package com.asim.books.infrastructure.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

    @Before("@annotation(org.springframework.web.bind.annotation.ExceptionHandler) && args(ex)")
    public void logException(Exception ex) {
        String message = ex.getMessage();
        String name = ex.getClass().getSimpleName();
        String requestId = null;

        try {
            // Get request ID from current request
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            requestId = (String) request.getAttribute("requestId");

        } catch (Exception _) {
        }

        log.warn("[{}] -- handled exception: {}, message: {}",
                requestId != null ? requestId : "unknown",
                name,
                message
        );
    }
}
