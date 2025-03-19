package com.asim.books.domain.author.controller.integration;

import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.test.util.BaseControllerIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

abstract class BaseAuthorControllerIntegrationTest extends BaseControllerIntegrationTest {
    protected static final String BASE_URL = "/authors";

    /**
     * Creates a new author via API
     *
     * @param authorDto Author DTO
     * @return ResultActions for further assertions
     */
    protected ResultActions createAuthor(AuthorDto authorDto) throws Exception {
        String authorJson = objectMapper.writeValueAsString(authorDto);
        return createAuthor(authorJson);
    }

    /**
     * Creates a new author via API using raw JSON
     *
     * @param authorJson Raw JSON representation of author
     * @return ResultActions for further assertions
     */
    protected ResultActions createAuthor(String authorJson) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        );
    }

    /**
     * Creates a new author via API and returns the created author
     *
     * @param authorDto Author DTO
     * @return AuthorDto of the created author
     */
    protected AuthorDto createAndReturnAuthor(AuthorDto authorDto) throws Exception {
        String response = createAuthor(authorDto)
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, AuthorDto.class);
    }

    /**
     * Gets an author by ID
     *
     * @param id Author ID
     * @return ResultActions for further assertions
     */
    protected ResultActions getAuthor(Long id) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .get(BASE_URL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
        );
    }

    /**
     * Deletes an author by ID
     *
     * @param id Author ID
     * @return ResultActions for further assertions
     */
    protected ResultActions deleteAuthor(Long id) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(BASE_URL + "/{id}", id)
        );
    }

    /**
     * Updates an author with patch method
     *
     * @param id        Author ID
     * @param updateDto Author DTO with updated data
     * @return ResultActions for further assertions
     */
    protected ResultActions updateAuthor(Long id, AuthorDto updateDto) throws Exception {
        String updateJson = objectMapper.writeValueAsString(updateDto);
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(BASE_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson)
        );
    }

    /**
     * Gets all authors with default pagination parameters
     *
     * @return ResultActions for further assertions
     */
    protected ResultActions getAllAuthors() throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
        );
    }

    /**
     * Gets all authors with pagination and filtering
     *
     * @param page Page number
     * @param size Page size
     * @param sort Sorting parameters
     * @param name Name filter
     * @return ResultActions for further assertions
     */
    protected ResultActions getAllAuthors(int page, int size, String[] sort, String name) throws Exception {
        var builder = MockMvcRequestBuilders
                .get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size));

        if (name != null) {
            builder.param("name", name);
        }

        if (sort != null) {
            for (String sortParam : sort) {
                builder.param("sort", sortParam);
            }
        }

        return mockMvc.perform(builder);
    }

    /**
     * Gets an author by ID (as string - for testing invalid IDs)
     *
     * @param id Author ID as string
     * @return ResultActions for further assertions
     */
    protected ResultActions getAuthorWithStringId(String id) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .get(BASE_URL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
        );
    }

    /**
     * Updates an author with patch method using string ID (for testing invalid IDs)
     *
     * @param id        Author ID as string
     * @param updateDto Author DTO with updated data
     * @return ResultActions for further assertions
     */
    protected ResultActions updateAuthorWithStringId(String id, AuthorDto updateDto) throws Exception {
        String updateJson = objectMapper.writeValueAsString(updateDto);
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(BASE_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson)
        );
    }

    /**
     * Deletes an author by ID (as string - for testing invalid IDs)
     *
     * @param id Author ID as string
     * @return ResultActions for further assertions
     */
    protected ResultActions deleteAuthorWithStringId(String id) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(BASE_URL + "/{id}", id)
        );
    }
}