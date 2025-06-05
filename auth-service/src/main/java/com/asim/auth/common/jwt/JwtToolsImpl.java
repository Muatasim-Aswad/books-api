package com.asim.auth.common.jwt;

import com.asim.auth.common.exception.UnauthorizedException;
import com.asim.auth.infrastructure.config.CacheConfigs;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtToolsImpl implements JwtTools {

    private final CacheManager cacheManager;

    @Value("${jwt.access.secret}")
    private String accessJwtSecret;

    @Value(("${jwt.refresh.secret}"))
    private String refreshJwtSecret;

    @Value("${jwt.access.expiry}")
    private long accessJwtExpiration;

    @Value("${jwt.refresh.expiry}")
    private long refreshJwtExpiration;

    @Override
    public String generateToken(Long userId, String sessionId, String type) {
        if (type == null || userId == null || sessionId == null) {
            throw new IllegalArgumentException("User ID, session ID, and token type must not be null");
        }

        String secret = type.equals("access") ? accessJwtSecret : refreshJwtSecret;
        long expiryMs = type.equals("access") ? accessJwtExpiration : refreshJwtExpiration;

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("type", type);
        claims.put("jti", UUID.randomUUID().toString()); // JWT ID for uniqueness
        claims.put("sessionId", sessionId); // Session ID for tracking


        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiryMs);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public Map<String, Object> validateAndParseToken(String token, String type) {
        try {
            Map<String, Object> claims = parseToken(token, type);

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
            log.error("Error validating and parsing JWT token", ex);
            throw new UnauthorizedException(ex.getMessage());
        }
    }

    @Override
    public Map<String, Object> parseToken(String token, String type) {
        String secret = type.equals("access") ? accessJwtSecret : refreshJwtSecret;

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Check if the token type matches
        if (!claims.get("type").equals(type)) {
            throw new UnauthorizedException("Invalid token type. Expected: " + type + ", Found: " + claims.get("type"));
        }

        return new HashMap<>(claims);
    }
}