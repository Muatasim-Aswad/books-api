package com.asim.auth.core.model.mapper;

import com.asim.auth.core.model.dto.UserPublic;
import com.asim.auth.core.model.mapper.base.BaseUserDtoMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between User entity and UserProfileDto.
 */
@Component
public class UserPublicMapper extends BaseUserDtoMapper<UserPublic> {

    public UserPublicMapper(ModelMapper modelMapper) {
        super(modelMapper, UserPublic.class);
    }
}