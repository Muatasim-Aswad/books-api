package com.asim.books.domain.author.model.mapper;

import com.asim.books.common.mapper.entity.BaseEntityMapper;
import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.model.entity.Author;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Maps Author entity to AuthorDto and vice versa.
 */
@Component
public class AuthorMapper extends BaseEntityMapper<Author, AuthorDto> {

    public AuthorMapper(ModelMapper modelMapper) {
        super(modelMapper, Author.class, AuthorDto.class);
    }

}