package com.asim.books.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for reflection operations
 * Made as a Spring component to enable caching of field names with @Cacheable.
 */
@Slf4j
@Component
public class ReflectionUtils {

    /**
     * Returns a set of field names from the given class
     * Result is cached using Spring's caching mechanism
     *
     * @param clazz Class to extract field names from
     * @return Set containing the field names
     */
    @Cacheable("fieldNames")
    public Set<String> getFieldNames(Class<?> clazz) {
        Set<String> fieldNames = new HashSet<>();
        populateFieldNames(clazz, "", fieldNames);
        return fieldNames;
    }

    /**
     * Recursively populates a set with field names from the given class
     *
     * @param clazz      Class to extract field names from
     * @param prefix     Prefix to add to the field names
     * @param fieldNames Set to populate with field names
     */
    private void populateFieldNames(Class<?> clazz, String prefix, Set<String> fieldNames) {
        try {
            // Get all declared fields from the class
            Field[] fields = clazz.getDeclaredFields();

            // Add all field names to the set
            for (Field field : fields) {
                String fieldName = prefix + field.getName();
                fieldNames.add(fieldName);

                // For non-primitive, non-JDK types, recursively add nested fields
                Class<?> fieldType = field.getType();
                if (!fieldType.isPrimitive() &&
                        !fieldType.isEnum() &&
                        !fieldType.getName().startsWith("java.") &&
                        !fieldType.getName().startsWith("javax.") &&
                        !fieldType.isArray() &&
                        !fieldType.equals(String.class)) {

                    populateFieldNames(fieldType, fieldName + ".", fieldNames);
                }
            }
        } catch (SecurityException e) {
            log.error("Security exception while getting fields from class {}: {}", clazz.getName(), e.getMessage());
        }
    }
}