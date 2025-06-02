package com.asim.business.common.annotation.validation.patterns;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

/**
 * Pattern validation for alphanumeric strings.
 * Allows whitespace.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "String must be alphanumeric, no special characters allowed")
public @interface AlphaNumeric {
    Class<?>[] groups() default {};

    String message() default "String must be alphanumeric, no special characters allowed";

    Class<? extends Payload>[] payload() default {};
}