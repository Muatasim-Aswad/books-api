package com.asim.business.domain.user.controller;

import com.asim.business.domain.user.model.dto.UserRoleUpdateDto;
import com.asim.business.domain.user.model.dto.UserViewDto;
import com.asim.business.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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