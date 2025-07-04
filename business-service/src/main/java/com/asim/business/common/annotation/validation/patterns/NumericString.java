package com.asim.business.common.annotation.validation.patterns;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

/**
 * Pattern validation for numeric strings (digits).
 * No whitespace or special characters allowed.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Pattern(regexp = "^[0-9]+$", message = "Must contain only numeric characters")
public @interface NumericString {
    Class<?>[] groups() default {};

    String message() default "Must contain only numeric characters";

    Class<? extends Payload>[] payload() default {};
}