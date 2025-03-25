package com.asim.books.common.annotation.validation.domain;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.lang.annotation.*;

/**
 * Validation for valid IDs.
 * Uses {@link NotNull} to ensure the field is not null.
 * Uses {@link Positive} to ensure the field is positive.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@NotNull(message = "ID cannot be null")
@Positive(message = "ID must be positive")
public @interface ValidID {
    String message() default "Invalid ID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}