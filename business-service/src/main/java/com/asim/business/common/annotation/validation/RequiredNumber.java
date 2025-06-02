package com.asim.business.common.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

/**
 * Validation for required numeric fields.
 * Uses {@link NotNull} to ensure the field is not null.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@NotNull(message = "Required. This numeric field cannot be null. e.g. 5")
public @interface RequiredNumber {
    Class<?>[] groups() default {};

    String message() default "Required. This numeric field cannot be null. e.g. 5";

    Class<? extends Payload>[] payload() default {};

}
