package com.asim.books.domain.book.controller.annotation.springdoc.param;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

/**
 * Query parameter for name.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parameter(description = "Filter by the author name (case-insensitive, partial match). e.g. ?author=John", example = "John")
public @interface AuthorNameQuery {
}
