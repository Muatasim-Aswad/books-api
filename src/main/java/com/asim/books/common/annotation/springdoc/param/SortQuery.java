package com.asim.books.common.annotation.springdoc.param;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

/**
 * Query parameter for sorting.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parameter(description = "Sorting criteria, pairs of field,direction(asc|desc), can be written in multiple sort queries or in a one separated by comma. e.g. ?sort=name,asc", example = "name,asc")
public @interface SortQuery {
}
