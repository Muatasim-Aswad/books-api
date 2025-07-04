package com.asim.business.domain.author.service;

import com.asim.business.common.exception.DuplicateResourceException;
import com.asim.business.common.exception.NoIdIsProvidedException;
import com.asim.business.common.exception.OptimisticLockException;
import com.asim.business.common.exception.ResourceNotFoundException;
import com.asim.business.domain.author.model.dto.AuthorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Gets all authors with pagination, sorting and filtering
     *
     * @param pageable Pagination and sorting information
     * @param name     Optional name filter
     * @return Page of AuthorDto
     */
    Page<AuthorDto> getAuthors(Pageable pageable, String name);

    /**
     * Finds author and matches.
     * The matching is done by comparing only non-null fields.
     *
     * @param authorDto AuthorDto
     * @return author if found and matches, null if found but does not match
     * @throws NoIdIsProvidedException   if author ID is not provided
     * @throws ResourceNotFoundException if author not found
     * @throws OptimisticLockException   if version mismatch
     */
    AuthorDto findMatchingAuthor(AuthorDto authorDto) throws NoIdIsProvidedException, ResourceNotFoundException, OptimisticLockException;
}
