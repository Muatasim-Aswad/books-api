package com.asim.books.book.gateway.authorGatewayImpl;

import com.asim.books.author.facade.AuthorFacade;
import com.asim.books.author.model.dto.AuthorDto;
import com.asim.books.book.gateway.AuthorGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AuthorGatewayImpl implements AuthorGateway {
    //**only as types** Author & AuthorDto are directly referenced in Book & BookDto respectively
    //This direct referencing happened due to the tight coupling between Book and Author entities in Hibernate
    //If possible, this coupling should be removed

    private final AuthorFacade authorFacade;

    public AuthorGatewayImpl(@Qualifier("authorFacadeImpl") AuthorFacade authorFacade) {
        this.authorFacade = authorFacade;
    }

    public AuthorDto findAuthorById(Long id) {
        return authorFacade.findAuthorById(id);
    }


    public boolean authorExists(Long id) {
        return authorFacade.authorExists(id);
    }

    public boolean findAuthorAndMatch(AuthorDto authorDto) {
        return authorFacade.findAuthorAndMatch(authorDto);
    }
}
