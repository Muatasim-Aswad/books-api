package com.asim.books.domain.author.model.dto;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Component
@Validated
public class AuthorDtoValidator {
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
    @Validated(AuthorDto.Required.class)
    public void validateRequired(@Valid AuthorDto author) {
    }
}