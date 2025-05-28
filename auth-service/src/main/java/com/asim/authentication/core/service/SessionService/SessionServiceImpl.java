package com.asim.authentication.core.service.SessionService;

import com.asim.authentication.common.exception.ResourceNotFoundException;
import com.asim.authentication.common.jwt.JwtTools;
import com.asim.authentication.core.model.dto.TokenResponse;
import com.asim.authentication.core.model.mapper.UserInternalMapper;
import com.asim.authentication.core.model.mapper.UserPublicMapper;
import com.asim.authentication.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
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

    @Value("${jwt.access.expiry}")
    private long accessJwtExpiration;

    @Override
    public TokenResponse login(String username, String password) {
        var user = userRepository.findByName(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return generateTokensById(user.getId());
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtTools.validateToken(refreshToken, "refresh")) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        Map<String, Object> claims = jwtTools.parseToken(refreshToken, "refresh");
        Long userId = Long.valueOf(claims.get("userId").toString());

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        return generateTokensById(userId);
    }

    @Override
    public void logout(String refreshToken) {
        // In a more complete implementation, you would invalidate the token
        // This could involve adding it to a blacklist or using a token registry
    }


    private TokenResponse generateTokensById(Long userId) {

        String accessToken = jwtTools.generateToken(userId, "access");
        String refreshToken = jwtTools.generateToken(userId, "refresh");

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessJwtExpiration / 1000)
                .build();
    }
}