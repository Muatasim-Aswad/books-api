package com.asim.books.domain.book.model.dto;

import com.asim.books.test.util.ValidationTestHelper;
import com.asim.books.test.util.fixtures.BookTestFixtures;
import com.asim.books.test.util.fixtures.CommonTestFixtures;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BookDto Validation Tests")
class BookDtoTest {

    @Test
    @DisplayName("should validate completely valid book DTO")
    void whenValidDto_thenNoViolations() {
        // Arrange
        BookDto bookDto = BookTestFixtures.getOneDto();

        // Act
        Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should reject completely invalid book DTO")
    void whenCompletelyInvalidDto_thenViolations() {
        // Arrange
        BookDto bookDto = BookTestFixtures.getCompletelyInvalidDto();

        // Act
        Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.size()).isGreaterThan(1);
    }

    @Nested
    @DisplayName("ISBN validation")
    class IsbnValidationTests {

        @Test
        @DisplayName("should accept valid ISBN when properly formatted")
        void whenValidIsbn_thenNoViolations() {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto);

            // Assert
            assertThat(violations).isEmpty();
        }

        @ParameterizedTest
        @MethodSource("com.asim.books.test.util.fixtures.BookTestFixtures#invalidIsbnsProvider")
        @DisplayName("should reject invalid ISBNs")
        void whenInvalidIsbn_thenViolations(String invalidIsbn) {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();
            bookDto.setIsbn(invalidIsbn);

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto);

            // Assert
            assertThat(violations).isNotEmpty();
            assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("isbn"))).isTrue();
        }

        @Test
        @DisplayName("should accept null ISBN for Default validation group")
        void whenNullIsbn_thenNoViolationsForDefaultGroup() {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();
            bookDto.setIsbn(null);

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto);

            // Assert
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("should reject null ISBN for Required validation group")
        void whenNullIsbn_thenViolationsForRequiredGroup() {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();
            bookDto.setIsbn(null);

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto, BookDto.OnCreate.class);

            // Assert
            assertThat(violations).isNotEmpty();
            assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("isbn"))).isTrue();
        }
    }

    @Nested
    @DisplayName("Title validation")
    class TitleValidationTests {
        @Test
        @DisplayName("should accept valid title when properly formatted")
        void whenValidTitle_thenNoViolations() {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto);

            // Assert
            assertThat(violations).isEmpty();
        }

        @ParameterizedTest
        @MethodSource("com.asim.books.test.util.fixtures.BookTestFixtures#invalidTitlesProvider")
        @DisplayName("should reject invalid titles")
        void whenInvalidTitle_thenViolations(String invalidTitle) {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();
            bookDto.setTitle(invalidTitle);

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto);

            // Assert
            assertThat(violations).isNotEmpty();
            assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title"))).isTrue();
        }

        @Test
        @DisplayName("should accept null title for Default validation group")
        void whenNullTitle_thenNoViolationsForDefaultGroup() {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();
            bookDto.setTitle(null);

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto);

            // Assert
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("should reject null title for Required validation group")
        void whenNullTitle_thenViolationsForRequiredGroup() {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();
            bookDto.setTitle(null);

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto, BookDto.OnCreate.class);

            // Assert
            assertThat(violations).isNotEmpty();
            assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title"))).isTrue();
        }

        @Test
        @DisplayName("should reject SQL injection attempts in title")
        void whenTitleContainsSqlInjection_thenViolations() {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();
            bookDto.setTitle(CommonTestFixtures.SQL_INJECTION);

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto);

            // Assert
            assertThat(violations).isNotEmpty();
            assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title"))).isTrue();
        }
    }

    @Nested
    @DisplayName("Author validation")
    class AuthorValidationTests {

        @Test
        @DisplayName("should accept valid author")
        void whenValidAuthor_thenNoViolations() {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto);

            // Assert
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("should accept null author for Default validation group")
        void whenNullAuthor_thenNoViolationsForDefaultGroup() {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();
            bookDto.setAuthor(null);

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto);

            // Assert
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("should reject null author for Required validation group")
        void whenNullAuthor_thenViolationsForRequiredGroup() {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();
            bookDto.setAuthor(null);

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto, BookDto.OnCreate.class);

            // Assert
            assertThat(violations).isNotEmpty();
            assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("author"))).isTrue();
        }
    }

    @Nested
    @DisplayName("Read-only fields validation")
    class ReadOnlyFieldsValidationTests {

        @Test
        @DisplayName("should not allow setting id for new book")
        void whenIdSetManually_thenViolations() {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();
            bookDto.setId(CommonTestFixtures.POSITIVE_NUMBER);

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto);

            // Assert
            assertThat(violations).isNotEmpty();
            assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("id"))).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"createdAt", "updatedAt", "createdBy", "updatedBy", "version"})
        @DisplayName("should not allow setting read-only fields")
        void whenReadOnlyFieldsSet_thenViolations(String fieldName) throws Exception {
            // Arrange
            BookDto bookDto = BookTestFixtures.getOneDto();
            switch (fieldName) {
                case "createdAt", "updatedAt" ->
                        BookDto.class.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
                                        java.time.ZonedDateTime.class)
                                .invoke(bookDto, CommonTestFixtures.PRESENT_ZONED_DATE_TIME);
                case "createdBy", "updatedBy" ->
                        BookDto.class.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
                                        Long.class)
                                .invoke(bookDto, CommonTestFixtures.VALID_ID);
                case "version" ->
                        BookDto.class.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
                                        Integer.class)
                                .invoke(bookDto, CommonTestFixtures.INTEGER_BOUNDARY_POSITIVE);
            }

            // Act
            Set<ConstraintViolation<BookDto>> violations = ValidationTestHelper.validate(bookDto, BookDto.OnCreate.class);

            // Assert
            assertThat(violations).isNotEmpty();
            assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(fieldName))).isTrue();
        }
    }
}