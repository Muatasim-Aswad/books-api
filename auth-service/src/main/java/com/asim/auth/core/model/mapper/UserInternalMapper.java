package com.asim.auth.core.model.mapper;

import com.asim.auth.core.model.dto.UserInternal;
import com.asim.auth.core.model.mapper.base.BaseUserDtoMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between User entity and UserInternal Dto.
 */
@Component
public class UserInternalMapper extends BaseUserDtoMapper<UserInternal> {

    public UserInternalMapper(ModelMapper modelMapper) {
        super(modelMapper, UserInternal.class);
    }
}
