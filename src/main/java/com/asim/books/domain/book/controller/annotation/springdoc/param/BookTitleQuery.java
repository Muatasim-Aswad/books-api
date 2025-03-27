package com.asim.books.domain.book.controller.annotation.springdoc.param;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

/**
 * SpringDoc Parameter for book title query.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parameter(description = "Filter by the book title (case-insensitive, partial match). e.g. ?title=x", example = "x")
public @interface BookTitleQuery {
}
