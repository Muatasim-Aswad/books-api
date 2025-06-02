package com.asim.business.domain.book.gateway;

import com.asim.business.domain.author.facade.AuthorFacade;

/**
 * Gateway for author operations.
 * Currently, it's a full user of the facade, therefor, it extends it.
 */
public interface AuthorGateway extends AuthorFacade {
}
