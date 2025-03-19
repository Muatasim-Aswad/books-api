package com.asim.books.common.exception;

public class NoIdIsProvidedException extends RuntimeException {
    public NoIdIsProvidedException(String name) {
        super("No id is provided for " + name);
    }
}
