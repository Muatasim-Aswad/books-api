package com.asim.business.domain.author.facade;

import com.asim.business.domain.author.model.dto.AuthorDto;
import com.asim.business.domain.author.model.dto.AuthorDtoValidator;
import com.asim.business.domain.author.service.AuthorService;

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