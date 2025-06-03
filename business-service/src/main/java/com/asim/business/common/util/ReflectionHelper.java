package com.asim.business.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper class for reflection operations (no caching logic)
 */
@Slf4j
@Component
public class ReflectionHelper {

    public boolean isCustomClass(Class<?> clazz) {
        if (clazz == null || clazz.isPrimitive() || clazz.isArray() || clazz.isEnum()) {
            return false;
        }

        Package pkg = clazz.getPackage();
        if (pkg == null) return false;

        String packageName = pkg.getName();
        return packageName.startsWith("com.asim.business");
    }

    public Field[] getDeclaredFields(Class<?> clazz) {
        try {
            return clazz.getDeclaredFields();
        } catch (SecurityException e) {
            log.error("Security exception while getting fields from class {}: {}", clazz.getName(), e.getMessage());
            return new Field[0];
        }
    }

    public Set<String> getFieldNames(Class<?> clazz, ReflectionUtils cacheService) {
        Set<String> fieldNames = new HashSet<>();
        populateFieldNames(clazz, "", fieldNames, cacheService);
        return fieldNames;
    }

    /**
     * Recursively populates a set with field names from the given class
     *
     * @param clazz      Class to extract field names from
     * @param prefix     Prefix to add to the field names
     * @param fieldNames Set to populate with field names
     */
    private void populateFieldNames(Class<?> clazz, String prefix, Set<String> fieldNames,
                                    ReflectionUtils cacheService) {
        // Use cache service for cached operations
        Field[] fields = cacheService.getDeclaredFields(clazz);

        for (Field field : fields) {
            String fieldName = prefix + field.getName();
            fieldNames.add(fieldName);

            Class<?> fieldType = field.getType();
            if (cacheService.isCustomClass(fieldType)) {
                populateFieldNames(fieldType, fieldName + ".", fieldNames, cacheService);
            }
        }
    }
}