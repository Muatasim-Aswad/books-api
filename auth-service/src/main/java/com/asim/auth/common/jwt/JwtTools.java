package com.asim.auth.common.jwt;

import com.asim.auth.common.exception.UnauthorizedException;

import java.util.Map;

public interface JwtTools {

    /**
     * Generates a JWT token with the given claims
     *
     * @param id        The user ID to include in the token
     * @param type      The type of token (e.g., "access" or "refresh")
     * @param sessionId The session ID to include in the token (required for access tokens)
     * @return The generated JWT token string
     */
    String generateToken(Long id, String type, String sessionId);

    /**
     * Validates if a token is valid and not expired
     *
     * @param token The token to validate
     * @param type  The type of token (e.g., "access" or "refresh")
     * @return claims if the token is valid
     * @throws UnauthorizedException if the token is invalid or expired
     */
    Map<String, Object> validateAndParseToken(String token, String type);

    /**
     * Extracts all claims from a token
     *
     * @param token The token to parse
     * @param type  The type of token (e.g., "access" or "refresh")
     * @return Map of all claims in the token
     */
    Map<String, Object> parseToken(String token, String type);

    String getSessionId(String token, String type);
}