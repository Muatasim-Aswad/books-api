package com.asim.books.common.util;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Utility class for checking contradictions between values.
 */
public final class ContradictionUtils {
    /**
     * Checks if two values contradict each other.
     * A contradiction exists if both values are non-null and not equal.
     * A null value does not contradict any other value.
     *
     * @param value1 First value to compare
     * @param value2 Second value to compare
     * @return true if values contradict, false otherwise
     */
    public static <T> boolean contradicts(T value1, T value2) {
        if (value1 == null || value2 == null) return false;

        if (value1 instanceof ZonedDateTime && value2 instanceof ZonedDateTime) {
            // Compare instants (epoch time) rather than formatted representations
            return !((ZonedDateTime) value1).toInstant()
                    .equals(((ZonedDateTime) value2).toInstant());
        }

        return !value1.equals(value2);
    }

    /**
     * Automatically checks if two objects contradict each other by comparing all their fields.
     * Only compares fields that are non-null in both objects.
     *
     * @param obj1 First object to compare
     * @param obj2 Second object to compare
     * @param <T>  The type of objects being compared
     * @return true if there are contradictions, false otherwise
     */
    public static <T> boolean doesContradict(T obj1, T obj2) {
        if (obj1 == null || obj2 == null) {
            return obj1 != obj2; // They contradict if one is null and the other isn't
        }

        if (!obj1.getClass().equals(obj2.getClass())) {
            throw new IllegalArgumentException("Cannot compare objects of different types");
        }

        if (obj1.equals(obj2)) {
            return false;
        }

        Field[] fields = obj1.getClass().getDeclaredFields();

        Predicate<Field> doesContradict = field -> {
            try {
                field.setAccessible(true);
                Object value1 = field.get(obj1);
                Object value2 = field.get(obj2);
                return contradicts(value1, value2);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        };

        return Arrays.stream(fields).anyMatch(doesContradict);
    }
}