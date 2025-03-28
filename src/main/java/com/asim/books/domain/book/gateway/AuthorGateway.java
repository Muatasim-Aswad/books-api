package com.asim.books.domain.book.gateway;

import com.asim.books.domain.author.facade.AuthorFacade;

/**
 * Gateway for author operations.
 * Currently, it's a full user of the facade, therefor, it extends it.
 */
public interface AuthorGateway extends AuthorFacade {
}
