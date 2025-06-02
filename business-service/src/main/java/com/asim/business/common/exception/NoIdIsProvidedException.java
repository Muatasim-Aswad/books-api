package com.asim.business.common.exception;

/**
 * Exception to be thrown when no id is provided for a resource.
 * Should result in a 400 Bad Request response.
 */
public class NoIdIsProvidedException extends RuntimeException {

    /**
     * e.g. where name is "Author"
     * message: "No id is provided for Author"
     *
     * @param name The name of the resource that is missing an id.
     */
    public NoIdIsProvidedException(String name) {
        super("No id is provided for " + name);
    }
}
