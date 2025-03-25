package com.asim.books.infrastructure.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods for exception handling
 * Currently only used within validation error handling
 */
public interface Utils {

    /**
     * Helper method to format validation error messages consistently
     * Adds the constraint name to the error message
     *
     * @param errorMessage   the explanation of the error
     * @param constraintName e.g. "NotNull", "Size", etc.
     */
    static String formatErrorMessage(String errorMessage, String constraintName) {
        if (constraintName != null) constraintName = constraintName.toLowerCase();
        return errorMessage + " (" + constraintName + " violation)";
    }

    /**
     * ** UserDto -> user
     * ** not a ..Dto -> "parameter"
     */
    static String normalizeClassName(String className) {
        boolean isDto = className != null && className.toLowerCase().contains("dto");

        if (!isDto) return "parameter";
        else {
            //remove dto part
            className = className.substring(0, className.length() - 3);
            //lowercase first letter
            className = className.substring(0, 1).toLowerCase() + className.substring(1);
        }

        return className;
    }

    /**
     * Helper method to process field errors and group them by class name including nested fields
     * (e.g., "author.name":  => "author": {"name": "error message"})
     * (2 levels deep) For deeper nesting, add recursion
     * Warning: the state of the finalErrors map is mutated by this method
     */
    static void processFieldError(Map<String, Object> finalErrors, String fieldName, String message, String className) {
        // Create or retrieve the parent object
        @SuppressWarnings("unchecked")
        Map<String, Object> parentMap = (Map<String, Object>) finalErrors.computeIfAbsent(
                className, k -> new HashMap<String, Object>()
        );

        // Handle nested fields (e.g., "author.name")
        if (fieldName.contains(".")) {
            String[] parts = fieldName.split("\\.", 2);
            String child = parts[0];
            String field = parts[1];

            // Create or retrieve child object
            @SuppressWarnings("unchecked")
            Map<String, Object> childMap = (Map<String, Object>) parentMap.computeIfAbsent(
                    child, k -> new HashMap<String, Object>()
            );

            // Add the field to the child object
            childMap.put(field, message);
        } else {
            // For non-nested fields, group them under the class name
            parentMap.put(fieldName, message);
        }
    }

}
