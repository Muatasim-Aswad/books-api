package com.asim.auth.core.service.SessionService;

import com.asim.auth.core.model.dto.TokenResponse;

public interface SessionService {
    TokenResponse login(String username, String password);

    TokenResponse refreshToken(String refreshToken);

    void logout();
}