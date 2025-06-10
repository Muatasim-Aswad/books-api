package com.asim.business.common.exception;

import lombok.Getter;

/**
 * Exception to be thrown when authentication fails.
 * Should result in a 401 Unauthorized response.
 */
@Getter
public class UnauthorizedException extends RuntimeException {
    private final String reason;

    public UnauthorizedException(String reason) {
        super("Unauthorized: " + reason);
        this.reason = reason;
    }
}