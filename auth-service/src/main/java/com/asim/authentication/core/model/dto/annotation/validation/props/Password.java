package com.asim.authentication.core.model.dto.annotation.validation.props;

import com.asim.authentication.core.model.dto.annotation.validation.patterns.SecureSqlString;
import com.asim.authentication.core.model.dto.annotation.validation.patterns.TrimmedString;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

/**
 * Validation for secure passwords.
 * Enforces:
 * - 8-72 character length
 * - At least one uppercase letter
 * - At least one lowercase letter
 * - At least one digit
 * - At least one special character
 * - No leading/trailing spaces (via {@link TrimmedString})
 * - No SQL injection patterns (via {@link SecureSqlString})
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@TrimmedString
@SecureSqlString
@Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
@Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
@Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
@Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit")
@Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*", message = "Password must contain at least one special character")
public @interface Password {
    Class<?>[] groups() default {};

    String message() default "Password must be 8-72 characters and include uppercase, lowercase, digit and special characters";

    Class<? extends Payload>[] payload() default {};
}