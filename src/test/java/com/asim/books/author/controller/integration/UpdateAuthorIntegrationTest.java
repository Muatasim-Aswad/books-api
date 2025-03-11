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

@DisplayName("Update Author Integration Tests")
class UpdateAuthorIntegrationTest extends BaseAuthorControllerIntegrationTest {

    private Long authorId;

    @BeforeEach
    void setup() throws Exception {
        // Create an author to update later
        AuthorDto author = new AuthorDto("Original Name", 30);
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
    @DisplayName("should update author name successfully")
    void testUpdateAuthor_UpdateName() throws Exception {
        AuthorDto updateDto = new AuthorDto();
        updateDto.setName("Updated Name");

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(BASE_URL + "/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(authorId.intValue())))
                .andExpect(jsonPath("$.name", is("Updated Name")))
                .andExpect(jsonPath("$.age", is(30)));
    }

    @Test
    @DisplayName("should update author age successfully")
    void testUpdateAuthor_UpdateAge() throws Exception {
        AuthorDto updateDto = new AuthorDto();
        updateDto.setAge(45);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(BASE_URL + "/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(authorId.intValue())))
                .andExpect(jsonPath("$.name", is("Original Name")))
                .andExpect(jsonPath("$.age", is(45)));
    }

    @Test
    @DisplayName("should update both author name and age successfully")
    void testUpdateAuthor_UpdateBothFields() throws Exception {
        AuthorDto updateDto = new AuthorDto();
        updateDto.setName("Completely New Name");
        updateDto.setAge(55);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(BASE_URL + "/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(authorId.intValue())))
                .andExpect(jsonPath("$.name", is("Completely New Name")))
                .andExpect(jsonPath("$.age", is(55)));
    }

    @Test
    @DisplayName("should return 400 when name is empty")
    void testUpdateAuthor_InvalidAge() throws Exception {
        AuthorDto updateDto = new AuthorDto();
        updateDto.setAge(-10);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(BASE_URL + "/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when name is empty")
    void testUpdateAuthor_InvalidNameLength() throws Exception {
        AuthorDto updateDto = new AuthorDto();
        updateDto.setName("A"); // Too short

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(BASE_URL + "/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 404 when author not found")
    void testUpdateAuthor_NotFound() throws Exception {
        AuthorDto updateDto = new AuthorDto();
        updateDto.setName("Valid Name");

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(BASE_URL + "/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isNotFound());
    }
}