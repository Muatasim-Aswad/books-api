package com.asim.books.common.util;

import com.asim.books.common.exception.custom.BadRequestException;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class SortUtil {
    /**
     * Creates a Sort object from a string array
     *
     * @param sort String array containing property and direction pairs
     * @return Sort object
     */
    public static Sort createObject(String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();

        // sort=["field,direction", "field,direction"]
        for (String sortOrder : sort) {
            if (!sortOrder.contains(","))
                throw new BadRequestException("Sort query parameter", "It should be: ?sort=field,direction");

            String[] parts = sortOrder.split(",");
            org.springframework.data.domain.Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("desc") ?
                    org.springframework.data.domain.Sort.Direction.DESC : org.springframework.data.domain.Sort.Direction.ASC;

            orders.add(new org.springframework.data.domain.Sort.Order(direction, parts[0]));
        }


        return org.springframework.data.domain.Sort.by(orders);
    }
}
