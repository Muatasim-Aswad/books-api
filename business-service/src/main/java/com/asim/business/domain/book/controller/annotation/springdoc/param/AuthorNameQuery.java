package com.asim.business.domain.book.controller.annotation.springdoc.param;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

/**
 * SpringDoc Parameter for author name query.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parameter(description = "Filter by the author name (case-insensitive, partial match). e.g. ?author=John", example = "John")
public @interface AuthorNameQuery {
}
