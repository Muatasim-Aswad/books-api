package com.asim.books.infrastructure.exception;

import com.asim.books.common.exception.BadRequestException;
import com.asim.books.common.exception.DuplicateResourceException;
import com.asim.books.common.exception.IllegalAttemptToModify;
import com.asim.books.common.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    //in case of schema validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            String errorCode = error.getCode();
            if (errorCode != null) errorCode = errorCode.toLowerCase();

            errors.put(fieldName, errorMessage + " (" + errorCode + " violation)");
        });

        String objClassName = ex.getTarget().getClass().getSimpleName();
        Map<String, Map<String, String>> schema = new HashMap<>();
        schema.put(objClassName, errors);

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), schema);
    }

    //in case of a parameter validation error
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        String className = null;

        for (var violation : ex.getConstraintViolations()) {
            // Extract field name from the property path
            String fieldName = violation.getPropertyPath().toString();
            fieldName = fieldName.substring(fieldName.indexOf(".") + 1);

            String errorMessage = violation.getMessage();
            String errorCode = violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
            errors.put(fieldName, errorMessage + " (" + errorCode + " violation)");

            // Get the class name from the root bean class
            if (className == null) {
                className = violation.getRootBeanClass().getSimpleName();
            }
        }

        Map<String, Map<String, String>> wholeError = new HashMap<>();
        // Use the extracted class name instead of hardcoding "parameter"
        wholeError.put(className != null ? className : "parameter", errors);

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), wholeError);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
    }

    // in attempt to create a resource that already exists
    @ExceptionHandler({DuplicateResourceException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateResource(Exception ex) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Resource already exists"
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Data integrity violation: " + ex.getMessage()
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

    // Handles non-existing/undefined resources and paths
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class, ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandlerFound(Exception ex) {
        log.error(ex.getMessage(), ex);

        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
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