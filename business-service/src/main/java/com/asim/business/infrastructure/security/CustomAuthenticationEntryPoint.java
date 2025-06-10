package com.asim.business.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Custom Authentication Entry Point that handles unauthorized access attempts.
 * This component is triggered when a user tries to access a protected resource
 * without proper authentication or with invalid/expired credentials.
 *
 * Instead of redirecting to a login page (typical for web applications),
 * this entry point returns a JSON error response suitable for REST APIs.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Jackson ObjectMapper for JSON serialization
    private final ObjectMapper objectMapper;

    /**
     * Handles authentication failures by returning a standardized JSON error response.
     * This method is called by Spring Security when authentication fails.
     *
     * @param request       The request that resulted in an authentication failure
     * @param response      The response to send to the client
     * @param authException The exception that caused the authentication failure
     * @throws IOException If writing the response fails
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Generate a unique error ID for tracking and support purposes
        String errorId = UUID.randomUUID().toString();

        // Log the authentication failure with error ID for debugging
        log.error("Auth error",authException);
        log.error("Responding with unauthorized error. Message - {}, ErrorId - {}",
                authException.getMessage(), errorId, authException);

        // Set response content type to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Set HTTP status to 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create a structured error response body
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "Authentication failed. Please provide a valid access token.");
        body.put("path", request.getServletPath());
        body.put("errorId", errorId); // Include error ID for support tracking

        // Write the JSON error response to the output stream
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}