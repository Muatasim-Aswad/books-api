package com.asim.books.domain.user.facade;

import com.asim.books.common.exception.DuplicateResourceException;
import com.asim.books.common.exception.ResourceNotFoundException;
import com.asim.books.domain.user.model.dto.UserCreateDto;
import com.asim.books.domain.user.model.dto.UserViewDto;
import com.asim.books.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade{
    private final UserService userService;

    @Override
    public UserViewDto createUser(UserCreateDto userCreateDto) throws DuplicateResourceException {
        return userService.createUser(userCreateDto);
    }

    @Override
    public UserViewDto getUser(Long userId) throws ResourceNotFoundException {
        return userService.getUser(userId);
    }
}
