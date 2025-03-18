package com.asim.books.common.util;

import com.asim.books.common.exception.custom.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SortUtil {
    /**
     * Creates a Sort object from a string array
     *
     * @param sort String array containing property and direction pairs
     * @return Sort object
     */
    public static Sort createObject(String[] sort) {
        if (sort == null || sort.length == 0) {
            return Sort.unsorted();
        }

        if (sort.length % 2 != 0)
            throw new BadRequestException("Sort query parameter", "The number of elements should be even representing pairs of field and direction. Example: '..?sort=field1,asc,field2,desc' or '..?sort=field1&sort=desc'");

        List<Sort.Order> orders = new ArrayList<>();

        for (int i = 0; i < sort.length; i += 2) {
            String field = sort[i];
            String direction = sort[i + 1];

            Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;

            orders.add(new Sort.Order(sortDirection, field));
        }


        return org.springframework.data.domain.Sort.by(orders);
    }
}
