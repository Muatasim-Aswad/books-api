package com.asim.authentication.core.service.SessionService;

import com.asim.authentication.common.exception.ResourceNotFoundException;
import com.asim.authentication.common.jwt.JwtTools;
import com.asim.authentication.core.model.dto.TokenResponse;
import com.asim.authentication.core.model.dto.UserPublic;
import com.asim.authentication.core.model.mapper.UserPublicMapper;
import com.asim.authentication.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final UserRepository userRepository;
    private final UserPublicMapper userPublicMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTools jwtTools;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    @Override
    public TokenResponse login(String username, String password) {
        var user = userRepository.findByName(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return generateTokens(user.getId(), userPublicMapper.toDto(user));
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtTools.validateToken(refreshToken, jwtSecret)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        Map<String, Object> claims = jwtTools.parseToken(refreshToken, jwtSecret);
        Long userId = Long.valueOf(claims.get("userId").toString());

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        return generateTokens(userId, userPublicMapper.toDto(user));
    }

    @Override
    public void logout(String refreshToken) {
        // In a more complete implementation, you would invalidate the token
        // This could involve adding it to a blacklist or using a token registry
    }

    private TokenResponse generateTokens(Long userId, UserPublic userPublic) {
        Map<String, Object> accessClaims = new HashMap<>();
        accessClaims.put("userId", userId);
        accessClaims.put("type", "access");

        Map<String, Object> refreshClaims = new HashMap<>();
        refreshClaims.put("userId", userId);
        refreshClaims.put("type", "refresh");
        refreshClaims.put("tokenId", UUID.randomUUID().toString());

        String accessToken = jwtTools.generateToken(accessClaims, jwtSecret, accessTokenExpiration);
        String refreshToken = jwtTools.generateToken(refreshClaims, jwtSecret, refreshTokenExpiration);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration / 1000)
                .user(userPublic)
                .build();
    }
}