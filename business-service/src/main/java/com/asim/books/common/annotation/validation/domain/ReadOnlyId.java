package com.asim.books.common.annotation.validation.domain;

import com.asim.books.common.annotation.validation.ReadOnly;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Null;

import java.lang.annotation.*;

/**
 * Validation for read-only ID fields.
 * Provides a custom message compared to {@link ReadOnly}.
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
