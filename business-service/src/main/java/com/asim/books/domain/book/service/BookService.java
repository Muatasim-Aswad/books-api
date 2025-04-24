package com.asim.books.domain.book.service;

import com.asim.books.common.exception.DuplicateResourceException;
import com.asim.books.common.exception.IllegalAttemptToModify;
import com.asim.books.common.exception.NoIdIsProvidedException;
import com.asim.books.common.exception.ResourceNotFoundException;
import com.asim.books.domain.book.model.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    /**
     * Adds book
     *
     * @param book BookDto
     * @return BookDto
     * @throws DuplicateResourceException if book already exists
     * @throws IllegalAttemptToModify     if attempted to modify an existing author details
     */
    BookDto addBook(BookDto book) throws DuplicateResourceException, IllegalAttemptToModify;

    /**
     * Gets book by ID
     *
     * @param id Book ID
     * @return BookDto
     * @throws ResourceNotFoundException if book not found
     */
    BookDto getBook(Long id) throws ResourceNotFoundException;

    /**
     * Updates book
     *
     * @param id   Book ID
     * @param book BookDto
     * @return BookDto
     * @throws IllegalAttemptToModify if attempted to modify an existing author details
     */
    BookDto updateBook(Long id, BookDto book) throws ResourceNotFoundException, IllegalAttemptToModify, NoIdIsProvidedException;

    /**
     * Deletes book
     *
     * @param id Book ID
     */
    void deleteBook(Long id) throws ResourceNotFoundException;

    /**
     * Gets all books with pagination, sorting and filtering
     *
     * @param pageable Pagination and sorting information
     * @param author   Optional name filter
     * @return Page of BookDto
     */
    Page<BookDto> getBooks(Pageable pageable, String title, String author);
}
