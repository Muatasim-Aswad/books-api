package com.asim.books.common.annotation.springdoc.param;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

/**
 * Common parameter for author id.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parameter(description = "Resource ID. e.g. api/v1/resource/1", required = true, example = "1")
public @interface IdParam {
}