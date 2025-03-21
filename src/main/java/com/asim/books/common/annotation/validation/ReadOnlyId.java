package com.asim.books.common.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Null;

import java.lang.annotation.*;

/**
 * This annotation is used to validate fields that are read-only.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Null(message = "Id is read only, to perform an action on a specific resource use the path parameter. e.g. resources/1")
public @interface ReadOnlyId {
    Class<?>[] groups() default {};

    String message() default "Id is read only, to perform an action on a specific resource use the path parameter. e.g. resources/1";

    Class<? extends Payload>[] payload() default {};
}
