package com.asim.auth.infrastructure.security;

import com.asim.auth.infrastructure.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Generate a unique error ID for tracking and support purposes
        var errorId = UUID.randomUUID().toString();
        var path = request.getServletPath();
        var action = request.getMethod();
        var status = HttpServletResponse.SC_UNAUTHORIZED;
        var message = String.format(
                "Authentication failed. You need to be authenticated to %s in %s. Please provide a valid access token. If you need assistance, contact support with the error ID: %s",
                action, path, errorId);

        // Log the authentication failure with error ID for debugging
        log.error("Authentication failure: {}. Error ID: {}. Path: {}. Action: {}",
                authException.getMessage(), errorId, path, action, authException);

        // Set response content type to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Set HTTP status to 401 Unauthorized
        response.setStatus(status);

        // Write the JSON error response to the output stream
        objectMapper.writeValue(response.getOutputStream(), new ErrorResponse(status, message));
    }
}