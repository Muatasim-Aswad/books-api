package com.asim.business.domain.user.model.mapper;

import com.asim.business.common.model.mapper.BaseEntityDtoMapper;
import com.asim.business.domain.user.model.dto.UserCreateDto;
import com.asim.business.domain.user.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Maps User entity to UserCreateDto and vice versa.
 */
@Component
public class UserCreateMapper extends BaseEntityDtoMapper<User, UserCreateDto> {

    public UserCreateMapper(ModelMapper modelMapper) {
        super(modelMapper, User.class, UserCreateDto.class);
    }
}