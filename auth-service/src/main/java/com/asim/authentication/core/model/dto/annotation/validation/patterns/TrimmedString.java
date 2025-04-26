package com.asim.authentication.core.model.dto.annotation.validation.patterns;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

/**
 * Pattern validation for trimmed strings.
 * No leading or trailing whitespace allowed.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Pattern(regexp = "^\\S.*\\S$|^\\S$", message = "String cannot have leading or trailing spaces")
public @interface TrimmedString {
    Class<?>[] groups() default {};

    String message() default "String cannot have leading or trailing spaces";

    Class<? extends Payload>[] payload() default {};
}