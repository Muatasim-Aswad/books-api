package com.asim.books.common.exception.custom;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String name, String message) {
        super(name + " is not valid: " + message);
    }

    public BadRequestException(String name) {
        super(name + " is not valid");
    }
}
