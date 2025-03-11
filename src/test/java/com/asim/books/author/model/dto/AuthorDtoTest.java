package com.asim.books.author.model.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("AuthorDto Validation Tests")
class AuthorDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Valid author should have no constraint violations")
    void whenValidAuthor_thenNoConstraintViolations() {
        // Given
        AuthorDto author = AuthorDto.builder()
                .name("John Doe")
                .age(30)
                .build();

        // When
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(author);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Empty name should cause constraint violation")
    void whenEmptyName_thenConstraintViolations() {
        // Given
        AuthorDto author = AuthorDto.builder()
                .name("")
                .age(30)
                .build();

        // When
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(author);

        // Then
        assertThat(violations.iterator().next().getMessage(), is("Author name must be between 2 and 100 characters"));
    }

    @Test
    @DisplayName("Name that's too short should cause constraint violation")
    void whenNameTooShort_thenConstraintViolations() {
        // Given
        AuthorDto author = AuthorDto.builder()
                .name("A")
                .age(30)
                .build();

        // When
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(author);

        // Then
        assertThat(violations, hasSize(1));
        assertThat(violations.iterator().next().getMessage(), is("Author name must be between 2 and 100 characters"));
    }

    @Test
    @DisplayName("Name that's too long should cause constraint violation")
    void whenNameTooLong_thenConstraintViolations() {
        // Given
        String longName = "A".repeat(101);
        AuthorDto author = AuthorDto.builder()
                .name(longName)
                .age(30)
                .build();

        // When
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(author);

        // Then
        assertThat(violations, hasSize(1));
        assertThat(violations.iterator().next().getMessage(), is("Author name must be between 2 and 100 characters"));
    }

    @Test
    @DisplayName("Null age should cause constraint violation")
    void whenAgeIsNull_thenConstraintViolations() {
        // Given
        AuthorDto author = AuthorDto.builder()
                .name("John Doe")
                .age(null)
                .build();

        // When
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(author);

        // Then
        assertThat(violations, hasSize(1));
        assertThat(violations.iterator().next().getMessage(), is("Age cannot be null"));
    }

    @Test
    @DisplayName("Negative age should cause constraint violation")
    void whenAgeIsNegative_thenConstraintViolations() {
        // Given
        AuthorDto author = AuthorDto.builder()
                .name("John Doe")
                .age(-1)
                .build();

        // When
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(author);

        // Then
        assertThat(violations, hasSize(1));
        assertThat(violations.iterator().next().getMessage(), is("Age must be a positive number"));
    }

    @Test
    @DisplayName("In Optional group, short name should still be validated if present")
    void whenValidatingOptionalGroup_withInvalidName_thenOnlyValidateIfPresent() {
        // Given
        AuthorDto author = AuthorDto.builder()
                .name("A") // Too short but should only be validated if present
                .build();

        // When
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(author, AuthorDto.Optional.class);

        // Then
        assertThat(violations, hasSize(1));
        assertThat(violations.iterator().next().getMessage(), is("Author name must be between 2 and 100 characters"));
    }

    @Test
    @DisplayName("In Optional group, missing name should not cause violation")
    void whenValidatingOptionalGroup_withMissingName_thenNoViolations() {
        // Given
        AuthorDto author = AuthorDto.builder().build(); // Empty name is okay in Optional group

        // When
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(author, AuthorDto.Optional.class);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("In Optional group, negative age should still be validated if present")
    void whenValidatingOptionalGroup_withNegativeAge_thenOnlyValidateIfPresent() {
        // Given
        AuthorDto author = AuthorDto.builder()
                .name("John Doe")
                .age(-5) // Negative but should be validated since it's present
                .build();

        // When
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(author, AuthorDto.Optional.class);

        // Then
        assertThat(violations, hasSize(1));
        assertThat(violations.iterator().next().getMessage(), is("Age must be a positive number"));
    }

    @Test
    @DisplayName("In Optional group, null age should not cause violation")
    void whenValidatingOptionalGroup_withNullAge_thenNoViolations() {
        // Given
        AuthorDto author = AuthorDto.builder()
                .name("John Doe")
                .age(null) // Null age is okay in Optional group
                .build();

        // When
        Set<ConstraintViolation<AuthorDto>> violations = validator.validate(author, AuthorDto.Optional.class);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Test constructors, getters and setters")
    void testConstructorAndGettersSetters() {
        // Default constructor + setters
        AuthorDto author1 = new AuthorDto();
        author1.setName("Author 1");
        author1.setAge(40);
        author1.setId(1L);

        assertThat(author1.getName(), is("Author 1"));
        assertThat(author1.getAge(), is(40));
        assertThat(author1.getId(), is(1L));

        // Constructor with name and age
        AuthorDto author2 = new AuthorDto("Author 2", 50);
        assertThat(author2.getName(), is("Author 2"));
        assertThat(author2.getAge(), is(50));
        assertThat(author2.getId(), is(nullValue()));

        // All args constructor
        AuthorDto author3 = new AuthorDto(2L, "Author 3", 60);
        assertThat(author3.getId(), is(2L));
        assertThat(author3.getName(), is("Author 3"));
        assertThat(author3.getAge(), is(60));
    }

    @Test
    @DisplayName("Builder should create valid object")
    void testBuilder() {
        AuthorDto author = AuthorDto.builder()
                .id(3L)
                .name("Builder Author")
                .age(35)
                .build();

        assertThat(author.getId(), is(3L));
        assertThat(author.getName(), is("Builder Author"));
        assertThat(author.getAge(), is(35));
    }

    @Test
    @DisplayName("equals() and hashCode() should work correctly")
    void testEqualsAndHashCode() {
        AuthorDto author1 = new AuthorDto(1L, "Same Author", 40);
        AuthorDto author2 = new AuthorDto(1L, "Same Author", 40);
        AuthorDto author3 = new AuthorDto(2L, "Different Author", 40);

        assertThat(author1.equals(author2), is(true));
        assertThat(author1.equals(author3), is(false));
        assertThat(author1.hashCode(), equalTo(author2.hashCode()));
    }

    @Test
    @DisplayName("toString() should return a string representation")
    void testToString() {
        AuthorDto author = new AuthorDto(1L, "Test Author", 45);
        String toString = author.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=Test Author"));
        assertTrue(toString.contains("age=45"));
    }
}