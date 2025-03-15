package com.asim.books.common.exception;

import com.asim.books.common.exception.custom.DuplicateResourceException;
import com.asim.books.common.exception.custom.IllegalAttemptToModify;
import com.asim.books.common.exception.custom.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //in case of validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors);
    }

    //in any request for a specific resource that does not exist
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
    }

    // in attempt to create a resource that already exists
    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateResource(DuplicateResourceException ex) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );
    }

    // Handle invalid method argument type like passing a string instead of a number
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid parameter: " + ex.getName() + " should be of type " + ex.getRequiredType().getSimpleName()
        );
    }

    // when the request body is not a valid JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON request: " + ex.getMessage()
        );
    }

    // Illegal attempt to modify a resource
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalAttemptToModify.class)
    public ErrorResponse handleIllegalAttemptToModify(IllegalAttemptToModify ex) {
        return new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage()
        );
    }

    // This should be the last resort exception handler
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception ex) {
        log.error("An unexpected error occurred", ex);

        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please try again later."
        );
    }
}