package com.asim.auth.common.exception;

/**
 * Exception to be thrown when a request property is not valid.
 * Should result in a 400 Bad Request response.
 */
public class BadRequestException extends RuntimeException {

    /**
     * e.g. where name is "name" and message is "must be at least 3 characters long"
     * message: "name is not valid: must be at least 3 characters long"
     *
     * @param name    The name of the property that is not valid.
     * @param message The message to be displayed.
     */
    public BadRequestException(String name, String message) {
        super(name + " is not valid: " + message);
    }

    /**
     * e.g. where name is "name"
     * message: "name is not valid"
     *
     * @param name The name of the property that is not valid.
     */
    public BadRequestException(String name) {
        super(name + " is not valid");
    }
}
