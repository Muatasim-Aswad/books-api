package com.asim.books.common.util;

import com.asim.books.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Utility class for creating Sort objects from query parameters
 */
@Component
@RequiredArgsConstructor
public final class SortUtils {

    private final ReflectionUtils reflectionUtils;

    /**
     * Loops through an array of strings and if found combined with comma, separates them into two elements consecutively.
     *
     * @param sort Original array of strings that may contain comma-separated values
     * @return A new array with comma-separated values split into separate elements
     */
    private String[] separateCombined(String[] sort) {
        if (sort == null || sort.length == 0) {
            return sort;
        }

        List<String> result = new ArrayList<>();

        for (String item : sort) {
            if (item.contains(",")) {
                String[] parts = item.split(",");
                Collections.addAll(result, parts);
            } else {
                result.add(item);
            }
        }

        return result.toArray(new String[0]);
    }

    /**
     * Creates a Sort object from a string array and validates fields against a class
     *
     * @param sort        String array containing property and direction pairs
     * @param entityClass Class to validate field names against
     * @return Sort object
     */
    public Sort createObject(String[] sort, Class<?> entityClass) {
        if (sort == null || sort.length == 0 || entityClass == null) {
            return Sort.unsorted();
        }

        if (sort.length % 2 != 0)
            throw new BadRequestException("Sort query parameter", "The number of elements should be even representing pairs of field and direction. Example: '..?sort=field1,asc,field2,desc' or '..?sort=field1&sort=desc'");

        //Separate if combined with comma e.g. sort=field1,asc,field2,desc
        sort = separateCombined(sort);

        List<Sort.Order> orders = new ArrayList<>();
        Set<String> validFields = reflectionUtils.getFieldNames(entityClass);

        //populate orders with Sort.Order objects
        for (int i = 0; i < sort.length; i += 2) {
            //get and check field
            String field = sort[i];
            if (validFields != null && !validFields.contains(field))
                throw new BadRequestException("Sort query parameter", "Field '" + field + "' does not exist in " + entityClass.getSimpleName());

            //get and check direction
            String direction = sort[i + 1];
            if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc"))
                throw new BadRequestException("Sort query parameter", "Sort Direction should be 'asc' or 'desc'." + " Found: " + direction);


            Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;

            orders.add(new Sort.Order(sortDirection, field));
        }

        return Sort.by(orders);
    }
}