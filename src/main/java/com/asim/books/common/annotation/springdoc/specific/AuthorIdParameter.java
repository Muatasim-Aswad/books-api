package com.asim.books.common.annotation.springdoc.specific;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

/**
 * Common parameter for author id.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parameter(description = "Author ID", required = true, example = "1")
public @interface AuthorIdParameter {
}