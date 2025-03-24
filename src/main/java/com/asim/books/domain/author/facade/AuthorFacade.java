package com.asim.books.domain.author.facade;

import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.model.dto.AuthorDtoValidator;
import com.asim.books.domain.author.service.AuthorService;

/**
 * Facade for author operations.
 * Delegates to {@link AuthorService}
 * As a facade, it should not contain any business logic or adaptions.
 */
public interface AuthorFacade {
    /**
     * @see AuthorService#getAuthor(Long)
     */
    AuthorDto findAuthorById(Long id);

    /**
     * @see AuthorService#authorExists(Long)
     */
    boolean authorExists(Long id);

    /**
     * @see AuthorService#findAuthorAndMatch(AuthorDto)
     */
    AuthorDto findAuthorAndMatch(AuthorDto authorDto);

    /**
     * @see AuthorDtoValidator#validate(AuthorDto)
     */
    void validateAuthor(AuthorDto authorDto);

    /**
     * @see AuthorDtoValidator#validateRequired(AuthorDto)
     */
    void validateAuthorRequired(AuthorDto authorDto);
}