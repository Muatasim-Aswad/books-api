package com.asim.books.common.annotation.validation.domain;

import com.asim.books.common.annotation.validation.patterns.AlphaNumeric;
import com.asim.books.common.annotation.validation.patterns.TrimmedString;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;


/**
 * Common validation for Book Titles.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@AlphaNumeric
@TrimmedString
@Size(min = 1, max = 255, message = "Book title must be between 1 and 255 characters")
public @interface BookTitle {
    Class<?>[] groups() default {};

    String message() default "Book title must be between 1 and 255 characters and alphanumeric";

    Class<? extends Payload>[] payload() default {};
}
