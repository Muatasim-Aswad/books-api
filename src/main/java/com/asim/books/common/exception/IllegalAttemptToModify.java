package com.asim.books.common.exception;

public class IllegalAttemptToModify extends Throwable {
    public IllegalAttemptToModify(String resourceName, Long id, String MoreInfo) {
        super("Illegal attempt to modify " + resourceName + " with id " + id + ". " + MoreInfo);
    }
}
