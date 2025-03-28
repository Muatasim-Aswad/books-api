package com.asim.books.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for reflection operations
 * * Made as a Spring component to enable caching of field names with @Cacheable.
 */
@Slf4j
@Component
public class ReflectionUtils {
    /**
     * Determines if a class is a custom class from the application
     * rather than a Java standard library, primitive, array, or enum.
     *
     * @param clazz The class to check
     * @return true if the class is a custom application class, false otherwise
     */
    @Cacheable("customClasses")
    public boolean isCustomClass(Class<?> clazz) {
        if (clazz == null || clazz.isPrimitive() || clazz.isArray() || clazz.isEnum()) {
            return false;
        }

        Package pkg = clazz.getPackage();
        if (pkg == null) return false;

        String packageName = pkg.getName();

        return packageName.startsWith("com.asim.books");
    }


    /**
     * Returns an array of fields from the given class
     * Result is cached using Spring's caching mechanism
     *
     * @param clazz Class to extract fields from
     * @return Array containing the fields
     */
    @Cacheable("declaredFields")
    public Field[] getDeclaredFields(Class<?> clazz) {
        try {
            return clazz.getDeclaredFields();
        } catch (SecurityException e) {
            log.error("Security exception while getting fields from class {}: {}", clazz.getName(), e.getMessage());
            return new Field[0];
        }
    }

    /**
     * Returns a set of field names from the given class
     * Result is cached using Spring's caching mechanism
     * Supports nested fields with dot notation
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

        // Get all declared fields from the class
        Field[] fields = getDeclaredFields(clazz);

        // Add all field names to the set
        for (Field field : fields) {
            String fieldName = prefix + field.getName();
            fieldNames.add(fieldName);

            // For non-primitive, non-JDK types, custom types recursively add nested fields
            Class<?> fieldType = field.getType();
            if (isCustomClass(fieldType)) {
                populateFieldNames(fieldType, fieldName + ".", fieldNames);
            }
        }

    }
}