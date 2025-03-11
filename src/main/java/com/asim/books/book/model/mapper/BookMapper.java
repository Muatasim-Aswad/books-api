package com.asim.books.book.model.mapper;

import com.asim.books.book.model.dto.BookDto;
import com.asim.books.book.model.entity.Book;
import com.asim.books.common.mapper.entity.BaseEntityMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapper extends BaseEntityMapper<Book, BookDto> {

    public BookMapper(ModelMapper modelMapper) {
        super(modelMapper, Book.class, BookDto.class);
    }

}