package com.asim.authentication.core.model.mapper.base;

import com.asim.authentication.core.model.entity.User;
import org.modelmapper.ModelMapper;

/**
 * A base mapper for User entity and its various DTOs.
 * Extends the generic base mapper with User-specific functionality.
 *
 * @param <Dto> the specific User DTO type
 */
public abstract class BaseUserDtoMapper<Dto> extends BaseEntityDtoMapper<User, Dto> {

    protected BaseUserDtoMapper(ModelMapper modelMapper, Class<Dto> dtoClass) {
        super(modelMapper, User.class, dtoClass);
    }
}