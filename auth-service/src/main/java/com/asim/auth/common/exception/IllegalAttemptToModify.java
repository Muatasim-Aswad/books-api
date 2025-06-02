package com.asim.auth.common.exception;

/**
 * Exception to be thrown when an attempt to modify a resource is illegal.
 * Should result in a 403 Forbidden response.
 */
public class IllegalAttemptToModify extends RuntimeException {

    /**
     * e.g. where resourceName is "Author", id is 1 and MoreInfo is "Author is referenced by a book"
     * message: "Illegal attempt to modify Author with id 1. Author is referenced by a book"
     *
     * @param resourceName The name of the resource that is being modified.
     * @param id           The id of the resource that is being modified.
     * @param MoreInfo     Additional information about why the modification is illegal.
     */
    public IllegalAttemptToModify(String resourceName, Long id, String MoreInfo) {
        super("Illegal attempt to modify " + resourceName + " with id " + id + ". " + MoreInfo);
    }

    public IllegalAttemptToModify(String resourceName) {
        super("Illegal attempt to modify " + resourceName);
    }
}
