package com.asim.books.author.controller.integration;

import com.asim.books.author.model.dto.AuthorDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("Post Author Integration Tests")
class PostAuthorIntegrationTest extends BaseAuthorControllerIntegrationTest {
    /*
     * Test Cases:
     * 1. addAuthor_WhenAuthorIsValid_ThenReturn201
     * 2. addAuthor_WhenAuthorIsInvalid_ThenReturn400
     * 3. addAuthor_WhenAuthorIsNull_ThenReturn400
     * 4. addAuthor_WhenAuthorIsDuplicate_ThenReturn400
     * */
    @Test
    @DisplayName("should add author successfully")
    void testAddAuthor_Success() throws Exception {
        AuthorDto author = new AuthorDto("J.K. Rowling", 56);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name").value("J.K. Rowling"))
                .andExpect(jsonPath("$.age").value(56))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DisplayName("should return 400 when author name is empty")
    void testAddAuthor_EmptyName() throws Exception {
        AuthorDto author = new AuthorDto("", 40);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when author age is negative")
    void testAddAuthor_NegativeAge() throws Exception {
        AuthorDto author = new AuthorDto("Stephen King", -5);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when author age is zero")
    void testAddAuthor_InvalidJson() throws Exception {
        String invalidJson = "{\"name\": \"Invalid Author\", age: invalid}";

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when author age is missing")
    void testAddAuthor_MissingRequiredFields() throws Exception {
        String incompleteJson = "{\"age\": 45}";

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(incompleteJson)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when author name is too short")
    void testAddAuthor_NameTooShort() throws Exception {
        AuthorDto author = new AuthorDto("A", 40);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.schemaViolations.name").value("Author name must be between 2 and 100 characters"));
    }

    @Test
    @DisplayName("should return 400 when author name is too long")
    void testAddAuthor_NullAge() throws Exception {
        String authorJson = "{\"name\": \"Valid Author\", \"age\": null}";

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.schemaViolations.age").value("Age cannot be null"));
    }

    @Test
    @DisplayName("should return 409 when author already exists")
    void testAddAuthor_DuplicateAuthor() throws Exception {
        AuthorDto author = new AuthorDto("J.K. Rowling", 56);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson)
                )
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
}