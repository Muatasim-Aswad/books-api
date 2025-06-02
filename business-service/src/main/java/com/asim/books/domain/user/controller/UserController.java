package com.asim.books.domain.user.controller;

import com.asim.books.domain.user.model.dto.UserRoleUpdateDto;
import com.asim.books.domain.user.model.dto.UserViewDto;
import com.asim.books.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @PatchMapping("/role")
    public UserViewDto updateUserRole(@RequestBody UserRoleUpdateDto userRoleUpdateDto) {
        return userService.updateUserRole(userRoleUpdateDto);
    }
}