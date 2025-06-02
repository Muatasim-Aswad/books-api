package com.asim.business.common.annotation.validation.domain;

import com.asim.business.common.annotation.validation.patterns.SecureSqlString;
import com.asim.business.common.annotation.validation.patterns.TrimmedString;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;


/**
 * Validation for Book Titles.
 * Alphanumeric characters with common punctuation marks.
 * Size between 1 and 255 characters.
 * Other constraints are applied by {@link TrimmedString}, {@link SecureSqlString}.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@TrimmedString
@SecureSqlString
@Pattern(regexp = "^[a-zA-Z0-9\\s.,:;!?&'\"--()]+$", message = "Book title can contain letters, numbers, spaces, and common punctuation marks (.,:;!?&'\"-()).")
@Size(min = 1, max = 255, message = "Book title must be between 1 and 255 characters")
public @interface BookTitle {
    Class<?>[] groups() default {};

    String message() default "Book title must be between 1 and 255 characters and alphanumeric";

    Class<? extends Payload>[] payload() default {};
}
