package com.asim.business.domain.book.gateway;

import com.asim.business.domain.author.facade.AuthorFacade;
import com.asim.business.domain.author.model.dto.AuthorDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AuthorGatewayImpl implements AuthorGateway {
    //**only as types** Author & AuthorDto are directly referenced in book

    private final AuthorFacade authorFacade;

    public AuthorGatewayImpl(@Qualifier("authorFacadeImpl") AuthorFacade authorFacade) {
        this.authorFacade = authorFacade;
    }

    @Override
    public AuthorDto findMatchingAuthor(AuthorDto authorDto) {
        return authorFacade.findMatchingAuthor(authorDto);
    }

    @Override
    public void validateAuthorToCreate(AuthorDto authorDto) {
        authorFacade.validateAuthorToCreate(authorDto);
    }
}
