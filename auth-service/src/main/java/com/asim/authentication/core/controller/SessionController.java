package com.asim.authentication.core.controller;

import com.asim.authentication.core.model.dto.UserPublic;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth/sessions")
@RequiredArgsConstructor
public class SessionController {

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserPublic login() {
        return new UserPublic();
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public UserPublic refresh() {
        return new UserPublic();
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        // Logic to handle user logout
    }
}
