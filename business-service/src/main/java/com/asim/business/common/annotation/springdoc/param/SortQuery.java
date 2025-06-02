package com.asim.business.common.annotation.springdoc.param;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

/**
 * Query parameter for sorting.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parameter(description = "Sorting criteria, pairs of field,direction(asc|desc). " +
        "Can be multiple and supports nesting like: 'parent.child'. " +
        "Can be written as a one param separated by comma ',' or multiple sort params.", example = "id,asc")
public @interface SortQuery {
}
