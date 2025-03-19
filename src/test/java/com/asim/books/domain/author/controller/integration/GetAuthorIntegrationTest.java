package com.asim.books.domain.author.controller.integration;

import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.test.util.AuthorTestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Get Author Integration Tests")
class GetAuthorIntegrationTest extends BaseAuthorControllerIntegrationTest {

    private Long authorId;

    @BeforeEach
    void setup() throws Exception {
        // Create an author to retrieve
        AuthorDto author = AuthorTestFixtures.getOneDto();
        AuthorDto createdAuthor = createAndReturnAuthor(author);
        authorId = createdAuthor.getId();
    }

    @Test
    @DisplayName("should return author successfully when ID exists")
    void testGetAuthor_Success() throws Exception {
        // Act & Assert
        getAuthor(authorId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(authorId.intValue())))
                .andExpect(jsonPath("$.name", is(AuthorTestFixtures.NAME)))
                .andExpect(jsonPath("$.age", is(AuthorTestFixtures.AGE)));
    }

    @Test
    @DisplayName("should return 404 when author not found")
    void testGetAuthor_NotFound() throws Exception {
        // Arrange
        Long nonExistingId = AuthorTestFixtures.NON_EXISTING_ID;

        // Act & Assert
        getAuthor(nonExistingId)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when ID is invalid string")
    void testGetAuthor_InvalidId() throws Exception {
        // Arrange
        String invalidId = AuthorTestFixtures.STRING_ID;

        // Act & Assert
        getAuthorWithStringId(invalidId)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when ID is negative")
    void testGetAuthor_NegativeId() throws Exception {
        // Arrange
        Long negativeId = AuthorTestFixtures.NEGATIVE_ID;

        // Act & Assert
        getAuthor(negativeId)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when ID is zero")
    void testGetAuthor_ZeroId() throws Exception {
        // Arrange
        Long zeroId = AuthorTestFixtures.ZERO_ID;

        // Act & Assert
        getAuthor(zeroId)
                .andExpect(status().isBadRequest());
    }
}