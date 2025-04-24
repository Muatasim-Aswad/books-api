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
     * @see AuthorService#findMatchingAuthor(AuthorDto)
     */
    AuthorDto findMatchingAuthor(AuthorDto authorDto);

    /**
     * @see AuthorDtoValidator#validateOnCreate(AuthorDto)
     */
    void validateAuthorToCreate(AuthorDto authorDto);
}