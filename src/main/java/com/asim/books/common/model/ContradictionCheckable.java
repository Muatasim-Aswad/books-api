package com.asim.books.common.model;

import com.asim.books.common.util.ContradictionUtils;

/**
 * A mixin interface that provides contradiction checking functionality.
 * Can be implemented by any DTO or model class that needs to check for contradictions
 * while ignoring null values.
 *
 * @param <T> The type that implements this interface
 */
public interface ContradictionCheckable<T> {

    /**
     * Checks if the provided object contradicts this one.
     * Only compares non-null fields. The absence of a field is not considered a contradiction.
     *
     * @param other The object to compare
     * @return true if there are contradictions, false otherwise
     */
    default boolean doesContradict(T other) {
        return ContradictionUtils.doesContradict(this, other);
    }
}