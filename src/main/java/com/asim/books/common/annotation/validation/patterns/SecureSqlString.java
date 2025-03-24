package com.asim.books.common.annotation.validation.patterns;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Common validation for secure SQL strings.
 * No SQL injection allowed.
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