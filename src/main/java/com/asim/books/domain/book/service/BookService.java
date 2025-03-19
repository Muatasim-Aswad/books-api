package com.asim.books.domain.book.service;

import com.asim.books.domain.book.model.dto.BookDto;
import com.asim.books.common.exception.IllegalAttemptToModify;

import java.util.List;

public interface BookService {
    BookDto addBook(BookDto book) throws IllegalAttemptToModify;

    BookDto getBook(Long id);

    BookDto updateBook(Long id, BookDto book);

    void deleteBook(Long id);

    List<BookDto> getBooks();
}
