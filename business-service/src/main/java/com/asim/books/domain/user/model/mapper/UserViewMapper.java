package com.asim.books.domain.user.model.mapper;

import com.asim.books.common.model.mapper.BaseEntityDtoMapper;
import com.asim.books.domain.user.model.dto.UserViewDto;
import com.asim.books.domain.user.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Maps User entity to UserViewDto and vice versa.
 */
@Component
public class UserViewMapper extends BaseEntityDtoMapper<User, UserViewDto> {

    public UserViewMapper(ModelMapper modelMapper) {
        super(modelMapper, User.class, UserViewDto.class);
    }
}