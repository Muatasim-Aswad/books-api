package com.asim.books.common.util;

import com.asim.books.common.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class SortUtils {
    /**
     * Returns a set of field names from the given class
     *
     * @param clazz Class to extract field names from
     * @return HashSet containing the field names
     */
    private static final Map<Class<?>, Set<String>> FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * Creates a Sort object from a string array and validates fields against a class
     *
     * @param sort        String array containing property and direction pairs
     * @param entityClass Class to validate field names against (optional)
     * @return Sort object
     */
    public static Sort createObject(String[] sort, Class<?> entityClass) {
        if (sort == null || sort.length == 0) {
            return Sort.unsorted();
        }

        if (sort.length % 2 != 0)
            throw new BadRequestException("Sort query parameter", "The number of elements should be even representing pairs of field and direction. Example: '..?sort=field1,asc,field2,desc' or '..?sort=field1&sort=desc'");

        List<Sort.Order> orders = new ArrayList<>();
        Set<String> validFields = entityClass != null ? getFieldNames(entityClass) : null;

        if (sort[0].contains(",")) {
            String[] temp = new String[sort.length * 2];
            for (int i = 0; i < sort.length; i++) {
                String[] parts = sort[i].split(",");
                temp[i * 2] = parts[0];
                temp[i * 2 + 1] = parts[1];
            }
            sort = temp;
        }

        for (int i = 0; i < sort.length; i += 2) {
            String field = sort[i];
            String direction = sort[i + 1];

            if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc"))
                throw new BadRequestException("Sort query parameter", "Sort Direction should be 'asc' or 'desc'." + " Found: " + direction);

            if (validFields != null && !validFields.contains(field)) {
                throw new BadRequestException("Sort query parameter", "Field '" + field + "' does not exist in " + entityClass.getSimpleName());
            }

            Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;

            orders.add(new Sort.Order(sortDirection, field));
        }

        return Sort.by(orders);
    }

    public static Set<String> getFieldNames(Class<?> clazz) {
        return FIELD_CACHE.computeIfAbsent(clazz, SortUtils::extractFieldNames);
    }

    private static Set<String> extractFieldNames(Class<?> clazz) {
        Set<String> fieldNames = new HashSet<>();
        populateFieldNames(clazz, "", fieldNames);
        return fieldNames;
    }

    private static void populateFieldNames(Class<?> clazz, String prefix, Set<String> fieldNames) {
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
