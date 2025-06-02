package com.asim.books.domain.user.facade;

import com.asim.books.common.exception.DuplicateResourceException;
import com.asim.books.common.exception.ResourceNotFoundException;
import com.asim.books.domain.user.model.dto.UserCreateDto;
import com.asim.books.domain.user.model.dto.UserViewDto;

public interface UserFacade {

    UserViewDto createUser(UserCreateDto userCreateDto) throws DuplicateResourceException;

    UserViewDto getUser(Long userId) throws ResourceNotFoundException;
}
