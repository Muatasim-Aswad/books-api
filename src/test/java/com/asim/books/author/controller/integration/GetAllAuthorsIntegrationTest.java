package com.asim.books.author.controller.integration;

import com.asim.books.author.model.dto.AuthorDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Get All Authors Integration Tests")
class GetAllAuthorsIntegrationTest extends BaseAuthorControllerIntegrationTest {

    @BeforeEach
    void setup() throws Exception {
        // Create a few authors for the test
        createAuthor("Author One", 30);
        createAuthor("Author Two", 45);
        createAuthor("Author Three", 60);
    }

    private void createAuthor(String name, int age) throws Exception {
        AuthorDto author = new AuthorDto(name, age);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("should return all authors successfully")
    void testGetAllAuthors_Success() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$[*].id", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].name", hasItems("Author One", "Author Two", "Author Three")))
                .andExpect(jsonPath("$[*].age", hasItems(30, 45, 60)));
    }

    @Test
    @DisplayName("should return empty list when no authors found")
    void testGetAllAuthors_EmptyDatabase() throws Exception {
        // First delete all authors
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(result -> {
                    AuthorDto[] authors = objectMapper.readValue(
                            result.getResponse().getContentAsString(), AuthorDto[].class);

                    // Delete each author
                    for (AuthorDto author : authors) {
                        mockMvc.perform(
                                MockMvcRequestBuilders.delete(BASE_URL + "/{id}", author.getId())
                        );
                    }
                });

        // Then verify empty result
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}