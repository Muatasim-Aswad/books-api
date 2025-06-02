package com.asim.books.common.exception;

/**
 * Exception to be thrown when a resource is not found.
 * Should result in a 404 Not Found response.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * e.g. where resourceType is "Author" and id is 1
     * message: "Author with id '1' not found"
     *
     * @param resourceType The type of the resource that is not found.
     * @param id           The id of the resource that is not found.
     */
    public ResourceNotFoundException(String resourceType, Long id) {
        super(resourceType + " with id '" + id + "' not found");
    }

    public ResourceNotFoundException(String resourceType, String name) {
        super(resourceType + " with name '" + name + "' not found");
    }
}