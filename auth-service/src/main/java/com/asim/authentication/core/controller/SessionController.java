package com.asim.authentication.core.controller;

import com.asim.authentication.core.model.dto.RefreshTokenRequest;
import com.asim.authentication.core.model.dto.TokenResponse;
import com.asim.authentication.core.model.dto.UserInput;
import com.asim.authentication.core.service.SessionService.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth/sessions")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse login(@RequestBody UserInput user) {
        return sessionService.login(user.getName(), user.getPassword());
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse refresh(@RequestBody RefreshTokenRequest request) {
        return sessionService.refreshToken(request.getRefreshToken());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody RefreshTokenRequest request) {
        sessionService.logout(request.getRefreshToken());
    }
}