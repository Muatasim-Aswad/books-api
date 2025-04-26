package com.asim.authentication.core.model.dto.annotation.validation.patterns;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Pattern validation for secure SQL strings.
 * Checks for potential SQL injection patterns.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {SecureSqlStringValidator.class})
public @interface SecureSqlString {
    Class<?>[] groups() default {};

    String message() default "String contains potential SQL injection patterns";

    Class<? extends Payload>[] payload() default {};
}