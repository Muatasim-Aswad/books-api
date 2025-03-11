package com.asim.books.author.model.mapper;

import com.asim.books.author.model.dto.AuthorDto;
import com.asim.books.author.model.entity.Author;
import com.asim.books.common.mapper.entity.BaseEntityMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper extends BaseEntityMapper<Author, AuthorDto> {

    public AuthorMapper(ModelMapper modelMapper) {
        super(modelMapper, Author.class, AuthorDto.class);
    }

}