package com.asim.books.infrastructure.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Interceptor to log incoming requests and outgoing responses.
 */
@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final List<String> SENSITIVE_HEADERS = Arrays.asList(
            "authorization", "cookie", "set-cookie"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Use existing request ID from header if present (for distributed tracing)
        String requestId = request.getHeader("X-Request-ID");
        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
        }

        request.setAttribute("requestId", requestId);
        request.setAttribute("startTime", System.currentTimeMillis());

        // Get authenticated user if available
        String username = getUserName();

        // Get client IP (handling proxies)
        String clientIp = getClientIp(request);

        // Log request details
        log.info("[{}] REQUEST: {} {} - User: {} - IP: {} - Content-Type: {}",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                username,
                clientIp,
                request.getContentType());

        // Log request parameters if present
        if (!request.getParameterMap().isEmpty()) {
            String params = request.getParameterMap().entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                    .collect(Collectors.joining(", "));
            log.debug("[{}] request parameters: {}", requestId, params);
        }

        // Log request headers (excluding sensitive ones)
        logHeaders(request, requestId);

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
            // Get authenticated user if available
            String username = getUserName();

            // Log response headers (excluding sensitive ones)
            logHeaders(response, requestId);

            log.info("[{}] RESPONSE: {} {} - User: {} - {} ms - Content-Type: {}",
                    requestId,
                    response.getStatus(),
                    request.getRequestURI(),
                    username,
                    duration,
                    response.getContentType());

        }
    }

    /**
     * Get client IP address from request headers.
     * Handles proxies that may add the client IP in different headers.
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * Log headers from the request.
     * Sensitive headers are redacted.
     */
    private void logHeaders(HttpServletRequest request, String requestId) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement().toLowerCase();
            if (!SENSITIVE_HEADERS.contains(headerName)) {
                headers.put(headerName, request.getHeader(headerName));
            } else {
                headers.put(headerName, "[REDACTED]");
            }
        }

        if (!headers.isEmpty()) {
            log.debug("[{}] -- request headers: {}", requestId, headers);
        }
    }

    /**
     * Log headers from the response.
     * Sensitive headers are redacted.
     */
    private void logHeaders(HttpServletResponse response, String requestId) {
        Collection<String> headerNames = response.getHeaderNames();
        Map<String, String> headers = new HashMap<>();

        for (String headerName : headerNames) {
            String headerNameLower = headerName.toLowerCase();
            if (!SENSITIVE_HEADERS.contains(headerNameLower)) {
                headers.put(headerName, response.getHeader(headerName));
            } else {
                headers.put(headerName, "[REDACTED]");
            }
        }

        if (!headers.isEmpty()) {
            log.debug("[{}] -- response headers: {}", requestId, headers);
        }
    }

    /**
     * Get authenticated username if available or "anonymous".
     */
    private String getUserName() {
        String username = "anonymous";
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication != null && authentication.isAuthenticated()) {
//                username = authentication.getName();
//            }
        return username;
    }
}