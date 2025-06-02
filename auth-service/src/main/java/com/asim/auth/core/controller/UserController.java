package com.asim.auth.core.controller;

import com.asim.auth.core.model.dto.UserInput;
import com.asim.auth.core.model.dto.UserPublic;
import com.asim.auth.core.service.UserService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserPublic register(@RequestBody UserInput userInput) {

        return userService.registerUser(userInput);
    }

    @Override
    @PutMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public UserPublic changePassword(@RequestBody UserInput userInput) {
        return userService.changePassword(userInput);
    }

    @Override
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser() {
        userService.deleteUser();
    }
}