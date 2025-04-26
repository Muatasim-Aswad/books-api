package com.asim.authentication.core.model.mapper;

import com.asim.authentication.core.model.dto.UserProfileDto;
import com.asim.authentication.core.model.mapper.base.BaseUserDtoMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between User entity and UserProfileDto.
 */
@Component
public class UserProfileMapper extends BaseUserDtoMapper<UserProfileDto> {

    public UserProfileMapper(ModelMapper modelMapper) {
        super(modelMapper, UserProfileDto.class);
    }
}