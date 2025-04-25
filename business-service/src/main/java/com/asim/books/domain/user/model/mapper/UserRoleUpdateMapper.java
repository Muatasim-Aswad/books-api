package com.asim.books.domain.user.model.mapper;

import com.asim.books.common.model.mapper.BaseEntityDtoMapper;
import com.asim.books.domain.user.model.dto.UserRoleUpdateDto;
import com.asim.books.domain.user.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Maps User entity to UserRoleUpdateDto and vice versa.
 */
@Component
public class UserRoleUpdateMapper extends BaseEntityDtoMapper<User, UserRoleUpdateDto> {

    public UserRoleUpdateMapper(ModelMapper modelMapper) {
        super(modelMapper, User.class, UserRoleUpdateDto.class);
    }
}