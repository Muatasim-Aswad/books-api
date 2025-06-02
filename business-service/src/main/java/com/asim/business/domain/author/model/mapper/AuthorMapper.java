package com.asim.business.domain.author.model.mapper;

import com.asim.business.common.model.mapper.BaseEntityDtoMapper;
import com.asim.business.domain.author.model.dto.AuthorDto;
import com.asim.business.domain.author.model.entity.Author;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Maps Author entity to AuthorDto and vice versa.
 */
@Component
public class AuthorMapper extends BaseEntityDtoMapper<Author, AuthorDto> {

    public AuthorMapper(ModelMapper modelMapper) {
        super(modelMapper, Author.class, AuthorDto.class);
    }

}