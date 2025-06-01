package com.asim.authentication.core.controller;

import com.asim.authentication.core.model.dto.RefreshToken;
import com.asim.authentication.core.model.dto.TokenResponse;
import com.asim.authentication.core.model.dto.UserInput;
import com.asim.authentication.core.service.SessionService.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth/sessions")
@RequiredArgsConstructor
public class SessionController implements SessionApi{
    private final SessionService sessionService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse login(@RequestBody UserInput user) {
        return sessionService.login(user.getName(), user.getPassword());
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse refresh(@RequestBody RefreshToken refreshToken) {
        return sessionService.refreshToken(refreshToken.getRefreshToken());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        sessionService.logout();
    }
}