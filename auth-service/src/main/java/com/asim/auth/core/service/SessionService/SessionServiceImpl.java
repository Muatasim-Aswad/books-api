package com.asim.auth.core.service.SessionService;

import com.asim.auth.common.exception.ResourceNotFoundException;
import com.asim.auth.common.exception.UnauthorizedException;
import com.asim.auth.common.jwt.JwtTools;
import com.asim.auth.core.model.dto.TokenResponse;
import com.asim.auth.core.model.mapper.UserInternalMapper;
import com.asim.auth.core.model.mapper.UserPublicMapper;
import com.asim.auth.core.repository.UserRepository;
import com.asim.auth.infrastructure.config.CacheConfigs;
import com.asim.auth.infrastructure.grpc.GrpcClientService;
import com.asim.auth.infrastructure.security.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final UserRepository userRepository;
    private final UserPublicMapper userPublicMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTools jwtTools;
    private final UserInternalMapper userInternalMapper;
    private final GrpcClientService grpcClientService;
    private final CacheManager cacheManager;

    @Value("${jwt.access.expiry}")
    private long accessJwtExpiration;

    @Override
    public TokenResponse login(String username, String password) {
        var user = userRepository.findByName(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Wrong password");
        }

        return generateTokensById(user.getId(), null);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        Map<String, Object> claims = jwtTools.validateAndParseToken(refreshToken, "refresh");

        Long userId = Long.valueOf(claims.get("userId").toString());

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        return generateTokensById(userId, refreshToken);
    }

    @Override
    public void logout() {
        var sessionId = getSessionId();

        // Add to invalidated sessions cache
        Cache cache = cacheManager.getCache(CacheConfigs.INVALID_SESSION);
        if (cache == null) {
            throw new IllegalStateException("Cache for invalid sessions is not configured");
        }
        cache.put(sessionId, true);

        // send the sessionId to business service via gRPC
        var isDone = grpcClientService.blockSession(sessionId);

        log.info("Session invalidation is done: {}", isDone);
    }

    private TokenResponse generateTokensById(Long userId, String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            refreshToken = jwtTools.generateToken(userId, "refresh", null);
        }

        String sessionId = jwtTools.getSessionId(refreshToken, "refresh");

        String accessToken = jwtTools.generateToken(userId, "access", sessionId);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresInSeconds(accessJwtExpiration / 1000)
                .build();
    }

    private String getSessionId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getSessionId();
        }

        throw new UnauthorizedException("User not authenticated or session ID not available");
    }

}