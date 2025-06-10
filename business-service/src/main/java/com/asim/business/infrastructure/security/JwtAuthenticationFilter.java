package com.asim.business.infrastructure.security;

import com.asim.business.common.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * JWT Authentication Filter that intercepts HTTP requests to validate JWT tokens.
 * This filter extends OncePerRequestFilter to ensure it's executed only once per request.
 *
 * The filter:
 * 1. Extracts JWT token from the Authorization header
 * 2. Validates the token using the JWT service
 * 3. Sets up Spring Security authentication context if token is valid
 * 4. Handles authentication exceptions appropriately
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // JWT service for token validation and parsing
    private final Jwt jwt;

    /**
     * Main filter method that processes each HTTP request.
     *
     * @param request     The HTTP request being processed
     * @param response    The HTTP response to be sent
     * @param filterChain The chain of filters to continue processing
     * @throws ServletException If servlet processing fails
     * @throws IOException      If I/O operations fail
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Extract JWT token from the Authorization header
            String jwtToken = extractJwtFromRequest(request);

            // Process the token if it exists
            if (jwtToken != null && !jwtToken.isEmpty()) {
                // Validate and parse the JWT token to extract claims
                Map<String, Object> claims = jwt.validateAndParseToken(jwtToken);

                // Extract user information from token claims
                Long userId = Long.valueOf(claims.get("userId").toString());
                String sessionId = claims.get("sessionId").toString();

                // Create Spring Security authentication token with user details
                // This represents an authenticated user in the security context
                JwtAuthenticationToken authentication =
                        new JwtAuthenticationToken(userId, null, null, sessionId);

                // Add request details (IP address, session ID, etc.) to the authentication
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in Spring Security context
                // This makes the user available throughout the request lifecycle
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (UnauthorizedException e) {
            // Re-throw UnauthorizedException to be handled by authentication entry point
            throw e;
        } catch (Exception e) {
            throw e;
            // Wrap any other exceptions as UnauthorizedException
            //throw new UnauthorizedException("Failed to set user authentication in security context: " + e.getMessage());
        }

        // Continue with the next filter in the chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts JWT token from the Authorization header.
     * Expected format: "Bearer <token>"
     *
     * @param request The HTTP request containing the Authorization header
     * @return The JWT token string, or null if not found or invalid format
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        // Get the Authorization header value
        String bearerToken = request.getHeader("Authorization");

        // Check if header exists and starts with "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Extract the token part (everything after "Bearer ")
            return bearerToken.substring(7);
        }

        // Return null if no valid token found
        return null;
    }
}