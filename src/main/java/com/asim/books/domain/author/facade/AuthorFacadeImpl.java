package com.asim.books.domain.author.facade;

import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.model.dto.AuthorDtoValidator;
import com.asim.books.domain.author.service.AuthorService;
import org.springframework.stereotype.Component;

@Component
public class AuthorFacadeImpl implements AuthorFacade {
    final private AuthorService authorService;
    final private AuthorDtoValidator authorDtoValidator;

    public AuthorFacadeImpl(AuthorService authorService, AuthorDtoValidator authorDtoValidator) {
        this.authorService = authorService;
        this.authorDtoValidator = authorDtoValidator;
    }

    @Override
    public AuthorDto findAuthorById(Long id) {
        return authorService.getAuthor(id);
    }

    @Override
    public boolean authorExists(Long id) {
        return authorService.authorExists(id);
    }

    @Override
    public boolean findAuthorAndMatch(AuthorDto authorDto) {
        return authorService.findAuthorAndMatch(authorDto);
    }

    @Override
    public void validateAuthor(AuthorDto authorDto) {
        authorDtoValidator.validate(authorDto);
    }

    @Override
    public void validateAuthorRequired(AuthorDto authorDto) {
        authorDtoValidator.validateRequired(authorDto);
    }

}
