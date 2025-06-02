package com.asim.business.infrastructure.exception;

import lombok.Data;

import java.util.Map;

/**
 * Represents an error response that can be sent to the client.
 */
@Data
public class ErrorResponse {
    private int status;
    private String message;
    private Map<String, Object> schemaViolations;

    /**
     * For errors that are schema violations.
     *
     * @param value  The HTTP status code.
     * @param errors The schema violations.
     */
    public ErrorResponse(int value, Map<String, Object> errors) {
        this.status = value;
        this.schemaViolations = errors;
        this.message = "Validation failed, check the schemaViolations field for more information.";
    }

    /**
     * For errors that are expressed with only the message.
     *
     * @param value   The HTTP status code.
     * @param message The error message.
     */
    public ErrorResponse(int value, String message) {
        this.status = value;
        this.message = message;
    }
}