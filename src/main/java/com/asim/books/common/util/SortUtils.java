package com.asim.books.common.util;

import com.asim.books.common.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public final class SortUtils {
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

        for (int i = 0; i < sort.length; i += 2) {
            String field = sort[i];
            String direction = sort[i + 1];

            if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc"))
                throw new BadRequestException("Sort query parameter", "Sort Direction should be 'asc' or 'desc'");

            if (validFields != null && !validFields.contains(field)) {
                throw new BadRequestException("Sort query parameter", "Field '" + field + "' does not exist in " + entityClass.getSimpleName());
            }

            Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;

            orders.add(new Sort.Order(sortDirection, field));
        }

        return Sort.by(orders);
    }

    /**
     * Returns a set of field names from the given class
     *
     * @param clazz Class to extract field names from
     * @return HashSet containing the field names
     */
    public static Set<String> getFieldNames(Class<?> clazz) {
        Set<String> fieldNames = new HashSet<>();

        try {
            // Get all declared fields from the class
            Field[] fields = clazz.getDeclaredFields();

            // Add all field names to the set
            for (Field field : fields) {
                fieldNames.add(field.getName());
            }
        } catch (SecurityException e) {
            log.error("Security exception while getting fields from class {}: {}", clazz.getName(), e.getMessage());
        }

        return fieldNames;
    }
}
