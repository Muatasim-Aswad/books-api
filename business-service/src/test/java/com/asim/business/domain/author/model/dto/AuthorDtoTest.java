package com.asim.business.domain.author.model.dto;

import com.asim.business.test.util.ValidationTestHelper;
import com.asim.business.test.util.fixtures.AuthorTestFixtures;
import com.asim.business.test.util.fixtures.CommonTestFixtures;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuthorDto Tests")
class AuthorDtoTest {

    // Test data providers
    static Stream<Arguments> invalidNameProvider() {
        return Stream.of(
                Arguments.of(CommonTestFixtures.NULL_STRING, "cannot be empty or blank"),
                Arguments.of(CommonTestFixtures.EMPTY_STRING, "cannot be empty or blank"),
                Arguments.of(CommonTestFixtures.BLANK_STRING, "cannot be empty or blank"),
                Arguments.of(AuthorTestFixtures.TOO_SHORT_NAME, "between 2 and 100"),
                Arguments.of(AuthorTestFixtures.TOO_LONG_NAME, "between 2 and 100")
        );
    }

    static Stream<Arguments> invalidAgeProvider() {
        return Stream.of(
                Arguments.of(CommonTestFixtures.INTEGER_NULL, "cannot be null"),
                Arguments.of(CommonTestFixtures.INTEGER_BOUNDARY_NEGATIVE, "positive number"),
                Arguments.of(AuthorTestFixtures.TOO_HIGH_AGE, "less than 150")
        );
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {
        @Test
        @DisplayName("should pass validation when valid author dto")
        void whenValidAuthorDto_thenNoValidationErrors() {
            // Arrange
            AuthorDto author = AuthorTestFixtures.getOneDto();

            // Act
            Set<ConstraintViolation<AuthorDto>> violations = ValidationTestHelper.validate(author, AuthorDto.OnCreate.class);

            // Assert
            assertTrue(violations.isEmpty());
        }

        @ParameterizedTest(name = "{2}")
        @MethodSource("com.asim.books.domain.author.model.dto.AuthorDtoTest#invalidNameProvider")
        @DisplayName("should fail validation when name is invalid")
        void whenInvalidName_thenValidationFails(String name, String expectedMessage) {
            // Arrange
            AuthorDto author = AuthorTestFixtures.createDto(name, AuthorTestFixtures.AGE);

            // Act
            Set<ConstraintViolation<AuthorDto>> violations = ValidationTestHelper.validate(author, AuthorDto.OnCreate.class);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains(expectedMessage)));
        }

        @ParameterizedTest(name = "{2}")
        @MethodSource("com.asim.books.domain.author.model.dto.AuthorDtoTest#invalidAgeProvider")
        @DisplayName("should fail validation when age is invalid")
        void whenInvalidAge_thenValidationFails(Integer age, String expectedMessage) {
            // Arrange
            AuthorDto author = AuthorTestFixtures.createDto(AuthorTestFixtures.NAME, age);

            // Act
            Set<ConstraintViolation<AuthorDto>> violations = ValidationTestHelper.validate(author, AuthorDto.OnCreate.class);

            // Assert
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains(expectedMessage)));
        }

        @Test
        @DisplayName("should fail validation when auto-generated @null fields are set")
        void whenNonNullFieldsAreSet_thenValidationFails() {
            // Arrange
            AuthorDto author = AuthorDto.builder()
                    .id(1L)
                    .name(AuthorTestFixtures.NAME)
                    .age(AuthorTestFixtures.AGE)
                    .createdAt(ZonedDateTime.now())
                    .updatedAt(ZonedDateTime.now())
                    .version(1)
                    .build();

            // Act
            Set<ConstraintViolation<AuthorDto>> violations = ValidationTestHelper.validate(author, AuthorDto.OnCreate.class);

            // Assert
            assertFalse(violations.isEmpty());
            assertEquals(4, violations.size());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("id")));
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("createdAt")));
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("updatedAt")));
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("version")));
        }

        @Test
        @DisplayName("should not enforce the notNull on age when Required group is not used")
        void whenOptionalGroup_thenNoNotNullAgeConstraint() {
            // Arrange
            AuthorDto author = AuthorDto.builder()
                    .name(AuthorTestFixtures.NAME)
                    .build();

            // Act
            Set<ConstraintViolation<AuthorDto>> violations = ValidationTestHelper.validate(author);

            // Assert
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("should not enforce the notNull on name when Required group is not used")
        void whenOptionalGroup_thenNoNotNullNameConstraint() {
            // Arrange
            AuthorDto author = AuthorDto.builder()
                    .age(AuthorTestFixtures.AGE)
                    .build();

            // Act
            Set<ConstraintViolation<AuthorDto>> violations = ValidationTestHelper.validate(author);

            // Assert
            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Does Contradict Tests")
    class DoesContradictTests {
        @Test
        @DisplayName("should contradict when author is totally null")
        void whenCompareWithNull_thenNoContradiction() {
            // Arrange
            AuthorDto author = AuthorTestFixtures.getOneDto();

            // Act & Assert
            assertTrue(author.doesContradict(null));
        }

        @Test
        @DisplayName("should not contradict when authors are identical")
        void whenCompareWithIdentical_thenNoContradiction() {
            // Arrange
            AuthorDto author1 = AuthorTestFixtures.getOneDto();
            AuthorDto author2 = AuthorTestFixtures.getOneDto();

            // Act & Assert
            assertFalse(author1.doesContradict(author2));
        }

        @Test
        @DisplayName("should contradict when name is different")
        void whenNameDifferent_thenContradiction() {
            // Arrange
            AuthorDto author1 = AuthorTestFixtures.getOneDto();
            AuthorDto author2 = AuthorTestFixtures.createDto(AuthorTestFixtures.UPDATED_NAME, AuthorTestFixtures.AGE);

            // Act & Assert
            assertTrue(author1.doesContradict(author2));
        }

        @Test
        @DisplayName("should contradict when age is different")
        void whenAgeDifferent_thenContradiction() {
            // Arrange
            AuthorDto author1 = AuthorTestFixtures.getOneDto();
            AuthorDto author2 = AuthorTestFixtures.createDto(AuthorTestFixtures.NAME, AuthorTestFixtures.UPDATED_AGE);

            // Act & Assert
            assertTrue(author1.doesContradict(author2));
        }

        @Test
        @DisplayName("should contradict when id is different")
        void whenIdDifferent_thenContradiction() {
            // Arrange
            AuthorDto author1 = AuthorTestFixtures.getOneDto();
            AuthorDto author2 = AuthorTestFixtures.getOneDto();
            author1.setId(1L);
            author2.setId(2L);

            // Act & Assert
            assertTrue(author1.doesContradict(author2));
        }

        @Test
        @DisplayName("should not contradict when null fields are compared")
        void whenNullFields_thenNoContradiction() {
            // Arrange
            AuthorDto author1 = new AuthorDto();
            author1.setName(AuthorTestFixtures.NAME);

            AuthorDto author2 = new AuthorDto();
            author2.setAge(AuthorTestFixtures.AGE);

            // Act & Assert
            assertFalse(author1.doesContradict(author2));
        }
    }
}