package com.asim.business.common.annotation.validation.domain;

import com.asim.business.common.annotation.validation.patterns.NumericString;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;


/**
 * Validation for ISBN.
 * 10 or 13 digits long numeric string.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@NumericString
@Pattern(regexp = "^.{10}$|^.{13}$", message = "ISBN must be either 10 or 13 digits long")
public @interface Isbn {
    Class<?>[] groups() default {};

    String message() default "ISBN must be valid";

    Class<? extends Payload>[] payload() default {};
}
