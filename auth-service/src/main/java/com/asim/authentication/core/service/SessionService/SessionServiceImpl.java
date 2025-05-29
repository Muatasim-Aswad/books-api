package com.asim.authentication.core.service.SessionService;

import com.asim.authentication.common.exception.ResourceNotFoundException;
import com.asim.authentication.common.exception.UnauthorizedException;
import com.asim.authentication.common.jwt.JwtTools;
import com.asim.authentication.core.model.dto.TokenResponse;
import com.asim.authentication.core.model.mapper.UserInternalMapper;
import com.asim.authentication.core.model.mapper.UserPublicMapper;
import com.asim.authentication.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final UserRepository userRepository;
    private final UserPublicMapper userPublicMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTools jwtTools;
    private final UserInternalMapper userInternalMapper;
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
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        return generateTokensById(userId, refreshToken);
    }

    @Override
    public void logout(String refreshToken) {

            // Get the sessionId from the refresh token
            String sessionId = jwtTools.getSessionId(refreshToken, "refresh");

            // Add to invalidated sessions cache
            Cache cache = cacheManager.getCache("invalidSessions");
            if (cache != null) {
                cache.put(sessionId, true);
            }
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
}