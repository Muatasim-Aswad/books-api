package com.asim.business.domain.user.facade;

import com.asim.business.common.exception.DuplicateResourceException;
import com.asim.business.common.exception.ResourceNotFoundException;
import com.asim.business.domain.user.model.dto.UserCreateDto;
import com.asim.business.domain.user.model.dto.UserViewDto;

public interface UserFacade {

    UserViewDto createUser(UserCreateDto userCreateDto) throws DuplicateResourceException;

    UserViewDto getUser(Long userId) throws ResourceNotFoundException;
}
