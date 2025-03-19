package com.asim.books.domain.book.model.mapper;

import com.asim.books.domain.book.model.dto.BookDto;
import com.asim.books.domain.book.model.entity.Book;
import com.asim.books.common.mapper.entity.BaseEntityMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapper extends BaseEntityMapper<Book, BookDto> {

    public BookMapper(ModelMapper modelMapper) {
        super(modelMapper, Book.class, BookDto.class);
    }

}