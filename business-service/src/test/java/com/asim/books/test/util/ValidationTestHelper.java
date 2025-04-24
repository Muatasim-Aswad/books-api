package com.asim.books.test.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

/**
 * Helper class for validation tests
 */
public final class ValidationTestHelper {
    private static final Validator validator;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private ValidationTestHelper() {
        // Utility class, no instances
    }

    /**
     * Validates an object against its constraints
     *
     * @param object The object to validate
     * @param <T>    The type of the object
     * @return Set of constraint violations
     */
    public static <T> Set<ConstraintViolation<T>> validate(T object) {
        return validator.validate(object);
    }

    /**
     * Validates an object against its constraints for a specific group
     *
     * @param object The object to validate
     * @param groups The validation groups
     * @param <T>    The type of the object
     * @return Set of constraint violations
     */
    public static <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        return validator.validate(object, groups);
    }
}