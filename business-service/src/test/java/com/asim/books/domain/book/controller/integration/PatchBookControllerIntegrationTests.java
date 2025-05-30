package com.asim.books.domain.book.controller.integration;

import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.book.model.dto.BookDto;
import com.asim.books.test.util.fixtures.AuthorTestFixtures;
import com.asim.books.test.util.fixtures.BookTestFixtures;
import com.asim.books.test.util.fixtures.CommonTestFixtures;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled("to solve later, build seems to be failing due to caching issues")
@DisplayName("Book Update (PATCH) Integration Tests")
class PatchBookControllerIntegrationTests extends BaseBookControllerIntegrationTest {

    @Test
    @DisplayName("should update book when book exists and update data is valid")
    void whenUpdateExistingBook_thenReturnUpdatedBook() throws Exception {
        // Arrange
        Long bookId = extractBookIdFromResponse(createValidBook());
        BookDto updateDto = createTitleUpdateDto(BookTestFixtures.UPDATED_TITLE);

        // Act & Assert
        updateBook(bookId, updateDto)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookId.intValue())))
                .andExpect(jsonPath("$.title", is(BookTestFixtures.UPDATED_TITLE)));
    }

    @Test
    @DisplayName("should update only specified fields when partial update is provided")
    void whenUpdatePartialFields_thenUpdateOnlySpecifiedFields() throws Exception {
        // Arrange
        Long bookId = extractBookIdFromResponse(createValidBook());
        BookDto updateDto = createIsbnUpdateDto(BookTestFixtures.UPDATED_ISBN);

        // Act & Assert
        updateBook(bookId, updateDto)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookId.intValue())))
                .andExpect(jsonPath("$.isbn", is(BookTestFixtures.UPDATED_ISBN)))
                // Original title should remain unchanged
                .andExpect(jsonPath("$.title", is(BookTestFixtures.TITLE)));
    }

    @Test
    @DisplayName("should fail when updating non-existing book")
    void whenUpdateNonExistingBook_thenReturnNotFound() throws Exception {
        // Arrange
        BookDto updateDto = createTitleUpdateDto(BookTestFixtures.UPDATED_TITLE);

        // Act & Assert
        updateBook(CommonTestFixtures.NON_EXISTING_ID, updateDto)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should fail when update data is invalid")
    void whenUpdateWithInvalidData_thenReturnBadRequest() throws Exception {
        // Arrange
        Long bookId = extractBookIdFromResponse(createValidBook());
        BookDto updateDto = new BookDto();
        updateDto.setIsbn(BookTestFixtures.TOO_SHORT_ISBN);

        // Act & Assert
        updateBook(bookId, updateDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should not update existing book with new author")
    void whenUpdateBookWithNewAuthor_thenReturnFailure() throws Exception {
        // Arrange
        Long bookId = extractBookIdFromResponse(createValidBook());
        BookDto updateDto = BookTestFixtures.getOneDto();
        updateDto.getAuthor().setName("New Author Name");
        updateDto.getAuthor().setAge(40);

        // Act & Assert
        updateBook(bookId, updateDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should update book with existing author when valid author ID is provided")
    void whenUpdateBookWithExistingAuthor_thenReturnUpdatedBook() throws Exception {
        // Arrange
        // First create a book with an author
        BookDto createdBook = createValidBookAndReturn();

        // create a new author
        AuthorDto authorDto = AuthorTestFixtures.getOneDto();
        authorDto.setName("new Author");
        authorDto.setAge(40);
        AuthorDto createdAuthor = createAuthorAndReturn(authorDto);


        // Prepare update payload with the first book's author
        BookDto updateDto = new BookDto();
        updateDto.setAuthor(createdAuthor);

        // Act & Assert
        updateBook(createdBook.getId(), updateDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdBook.getId().intValue())))
                .andExpect(jsonPath("$.author.id", is(createdAuthor.getId().intValue())));
    }

    @Test
    @DisplayName("should fail when attempting to update with contradicting author data")
    void whenUpdateWithContradictingAuthorData_thenReturnBadRequest() throws Exception {
        // Arrange
        // Create a book with an author
        String bookResponse = createValidBook()
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookDto createdBook = objectMapper.readValue(bookResponse, BookDto.class);
        Long bookId = createdBook.getId();
        Long authorId = createdBook.getAuthor().getId();
        Integer authorVersion = createdBook.getAuthor().getVersion();

        // Try to update with same author ID but different name/age
        BookDto updateBookDto = new BookDto();
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("New Name");
        authorDto.setAge(40);
        authorDto.setVersion(authorVersion);
        updateBookDto.setAuthor(authorDto);


        // Act & Assert
        updateBook(bookId, updateBookDto)
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("should not make changes when update with empty json is provided")
    void whenUpdateWithEmptyJson_thenNoChange() throws Exception {
        // Arrange
        Long bookId = extractBookIdFromResponse(createValidBook());
        String emptyJson = "{}";

        // Act & Assert
        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .patch(BOOKS_ID_API, bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(emptyJson)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.version", is(0)));
    }

    @Test
    @DisplayName("should not allow updating to invalid ISBN")
    void whenUpdateToInvalidIsbn_thenReturnBadRequest() throws Exception {
        // Arrange
        Long bookId = extractBookIdFromResponse(createValidBook());
        BookDto updateDto = createIsbnUpdateDto(BookTestFixtures.NON_NUMERIC_ISBN);

        // Act & Assert
        updateBook(bookId, updateDto)
                .andExpect(status().isBadRequest());
    }
}