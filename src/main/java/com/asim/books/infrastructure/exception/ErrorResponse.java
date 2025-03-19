package com.asim.books.infrastructure.exception;

import lombok.Data;

import java.util.Map;

@Data

public class ErrorResponse {
    private int status;
    private String message;
    private Map<String, String> schemaViolations;

    public ErrorResponse(int value, Map<String, String> errors) {
        this.status = value;
        this.schemaViolations = errors;
    }

    public ErrorResponse(int value, String message) {
        this.status = value;
        this.message = message;
    }
}