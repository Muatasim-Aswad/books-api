package com.asim.authentication.common.jwt;

import java.util.Map;

public interface JwtTools {

    /**
     * Generates a JWT token with the given claims
     *
     * @param claims   The claims to include in the token
     * @param secret   The secret key used for signing
     * @param expiryMs The expiry time in milliseconds
     * @return The generated JWT token string
     */
    String generateToken(Long id, String type);

    /**
     * Validates if a token is valid and not expired
     *
     * @param token  The token to validate
     * @param secret The secret key used for validation
     * @return true if token is valid, false otherwise
     */
    boolean validateToken(String token, String type);

    /**
     * Extracts all claims from a token
     *
     * @param token  The token to parse
     * @param secret The secret key used for parsing
     * @return Map of all claims in the token
     */
    Map<String, Object> parseToken(String token, String type);
}