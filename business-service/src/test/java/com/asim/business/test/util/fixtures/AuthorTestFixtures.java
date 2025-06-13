package com.asim.business.test.util.fixtures;

import com.asim.business.domain.author.model.dto.AuthorDto;
import com.asim.business.domain.author.model.entity.Author;

public final class AuthorTestFixtures {
    // Multiple authors
    public static final String[] NAMES = {
            "Name One", "Name Two", "Three", "Four", "Five", "Six"
    };
    public static final Integer[] AGES = {30, 45, 45, 60, 75, 90};

    // Single author
    public static final String NAME = "Original Name";
    public static final Integer AGE = 56;

    // Common test data
    public static final String UPDATED_NAME = "Updated Name";
    public static final Integer UPDATED_AGE = 45;
    public static final String NON_EXISTING_NAME = "Non Existing Name";

    // Invalid name - specific to author domain
    public static final String TOO_SHORT_NAME = "A";
    public static final String TOO_LONG_NAME = "A".repeat(101);

    // Invalid age - specific to author domain
    public static final Integer TOO_HIGH_AGE = 151;

    // Factory methods
    public static AuthorDto createDto(String name, Integer age) {
        return AuthorDto.builder()
                .name(name)
                .age(age)
                .build();
    }

    public static Author createEntity(String name, Integer age, Long id) {
        return Author.builder()
                .name(name)
                .age(age)
                .id(id)
                .version(CommonTestFixtures.INTEGER_BOUNDARY_POSITIVE)
                .createdBy(CommonTestFixtures.VALID_USERNAME)
                .lastModifiedBy(CommonTestFixtures.VALID_USERNAME)
                .createdAt(CommonTestFixtures.PRESENT_ZONED_DATE_TIME)
                .updatedAt(CommonTestFixtures.PRESENT_ZONED_DATE_TIME)
                .version(0)
                .build();
    }

    public static AuthorDto getOneDto() {
        return createDto(NAME, AGE);
    }

    public static AuthorDto getOneDtoWithId() {
        AuthorDto authorDto = createDto(NAME, AGE);
        authorDto.setId(CommonTestFixtures.POSITIVE_NUMBER);
        return authorDto;
    }

    public static AuthorDto getOneDtoWithAllFields() {
        AuthorDto authorDto = getOneDtoWithId();
        authorDto.setVersion(CommonTestFixtures.INTEGER_BOUNDARY_POSITIVE);
        authorDto.setCreatedBy(CommonTestFixtures.VALID_USERNAME);
        authorDto.setLastModifiedBy(CommonTestFixtures.VALID_USERNAME);
        authorDto.setCreatedAt(CommonTestFixtures.PRESENT_ZONED_DATE_TIME);
        authorDto.setUpdatedAt(CommonTestFixtures.PRESENT_ZONED_DATE_TIME);
        return authorDto;
    }

    public static AuthorDto[] getManyDTOs() {
        int length = NAMES.length;
        AuthorDto[] authorDTOs = new AuthorDto[length];

        for (int i = 0; i < length; i++) {
            authorDTOs[i] = createDto(NAMES[i], AGES[i]);
        }

        return authorDTOs;
    }

    public static Author getOneEntity() {
        return createEntity(NAME, AGE, CommonTestFixtures.VALID_ID);
    }

    public static Author[] getManyEntities() {
        int length = NAMES.length;
        Author[] authors = new Author[length];

        for (int i = 0; i < length; i++) {
            authors[i] = createEntity(NAMES[i], AGES[i], (long) i + 1);
        }

        return authors;
    }

    public static AuthorDto getInvalidDto() {
        return createDto(TOO_LONG_NAME, TOO_HIGH_AGE);
    }
}