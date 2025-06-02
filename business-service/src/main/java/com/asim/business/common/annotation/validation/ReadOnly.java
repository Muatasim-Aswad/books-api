package com.asim.business.common.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Null;

import java.lang.annotation.*;

/**
 * Validation for read-only fields.
 * Fields annotated with this annotation are read-only and cannot be set.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Null(message = "This field is read-only and cannot be set")
public @interface ReadOnly {
    Class<?>[] groups() default {};

    String message() default "This field is read-only";

    Class<? extends Payload>[] payload() default {};
}
