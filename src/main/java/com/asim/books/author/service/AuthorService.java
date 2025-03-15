package com.asim.books.author.service;

import com.asim.books.author.model.dto.AuthorDto;
import com.asim.books.common.exception.custom.DuplicateResourceException;
import com.asim.books.common.exception.custom.NoIdIsProvidedException;
import com.asim.books.common.exception.custom.ResourceNotFoundException;

import java.util.List;

public interface AuthorService {
    /**
     * Adds author
     *
     * @param author AuthorDto
     * @return AuthorDto
     * @throws DuplicateResourceException if author already exists
     */
    AuthorDto addAuthor(AuthorDto author) throws DuplicateResourceException;

    /**
     * Gets author by ID
     *
     * @param id Author ID
     * @return AuthorDto
     * @throws ResourceNotFoundException if author not found
     */
    AuthorDto getAuthor(Long id) throws ResourceNotFoundException;

    /**
     * Updates author
     *
     * @param id     Author ID
     * @param author AuthorDto
     * @return AuthorDto
     */
    AuthorDto updateAuthor(Long id, AuthorDto author) throws ResourceNotFoundException;

    /**
     * Deletes author
     *
     * @param id Author ID
     */
    void deleteAuthor(Long id) throws ResourceNotFoundException;

    /**
     * Gets all authors
     *
     * @return List of AuthorDto
     */
    List<AuthorDto> getAuthors();

    /**
     * Checks if author exists
     *
     * @param id Author ID
     * @return true if exists, false otherwise
     */
    boolean authorExists(Long id);

    /**
     * Finds author and matches.
     * The matching is done by comparing only non-null fields.
     *
     * @param authorDto AuthorDto
     * @return true if found and matches, false otherwise
     * @throws ResourceNotFoundException if author not found
     * @throws NoIdIsProvidedException   if author ID is not provided
     */
    boolean findAuthorAndMatch(AuthorDto authorDto) throws NoIdIsProvidedException, ResourceNotFoundException;
}
