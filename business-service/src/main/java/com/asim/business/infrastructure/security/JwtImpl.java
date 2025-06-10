package com.asim.business.infrastructure.security;

import com.asim.business.common.exception.UnauthorizedException;
import com.asim.business.infrastructure.config.CacheConfigs;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtImpl implements Jwt {

    private final CacheManager cacheManager;

    @Value("${jwt.access.secret}")
    private String secret;

    @Override
    public Map<String, Object> validateAndParseToken(String token) {
        try {
            Map<String, Object> claims = parseToken(token);

            // Check if the session has been invalidated
            String sessionId = claims.get("sessionId").toString();
            Cache cache = cacheManager.getCache(CacheConfigs.INVALID_SESSION);
            if (cache != null && cache.get(sessionId) != null) {
                throw new UnauthorizedException("Session has been invalidated");
            }

            return claims;
        } catch (SignatureException ex) {
            throw new UnauthorizedException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new UnauthorizedException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new UnauthorizedException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new UnauthorizedException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new UnauthorizedException("JWT claims string is empty");
        } catch (UnauthorizedException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected Error validating and parsing JWT token", ex);
            throw new UnauthorizedException(ex.getMessage());
        }
    }

    public Map<String, Object> parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Check if the token type matches
        if (!claims.get("type").equals("access")) {
            throw new UnauthorizedException("Invalid token type. Expected: \"access\", Found: " + claims.get("type"));
        }

        return new HashMap<>(claims);
    }
}
