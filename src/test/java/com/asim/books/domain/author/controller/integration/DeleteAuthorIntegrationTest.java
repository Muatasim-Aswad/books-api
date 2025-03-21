package com.asim.books.domain.author.controller.integration;

import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.test.util.fixtures.AuthorTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Delete Author Integration Tests")
class DeleteAuthorIntegrationTest extends BaseAuthorControllerIntegrationTest {

    @Test
    @DisplayName("should delete author successfully")
    void testDeleteAuthor_Success() throws Exception {
        // Arrange: Create an author first
        AuthorDto createdAuthor = createAndReturnAuthor(AuthorTestFixtures.getOneDto());
        Long authorId = createdAuthor.getId();

        // Act: Delete the author
        deleteAuthor(authorId)
                .andExpect(status().isNoContent());

        // Assert: Verify the author is deleted
        getAuthor(authorId)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 404 when author not found")
    void testDeleteAuthor_NotFound() throws Exception {
        // Arrange
        Long nonExistingId = AuthorTestFixtures.NON_EXISTING_ID;

        // Act & Assert
        deleteAuthor(nonExistingId)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when ID is invalid")
    void testDeleteAuthor_InvalidId() throws Exception {
        // Arrange
        String invalidId = AuthorTestFixtures.STRING_ID;

        // Act & Assert
        deleteAuthorWithStringId(invalidId)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when ID is negative")
    void testDeleteAuthor_NegativeId() throws Exception {
        // Arrange
        Long negativeId = AuthorTestFixtures.NEGATIVE_ID;

        // Act & Assert
        deleteAuthor(negativeId)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when ID is zero")
    void testDeleteAuthor_ZeroId() throws Exception {
        // Arrange
        Long zeroId = AuthorTestFixtures.ZERO_ID;

        // Act & Assert
        deleteAuthor(zeroId)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should reject null ID with 400 or 404 error")
    void testDeleteAuthor_NullId() throws Exception {
        // Act & Assert
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(BASE_URL + "/{id}", (Object) null)
        ).andExpect(status().is(
                anyOf(
                        equalTo(400),
                        equalTo(404)
                )
        ));
    }
}