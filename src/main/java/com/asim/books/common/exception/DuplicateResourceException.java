package com.asim.books.common.exception;

/**
 * Exception to be thrown when an attempt is made to create a resource that already exists.
 * Should result in a 409 Conflict response.
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * e.g. where resourceType is "Author", field is "name" and value is "John Doe"
     * message: "Author with name 'John Doe' already exists"
     *
     * @param resourceType The type of the resource that already exists.
     * @param field        The field that is duplicated.
     * @param value        The value that is duplicated.
     */
    public DuplicateResourceException(String resourceType, String field, String value) {
        super(resourceType + " with " + field + " '" + value + "' already exists");
    }
}