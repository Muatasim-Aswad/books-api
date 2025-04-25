package com.asim.books.domain.user.model.mapper;

import com.asim.books.common.model.mapper.BaseEntityDtoMapper;
import com.asim.books.domain.user.model.dto.UserNameUpdateDto;
import com.asim.books.domain.user.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Maps User entity to UserNameUpdateDto and vice versa.
 */
@Component
public class UserNameUpdateMapper extends BaseEntityDtoMapper<User, UserNameUpdateDto> {

    public UserNameUpdateMapper(ModelMapper modelMapper) {
        super(modelMapper, User.class, UserNameUpdateDto.class);
    }
}