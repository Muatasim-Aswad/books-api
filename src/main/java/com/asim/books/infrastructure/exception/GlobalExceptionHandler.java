package com.asim.books.infrastructure.exception;

import com.asim.books.common.exception.*;
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
    //400 Bad Request -----------------------------------------------------------
    // --------------------------------------------------------------------------

    /**
     * Thrown when a complete schema fails validation by @Valid or @Validated.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> finalErrors = new HashMap<>();

        // Get the class name from the target
        final String className = Utils.normalizeClassName(ex.getBindingResult().getObjectName());

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError fieldError) {
                String message = Utils.formatErrorMessage(error.getDefaultMessage(), error.getCode());
                Utils.processFieldError(finalErrors, fieldError.getField(), message, className);
            }
        });

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), finalErrors);
    }

    /**
     * Thrown when a single field parameter fails validation, or in custom validators.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, Object> finalErrors = new HashMap<>();
        String className = null;

        for (var violation : ex.getConstraintViolations()) {
            // Extract field name from the property path
            String fullPath = violation.getPropertyPath().toString();
            String fieldName = fullPath.substring(fullPath.indexOf(".") + 1);

            // Get the class name from the root bean class if not already set
            if (className == null) {
                className = Utils.normalizeClassName(violation.getRootBeanClass().getSimpleName());
            }

            String errorCode = violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
            String message = Utils.formatErrorMessage(violation.getMessage(), errorCode);

            Utils.processFieldError(finalErrors, fieldName, message, className);
        }

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), finalErrors);
    }

    /**
     * Thrown when a request parameter is not of the expected type.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid parameter: " + ex.getName() + " should be of type " + ex.getRequiredType().getSimpleName()
        );
    }

    /**
     * Thrown when the request body is not a valid JSON.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON request: " + ex.getMessage()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(NoIdIsProvidedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoIdIsProvidedException(NoIdIsProvidedException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
    }

    // 409 Conflict -------------------------------------------------------------
    // --------------------------------------------------------------------------

    @ExceptionHandler({DuplicateResourceException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateResource(Exception ex) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Resource already exists"
        );
    }

    /**
     * {@link DataIntegrityViolationException} is thrown when an integrity constraint is violated such as Unique Constraint.
     */
    @ExceptionHandler({DataIntegrityViolationException.class, OptimisticLockException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolation(Exception ex) {
        if (ex.getMessage().contains("duplicate") || ex.getMessage().contains("unique")) {
            return new ErrorResponse(
                    HttpStatus.CONFLICT.value(),
                    "Resource already exists"
            );
        }

        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Data integrity violation: " + ex.getMessage()
        );
    }

    // 403 Forbidden ------------------------------------------------------------
    // --------------------------------------------------------------------------

    @ExceptionHandler(IllegalAttemptToModify.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleIllegalAttemptToModify(IllegalAttemptToModify ex) {
        return new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage()
        );
    }

    // 404 Not Found ------------------------------------------------------------
    // --------------------------------------------------------------------------

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class, ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandlerFound(Exception ex) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
    }

    // 500 Internal Server Error ------------------------------------------------
    // --------------------------------------------------------------------------

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