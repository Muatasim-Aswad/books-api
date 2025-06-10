package com.asim.business.infrastructure.security;

import com.asim.business.common.exception.UnauthorizedException;

import java.util.Map;

public interface Jwt {
    /**
     * Validates if a token is valid and not expired
     *
     * @param token The token to validate
     * @return claims if the token is valid
     * @throws UnauthorizedException if the token is invalid or expired
     */
    Map<String, Object> validateAndParseToken(String token);
}
