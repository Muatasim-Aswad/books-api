package com.asim.business.domain.book.model.mapper;

import com.asim.business.common.model.mapper.BaseEntityDtoMapper;
import com.asim.business.domain.book.model.dto.BookDto;
import com.asim.business.domain.book.model.entity.Book;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Maps Book entity to BookDto and vice versa.
 */
@Component
public class BookMapper extends BaseEntityDtoMapper<Book, BookDto> {

    public BookMapper(ModelMapper modelMapper) {
        super(modelMapper, Book.class, BookDto.class);
    }
}