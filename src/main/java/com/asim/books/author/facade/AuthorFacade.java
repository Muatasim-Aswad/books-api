package com.asim.books.author.facade;

import com.asim.books.author.model.dto.AuthorDto;

/**
 * Facade for author operations.
 * Delegates to {@link com.asim.books.author.service.AuthorService}
 * As a facade, it should not contain any business logic or adaptions.
 */
public interface AuthorFacade {
    /**
     * @see com.asim.books.author.service.AuthorService#getAuthor(Long)
     */
    AuthorDto findAuthorById(Long id);

    /**
     * @see com.asim.books.author.service.AuthorService#authorExists(Long)
     */
    boolean authorExists(Long id);

    /**
     * @see com.asim.books.author.service.AuthorService#findAuthorAndMatch(AuthorDto)
     */
    boolean findAuthorAndMatch(AuthorDto authorDto);
}