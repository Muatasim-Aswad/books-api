package com.asim.books.author.controller.integration;

import com.asim.books.author.model.dto.AuthorDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Delete Author Integration Tests")
class DeleteAuthorIntegrationTest extends BaseAuthorControllerIntegrationTest {

    @Test
    @DisplayName("should delete author successfully")
    void testDeleteAuthor_Success() throws Exception {
        // First create an author
        AuthorDto author = new AuthorDto("Author To Delete", 35);
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
        Long authorId = createdAuthor.getId();

        // Then delete the author
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(BASE_URL + "/{id}", authorId)
                )
                .andExpect(status().isOk());

        // Verify the author is deleted
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(BASE_URL + "/{id}", authorId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 404 when author not found")
    void testDeleteAuthor_NotFound() throws Exception {
        Long nonExistingId = 9999L;
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(BASE_URL + "/{id}", nonExistingId)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when id is invalid")
    void testDeleteAuthor_InvalidId() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(BASE_URL + "/{id}", "invalid-id")
                )
                .andExpect(status().isBadRequest());
    }
}