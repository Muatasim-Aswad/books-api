package com.asim.books.common.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

/**
 * This annotation is used to validate fields that are required of type number.
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
