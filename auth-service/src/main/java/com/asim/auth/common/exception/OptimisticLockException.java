package com.asim.auth.common.exception;

/**
 * Exception to be thrown when an optimistic lock fails. e.g. providing an outdated version number.
 * Should result in a 409 Conflict response.
 */
public class OptimisticLockException extends RuntimeException {

    /**
     * e.g. where message is "Author with id 1 was modified by another user"
     * message: "Author with id 1 was modified by another user"
     *
     * @param message The message to be displayed.
     */
    public OptimisticLockException(String message) {
        super(message);
    }
}