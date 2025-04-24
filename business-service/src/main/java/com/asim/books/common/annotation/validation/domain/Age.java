package com.asim.books.common.annotation.validation.domain;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.lang.annotation.*;

/**
 * Validation for human age.
 * A number between 0 and 150.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Min(value = 0, message = "Age must be a positive number")
@Max(value = 150, message = "Age must be less than 150")
public @interface Age {
    Class<?>[] groups() default {};

    String message() default "Age must be between 0 and 150";

    Class<? extends Payload>[] payload() default {};
}
