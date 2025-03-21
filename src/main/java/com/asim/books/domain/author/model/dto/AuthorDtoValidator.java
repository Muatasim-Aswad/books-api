package com.asim.books.domain.author.model.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Slf4j
@Component
@Validated
public class AuthorDtoValidator {

    private final Validator validator;

    public AuthorDtoValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * Validates the default group. Enforces correct input but not (required).
     *
     * @param author the author to validate
     */
    public void validate(@Valid AuthorDto author) {
    }

    /**
     * Validates the required group. Enforces the existence of the field in addition to the default validations. (required)
     *
     * @param author the author to validate
     */
    public void validateRequired(@Validated(AuthorDto.Required.class) AuthorDto author) {
        log.info("AuthorDtoValidator.validateRequired({})", author);

        Set<ConstraintViolation<AuthorDto>> violations =
                validator.validate(author, AuthorDto.Required.class);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}