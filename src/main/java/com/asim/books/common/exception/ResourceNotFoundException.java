package com.asim.books.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, Long id) {
        super(resourceType + " with id '" + id + "' not found");
    }
}