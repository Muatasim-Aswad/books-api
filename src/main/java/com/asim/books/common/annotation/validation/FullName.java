package com.asim.books.common.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

/**
 * Common validation for full name.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
public @interface FullName {
    Class<?>[] groups() default {};

    String message() default "Name must be between 2 and 100 characters";

    Class<? extends Payload>[] payload() default {};
}
