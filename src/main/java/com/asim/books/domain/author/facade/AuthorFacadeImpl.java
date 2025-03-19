package com.asim.books.domain.author.facade;

import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.service.AuthorService;
import org.springframework.stereotype.Component;

@Component
public class AuthorFacadeImpl implements AuthorFacade {
    final private AuthorService authorService;

    public AuthorFacadeImpl(AuthorService authorService) {
        this.authorService = authorService;
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
}
