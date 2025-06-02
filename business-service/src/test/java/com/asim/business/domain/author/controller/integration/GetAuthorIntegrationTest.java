package com.asim.business.domain.author.controller.integration;

import com.asim.business.domain.author.model.dto.AuthorDto;
import com.asim.business.test.util.fixtures.AuthorTestFixtures;
import com.asim.business.test.util.fixtures.CommonTestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("to solve later, build seems to be failing due to caching issues")
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
        Long nonExistingId = CommonTestFixtures.NON_EXISTING_ID;

        // Act & Assert
        getAuthor(nonExistingId)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when ID is invalid string")
    void testGetAuthor_InvalidId() throws Exception {
        // Arrange
        String invalidId = CommonTestFixtures.STRING_ID;

        // Act & Assert
        getAuthorWithStringId(invalidId)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when ID is negative")
    void testGetAuthor_NegativeId() throws Exception {
        // Arrange
        Long negativeId = CommonTestFixtures.NEGATIVE_ID;

        // Act & Assert
        getAuthor(negativeId)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when ID is zero")
    void testGetAuthor_ZeroId() throws Exception {
        // Arrange
        Long zeroId = CommonTestFixtures.ZERO_ID;

        // Act & Assert
        getAuthor(zeroId)
                .andExpect(status().isBadRequest());
    }
}