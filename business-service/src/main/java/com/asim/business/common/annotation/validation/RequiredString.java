package com.asim.business.common.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.*;

/**
 * Validation for required string fields.
 * Uses {@link NotBlank} to ensure the field is not empty or blank.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@NotBlank(message = "Required. This field cannot be empty or blank. e.g. 'string'")
public @interface RequiredString {
    Class<?>[] groups() default {};

    String message() default "Required. This field cannot be empty or blank. e.g. 'string'";

    Class<? extends Payload>[] payload() default {};

}
