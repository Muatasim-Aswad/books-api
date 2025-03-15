package com.asim.books.author.controller.integration;

import com.asim.books.author.model.dto.AuthorDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Get Author Integration Tests")
class GetAuthorIntegrationTest extends BaseAuthorControllerIntegrationTest {

    private Long authorId;

    @BeforeEach
    void setup() throws Exception {
        // Create an author to retrieve later
        AuthorDto author = new AuthorDto("Test Author", 42);
        String authorJson = objectMapper.writeValueAsString(author);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson)
                )
                .andExpect(status().isCreated())
                .andReturn();

        AuthorDto createdAuthor = objectMapper.readValue(
                result.getResponse().getContentAsString(), AuthorDto.class);
        authorId = createdAuthor.getId();
    }

    @Test
    @DisplayName("should return author successfully")
    void testGetAuthor_Success() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(BASE_URL + "/{id}", authorId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(authorId.intValue())))
                .andExpect(jsonPath("$.name", is("Test Author")))
                .andExpect(jsonPath("$.age", is(42)));
    }

    @Test
    @DisplayName("should return 404 when author not found")
    void testGetAuthor_NotFound() throws Exception {
        Long nonExistingId = 9999L;
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(BASE_URL + "/{id}", nonExistingId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when id is invalid")
    void testGetAuthor_InvalidId() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(BASE_URL + "/{id}", "invalid-id")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
}