package com.asim.books.common.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String name, String message) {
        super(name + " is not valid: " + message);
    }

    public BadRequestException(String name) {
        super(name + " is not valid");
    }
}
