package com.asim.books.domain.book.controller.integration;

import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.book.model.dto.BookDto;
import com.asim.books.test.util.BaseControllerIntegrationTest;
import com.asim.books.test.util.fixtures.AuthorTestFixtures;
import com.asim.books.test.util.fixtures.BookTestFixtures;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Base class for Book Controller integration tests.
 * Provides common utilities and test data for book controller tests.
 */
abstract class BaseBookControllerIntegrationTest extends BaseControllerIntegrationTest {

    protected static final String BOOKS_API = "/books";
    protected static final String BOOKS_ID_API = BOOKS_API + "/{id}";

    /**
     * Creates a book through the API and returns the result actions for further assertions.
     *
     * @param bookDto the book DTO to create
     * @return ResultActions for further assertions
     * @throws Exception if an error occurs during the request
     */
    protected ResultActions createBook(BookDto bookDto) throws Exception {
        return mockMvc.perform(
                post(BOOKS_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto))
        );
    }

    /**
     * Creates a standard valid book through the API.
     *
     * @return ResultActions for further assertions
     * @throws Exception if an error occurs during the request
     */
    protected ResultActions createValidBook() throws Exception {
        BookDto bookDto = BookTestFixtures.getOneDto();
        return createBook(bookDto);
    }

    /**
     * Retrieves a book by ID through the API.
     *
     * @param id the book ID to retrieve
     * @return ResultActions for further assertions
     * @throws Exception if an error occurs during the request
     */
    protected ResultActions getBook(Long id) throws Exception {
        return mockMvc.perform(
                get(BOOKS_ID_API, id)
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    /**
     * Updates a book through the API.
     *
     * @param id      the book ID to update
     * @param bookDto the updated book data
     * @return ResultActions for further assertions
     * @throws Exception if an error occurs during the request
     */
    protected ResultActions updateBook(Long id, BookDto bookDto) throws Exception {
        return mockMvc.perform(
                patch(BOOKS_ID_API, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto))
        );
    }

    /**
     * Deletes a book through the API.
     *
     * @param id the book ID to delete
     * @return ResultActions for further assertions
     * @throws Exception if an error occurs during the request
     */
    protected ResultActions deleteBook(Long id) throws Exception {
        return mockMvc.perform(
                delete(BOOKS_ID_API, id)
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    /**
     * Gets all books with optional filtering parameters.
     *
     * @param page   the page number
     * @param size   the page size
     * @param sort   the sort criteria
     * @param title  the title filter
     * @param author the author filter
     * @return ResultActions for further assertions
     * @throws Exception if an error occurs during the request
     */
    protected ResultActions getBooks(Integer page, Integer size, String[] sort, String title, String author) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(BOOKS_API)
                .contentType(MediaType.APPLICATION_JSON);

        if (page != null) {
            requestBuilder.param("page", String.valueOf(page));
        }

        if (size != null) {
            requestBuilder.param("size", String.valueOf(size));
        }

        if (title != null) {
            requestBuilder.param("title", title);
        }

        if (author != null) {
            requestBuilder.param("author", author);
        }

        if (sort != null) {
            for (String sortParam : sort) {
                requestBuilder.param("sort", sortParam);
            }
        }

        return mockMvc.perform(requestBuilder);
    }

    /**
     * Deletes an author by id.
     *
     * @param id the author id
     * @return ResultActions for further assertions
     */
    protected ResultActions deleteAuthor(Long id) throws Exception {
        return mockMvc.perform(
                delete("/authors/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }


    /**
     * Creates an author through the API and returns the result actions for further assertions.
     *
     * @param authorDto the author DTO to create
     * @return ResultActions for further assertions
     * @throws Exception if an error occurs during the request
     */
    protected ResultActions createAuthor(AuthorDto authorDto) throws Exception {
        return mockMvc.perform(
                post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto))
        );
    }

    protected AuthorDto createAuthorAndReturn(AuthorDto authorDto) throws Exception {
        String responseJson = createAuthor(authorDto)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(responseJson, AuthorDto.class);
    }


    /**
     * Creates a book with an existing author ID to simulate associating a book with an existing author.
     *
     * @return a BookDto with an existing author reference
     */
    protected BookDto createBookDtoWithExistingAuthor() throws Exception {
        //create an author in the api
        BookDto bookDto = BookTestFixtures.getOneDto();
        AuthorDto authorDto = AuthorTestFixtures.getOneDto();
        AuthorDto createdAuthor = createAuthorAndReturn(authorDto);

        bookDto.setAuthor(createdAuthor);
        return bookDto;
    }

    /**
     * Creates a book DTO with invalid ISBN for testing validation.
     *
     * @return BookDto with invalid ISBN
     */
    protected BookDto createBookDtoWithInvalidIsbn() {
        return BookTestFixtures.getInvalidIsbnDto();
    }

    /**
     * Creates a book DTO with invalid title for testing validation.
     *
     * @return BookDto with invalid title
     */
    protected BookDto createBookDtoWithInvalidTitle() {
        return BookTestFixtures.getInvalidTitleDto();
    }

    /**
     * Creates a book update DTO with only the title field set.
     *
     * @param title the new title
     * @return BookDto with only title set for partial update
     */
    protected BookDto createTitleUpdateDto(String title) {
        BookDto updateDto = new BookDto();
        updateDto.setTitle(title);
        return updateDto;
    }

    /**
     * Creates a book update DTO with only the ISBN field set.
     *
     * @param isbn the new ISBN
     * @return BookDto with only ISBN set for partial update
     */
    protected BookDto createIsbnUpdateDto(String isbn) {
        BookDto updateDto = new BookDto();
        updateDto.setIsbn(isbn);
        return updateDto;
    }

    /**
     * Helper method to extract a book ID from a successful create response.
     *
     * @param createResult the result actions from book creation
     * @return the extracted book ID
     * @throws Exception if an error occurs during extraction
     */
    protected Long extractBookIdFromResponse(ResultActions createResult) throws Exception {
        String responseContent = createResult
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookDto createdBook = objectMapper.readValue(responseContent, BookDto.class);
        return createdBook.getId();
    }
}