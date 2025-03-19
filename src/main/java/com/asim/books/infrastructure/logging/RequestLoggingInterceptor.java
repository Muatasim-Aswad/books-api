package com.asim.books.infrastructure.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = UUID.randomUUID().toString();
        request.setAttribute("requestId", requestId);
        request.setAttribute("startTime", System.currentTimeMillis());

        // Log request details
        log.info("[{}] Request: {} {} - Content-Type: {}",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                request.getContentType());

        // Log request parameters if present
        if (!request.getParameterMap().isEmpty()) {
            String params = request.getParameterMap().entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                    .collect(Collectors.joining(", "));
            log.debug("[{}] Request parameters: {}", requestId, params);
        }

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestId = (String) request.getAttribute("requestId");
        long startTime = (Long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;

        if (ex != null) {
            log.error("[{}] Exception: {} - {} ms", requestId, ex.getMessage(), duration);
        } else {
            log.info("[{}] Response: {} {} - {} ms - Content-Type: {}",
                    requestId,
                    response.getStatus(),
                    request.getRequestURI(),
                    duration,
                    response.getContentType());
        }
    }
}