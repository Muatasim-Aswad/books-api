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

    /**
     * Helper method to format validation error messages consistently
     *
     * @param errorMessage   the explanation of the error
     * @param constraintName e.g. "NotNull", "Size", etc.
     */
    private String formatErrorMessage(String errorMessage, String constraintName) {
        if (constraintName != null) constraintName = constraintName.toLowerCase();
        return errorMessage + " (" + constraintName + " violation)";
    }

    /**
     * ** UserDto -> user
     * ** not a ..Dto -> "parameter"
     */
    private String normalizeClassName(String className) {
        boolean isDto = className != null && className.toLowerCase().contains("dto");

        if (!isDto) return "parameter";
        else {
            className = className.substring(0, className.length() - 3); //remove dto part
            className = className.substring(0, 1).toLowerCase() + className.substring(1); //lowercase first letter
        }

        return className;
    }

    // Helper method to process field errors and organize them into nested structure
    private void processFieldError(Map<String, Object> finalErrors, String fieldName, String message, String className) {
        // Handle nested fields (e.g., "author.name")
        if (fieldName.contains(".")) {
            String[] parts = fieldName.split("\\.", 2);
            String parent = parts[0];
            String child = parts[1];

            // Create or retrieve parent object
            @SuppressWarnings("unchecked")
            Map<String, Object> parentMap = (Map<String, Object>) finalErrors.computeIfAbsent(
                    parent, k -> new HashMap<String, Object>()
            );

            // Add the child field to parent map
            parentMap.put(child, message);
        } else {
            // For non-nested fields, group them under the class name
            @SuppressWarnings("unchecked")
            Map<String, Object> classMap = (Map<String, Object>) finalErrors.computeIfAbsent(
                    className, k -> new HashMap<String, Object>()
            );

            classMap.put(fieldName, message);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> finalErrors = new HashMap<>();

        // Get the class name from the target
        final String className = normalizeClassName(ex.getBindingResult().getObjectName());

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError fieldError) {
                String message = formatErrorMessage(error.getDefaultMessage(), error.getCode());
                processFieldError(finalErrors, fieldError.getField(), message, className);
            }
        });

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), finalErrors);
    }

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
                className = normalizeClassName(violation.getRootBeanClass().getSimpleName());
            }

            String errorCode = violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
            String message = formatErrorMessage(violation.getMessage(), errorCode);

            processFieldError(finalErrors, fieldName, message, className);
        }

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), finalErrors);
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

    // NoIdIsProvidedException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoIdIsProvidedException.class)
    public ErrorResponse handleNoIdIsProvidedException(NoIdIsProvidedException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
    }

    // Handles non-existing/undefined resources and paths
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class, ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandlerFound(Exception ex) {
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