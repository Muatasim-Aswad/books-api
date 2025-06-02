package com.asim.business.domain.author.model.dto;

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
     * Validates according to the creation group. Enforces the existence of the field in addition to the default validations. (required)
     *
     * @param author the author to validate
     */
    @Validated(AuthorDto.OnCreate.class)
    public void validateOnCreate(@Valid AuthorDto author) {
    }
}