package com.asim.authentication.core.service.SessionService;

import com.asim.authentication.core.model.dto.TokenResponse;

public interface SessionService {
    TokenResponse login(String username, String password);

    TokenResponse refreshToken(String refreshToken);

    void logout();
}