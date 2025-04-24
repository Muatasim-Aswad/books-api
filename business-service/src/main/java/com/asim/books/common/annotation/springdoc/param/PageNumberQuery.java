package com.asim.books.common.annotation.springdoc.param;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

/**
 * Query parameter for page number.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parameter(description = "Page number (0-indexed). e.g. ?page=0", example = "0")
public @interface PageNumberQuery {
}
