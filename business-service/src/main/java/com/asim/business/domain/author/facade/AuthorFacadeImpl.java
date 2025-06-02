package com.asim.business.domain.author.facade;

import com.asim.business.domain.author.model.dto.AuthorDto;
import com.asim.business.domain.author.model.dto.AuthorDtoValidator;
import com.asim.business.domain.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorFacadeImpl implements AuthorFacade {
    final private AuthorService authorService;
    final private AuthorDtoValidator authorDtoValidator;

    @Override
    public AuthorDto findMatchingAuthor(AuthorDto authorDto) {
        return authorService.findMatchingAuthor(authorDto);
    }

    @Override
    public void validateAuthorToCreate(AuthorDto authorDto) {
        authorDtoValidator.validateOnCreate(authorDto);
    }
}
