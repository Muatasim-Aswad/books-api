package com.asim.books.common.util;

import com.asim.books.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public final class SortUtils {

    private final ReflectionUtils reflectionUtils;

    /**
     * Creates a Sort object from a string array and validates fields against a class
     *
     * @param sort        String array containing property and direction pairs
     * @param entityClass Class to validate field names against (optional)
     * @return Sort object
     */
    public Sort createObject(String[] sort, Class<?> entityClass) {
        if (sort == null || sort.length == 0) {
            return Sort.unsorted();
        }

        if (sort.length % 2 != 0)
            throw new BadRequestException("Sort query parameter", "The number of elements should be even representing pairs of field and direction. Example: '..?sort=field1,asc,field2,desc' or '..?sort=field1&sort=desc'");

        List<Sort.Order> orders = new ArrayList<>();
        Set<String> validFields = entityClass != null ? reflectionUtils.getFieldNames(entityClass) : null;

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
}