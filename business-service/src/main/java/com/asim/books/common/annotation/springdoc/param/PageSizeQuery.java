package com.asim.books.common.annotation.springdoc.param;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

/**
 * Query parameter for page size.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parameter(description = "Number of resources to retrieve in a page. e.g. ?size=10", example = "10")
public @interface PageSizeQuery {
}
