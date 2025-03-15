package com.asim.books.book.service;

import com.asim.books.book.model.dto.BookDto;
import com.asim.books.common.exception.custom.IllegalAttemptToModify;

import java.util.List;

public interface BookService {
    BookDto addBook(BookDto book) throws IllegalAttemptToModify;

    BookDto getBook(Long id);

    BookDto updateBook(Long id, BookDto book);

    void deleteBook(Long id);

    List<BookDto> getBooks();
}
