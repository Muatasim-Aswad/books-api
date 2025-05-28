package com.asim.authentication.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class JwtToolsImpl implements JwtTools {

    @Value("${jwt.access.secret}")
    private String accessJwtSecret;

    @Value(("${jwt.refresh.secret}"))
    private String refreshJwtSecret;

    @Value("${jwt.access.expiry}")
    private long accessJwtExpiration;

    @Value("${jwt.refresh.expiry}")
    private long refreshJwtExpiration;

    @Override
    public String generateToken(Long id, String type) {
        String secret = type.equals("access") ? accessJwtSecret : refreshJwtSecret;
        long expiryMs = type.equals("access") ? accessJwtExpiration : refreshJwtExpiration;

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", id);
        claims.put("type", type);
        claims.put("jti", UUID.randomUUID().toString()); // JWT ID for uniqueness

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiryMs);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    @Override
    public boolean validateToken(String token, String type) {
        try {
            String secret = type.equals("access") ? accessJwtSecret : refreshJwtSecret;

            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            var claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Check if the token type matches
            if (!claims.get("type").equals(type)) {
                log.error("Invalid token type");
                return false;
            }

            // add later to check if the token is blacklisted or not
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
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

        return new HashMap<>(claims);
    }
}