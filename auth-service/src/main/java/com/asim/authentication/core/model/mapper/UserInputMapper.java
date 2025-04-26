package com.asim.authentication.core.model.mapper;

import com.asim.authentication.core.model.dto.UserInputDto;
import com.asim.authentication.core.model.mapper.base.BaseUserDtoMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between User entity and UserInputDto.
 */
@Component
public class UserInputMapper extends BaseUserDtoMapper<UserInputDto> {

    public UserInputMapper(ModelMapper modelMapper) {
        super(modelMapper, UserInputDto.class);
    }
}