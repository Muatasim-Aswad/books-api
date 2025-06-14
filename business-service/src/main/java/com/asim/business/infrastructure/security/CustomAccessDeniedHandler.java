package com.asim.business.infrastructure.security;

import com.asim.business.infrastructure.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Custom Access Denied Handler that handles forbidden access attempts.
 * This component is triggered when an authenticated user tries to access a resource
 * for which they don't have sufficient privileges.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    // Jackson ObjectMapper for JSON serialization
    private final ObjectMapper objectMapper;

    /**
     * Handles access denied failures by returning a standardized JSON error response.
     * This method is called by Spring Security when authorization fails.
     *
     * @param request               The request that resulted in an access denied failure
     * @param response              The response to send to the client
     * @param accessDeniedException The exception that caused the access denied failure
     * @throws IOException If writing the response fails
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        // Generate a unique error ID for tracking and support purposes
        var errorId = UUID.randomUUID().toString();
        var path = request.getServletPath();
        var action = request.getMethod();
        var status = HttpServletResponse.SC_FORBIDDEN;
        var message = String.format(
                "Access denied. You don't have permission to %s in %s. If you believe this is an error, please contact support with the error ID: %s",
                action, path, errorId);

        // Log the access denied failure with error ID for debugging
        log.error("Access denied failure: {}. Error ID: {}. Path: {}. Action: {}",
                accessDeniedException.getMessage(), errorId, path, action, accessDeniedException);

        // Set response content type to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Set HTTP status to 403 Forbidden
        response.setStatus(status);

        // Write the JSON error response to the output stream
        objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(status, message));
    }
}