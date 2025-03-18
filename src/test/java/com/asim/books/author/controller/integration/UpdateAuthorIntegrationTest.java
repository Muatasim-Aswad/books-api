package com.asim.books.author.controller.integration;

import com.asim.books.author.model.dto.AuthorDto;
import com.asim.books.test.util.AuthorTestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Update Author Integration Tests")
class UpdateAuthorIntegrationTest extends BaseAuthorControllerIntegrationTest {

    private Long authorId;

    @BeforeEach
    void setup() throws Exception {
        // Create an author to update later
        AuthorDto createdAuthor = createAndReturnAuthor(AuthorTestFixtures.getOneDto());
        authorId = createdAuthor.getId();
    }

    @Test
    @DisplayName("should update author name successfully")
    void testUpdateAuthor_UpdateName() throws Exception {
        // Arrange
        AuthorDto updateDto = new AuthorDto();
        updateDto.setName(AuthorTestFixtures.UPDATED_NAME);

        // Act & Assert
        updateAuthor(authorId, updateDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(authorId.intValue())))
                .andExpect(jsonPath("$.name", is(AuthorTestFixtures.UPDATED_NAME)))
                .andExpect(jsonPath("$.age", is(AuthorTestFixtures.AGE)));
    }

    @Test
    @DisplayName("should update author age successfully")
    void testUpdateAuthor_UpdateAge() throws Exception {
        // Arrange
        AuthorDto updateDto = new AuthorDto();
        updateDto.setAge(AuthorTestFixtures.UPDATED_AGE);

        // Act & Assert
        updateAuthor(authorId, updateDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(authorId.intValue())))
                .andExpect(jsonPath("$.name", is(AuthorTestFixtures.NAME)))
                .andExpect(jsonPath("$.age", is(AuthorTestFixtures.UPDATED_AGE)));
    }

    @Test
    @DisplayName("should update both author name and age successfully")
    void testUpdateAuthor_UpdateBothFields() throws Exception {
        // Arrange
        AuthorDto updateDto = new AuthorDto();
        updateDto.setName(AuthorTestFixtures.UPDATED_NAME);
        updateDto.setAge(AuthorTestFixtures.UPDATED_AGE);

        // Act & Assert
        updateAuthor(authorId, updateDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(authorId.intValue())))
                .andExpect(jsonPath("$.name", is(AuthorTestFixtures.UPDATED_NAME)))
                .andExpect(jsonPath("$.age", is(AuthorTestFixtures.UPDATED_AGE)));
    }

    @Test
    @DisplayName("should return 400 when update data is invalid")
    void testUpdateAuthor_InvalidData() throws Exception {
        // Arrange
        AuthorDto updateDto = new AuthorDto();
        updateDto.setAge(AuthorTestFixtures.NEGATIVE_AGE);

        // Act & Assert
        updateAuthor(authorId, updateDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 404 when author not found")
    void testUpdateAuthor_NotFound() throws Exception {
        // Arrange
        AuthorDto updateDto = new AuthorDto();
        updateDto.setName(AuthorTestFixtures.UPDATED_NAME);
        Long nonExistingId = AuthorTestFixtures.NON_EXISTING_ID;

        // Act & Assert
        updateAuthor(nonExistingId, updateDto)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when ID is invalid")
    void testUpdateAuthor_InvalidId() throws Exception {
        // Arrange
        AuthorDto updateDto = new AuthorDto();
        updateDto.setName(AuthorTestFixtures.UPDATED_NAME);
        String invalidId = AuthorTestFixtures.STRING_ID;

        // Act & Assert
        updateAuthorWithStringId(invalidId, updateDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when ID is negative")
    void testUpdateAuthor_NegativeId() throws Exception {
        // Arrange
        AuthorDto updateDto = new AuthorDto();
        updateDto.setName(AuthorTestFixtures.UPDATED_NAME);
        Long negativeId = AuthorTestFixtures.NEGATIVE_ID;

        // Act & Assert
        updateAuthor(negativeId, updateDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when ID is zero")
    void testUpdateAuthor_ZeroId() throws Exception {
        // Arrange
        AuthorDto updateDto = new AuthorDto();
        updateDto.setName(AuthorTestFixtures.UPDATED_NAME);
        Long zeroId = AuthorTestFixtures.ZERO_ID;

        // Act & Assert
        updateAuthor(zeroId, updateDto)
                .andExpect(status().isBadRequest());
    }
}