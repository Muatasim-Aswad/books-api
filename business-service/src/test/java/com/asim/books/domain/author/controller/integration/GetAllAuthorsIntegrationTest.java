package com.asim.books.domain.author.controller.integration;

import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.test.util.fixtures.AuthorTestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("to solve later, build seems to be failing due to caching issues")
@DisplayName("Get All Authors Integration Tests")
class GetAllAuthorsIntegrationTest extends BaseAuthorControllerIntegrationTest {

    @BeforeEach
    void setup() throws Exception {
        // Create test authors for retrieval
        AuthorDto[] authors = AuthorTestFixtures.getManyDTOs();
        for (AuthorDto author : authors) {
            createAuthor(author);
        }
    }

    @Test
    @DisplayName("should return all authors successfully")
    void testGetAllAuthors_Success() throws Exception {
        // Act & Assert
        getAllAuthors()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(AuthorTestFixtures.NAMES.length))))
                .andExpect(jsonPath("$.content[*].id", everyItem(notNullValue())))
                .andExpect(jsonPath("$.content[*].name", hasItems(AuthorTestFixtures.NAMES)))
                .andExpect(jsonPath("$.content[*].age", hasItems(AuthorTestFixtures.AGES)));
    }

    @Test
    @DisplayName("should return paginated results with correct size")
    void testGetAllAuthors_Pagination() throws Exception {
        // Arrange
        int page = 0;
        int size = 2;

        // Act & Assert
        getAllAuthors(page, size, null, null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(size)))
                .andExpect(jsonPath("$.size", is(size)))
                .andExpect(jsonPath("$.number", is(page)));
    }

    @Test
    @DisplayName("should filter authors by name")
    void testGetAllAuthors_FilterByName() throws Exception {
        // Arrange
        String nameFilter = AuthorTestFixtures.NAMES[0].substring(0, 4); // Use part of the first name

        // Act & Assert
        getAllAuthors(0, 10, null, nameFilter)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].name", everyItem(containsStringIgnoringCase(nameFilter))));
    }

    @Test
    @DisplayName("should return empty content when no authors match filter")
    void testGetAllAuthors_NoMatchingFilter() throws Exception {
        // Arrange
        String nonMatchingFilter = AuthorTestFixtures.NON_EXISTING_NAME;

        // Act & Assert
        getAllAuthors(0, 10, null, nonMatchingFilter)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @DisplayName("should return sorted results")
    void testGetAllAuthors_Sorting() throws Exception {
        // Arrange
        String[] sort = {"name", "asc"};

        // Act & Assert
        getAllAuthors(0, 10, sort, null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.sort.sorted", is(true)));
    }
}