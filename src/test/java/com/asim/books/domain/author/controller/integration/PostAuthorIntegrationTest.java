package com.asim.books.domain.author.controller.integration;

import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.test.util.AuthorTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Post Author Integration Tests")
class PostAuthorIntegrationTest extends BaseAuthorControllerIntegrationTest {

    @Test
    @DisplayName("should create author successfully when input is valid")
    void testCreateAuthor_Success() throws Exception {
        // Arrange
        AuthorDto author = AuthorTestFixtures.getOneDto();

        // Act & Assert
        createAuthor(author)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(AuthorTestFixtures.NAME))
                .andExpect(jsonPath("$.age").value(AuthorTestFixtures.AGE))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DisplayName("should return 400 when author name is empty")
    void testCreateAuthor_EmptyName() throws Exception {
        // Arrange
        AuthorDto author = AuthorDto.builder()
                .name(AuthorTestFixtures.EMPTY_NAME)
                .age(AuthorTestFixtures.AGE)
                .build();


        // Act & Assert
        createAuthor(author)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when author age is negative")
    void testCreateAuthor_NegativeAge() throws Exception {
        // Arrange
        AuthorDto author = AuthorDto.builder()
                .name(AuthorTestFixtures.NAME)
                .age(AuthorTestFixtures.NEGATIVE_AGE)
                .build();


        // Act & Assert
        createAuthor(author)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when JSON is invalid")
    void testCreateAuthor_InvalidJson() throws Exception {
        // Arrange
        String invalidJson = "{\"name\": \"Invalid Author\", age: invalid}";

        // Act & Assert
        createAuthor(invalidJson)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when required field is missing")
    void testCreateAuthor_MissingRequiredField() throws Exception {
        // Arrange
        String incompleteJson = "{\"age\": 45}";

        // Act & Assert
        createAuthor(incompleteJson)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when author name is too short")
    void testCreateAuthor_NameTooShort() throws Exception {
        // Arrange
        AuthorDto author = AuthorDto.builder()
                .name(AuthorTestFixtures.TOO_SHORT_NAME)
                .age(AuthorTestFixtures.AGE)
                .build();

        // Act & Assert
        createAuthor(author)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.schemaViolations.name").value("Name must be between 2 and 100 characters"));
    }

    @Test
    @DisplayName("should return 400 when author age is null")
    void testCreateAuthor_NullAge() throws Exception {
        // Arrange
        String authorJson = "{\"name\": \"Valid Author\", \"age\": null}";

        // Act & Assert
        createAuthor(authorJson)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.schemaViolations.age").value("Age cannot be null"));
    }

    @Test
    @DisplayName("should return 409 when author already exists")
    void testCreateAuthor_DuplicateAuthor() throws Exception {
        // Arrange
        AuthorDto author = AuthorTestFixtures.getOneDto();

        // Act: Create the author first time
        createAuthor(author)
                .andExpect(status().isCreated());

        // Act & Assert: Try to create the same author again
        createAuthor(author)
                .andExpect(status().isConflict());
    }
}