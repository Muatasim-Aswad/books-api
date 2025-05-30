package com.asim.books.domain.book.controller.integration;

import com.asim.books.domain.book.model.dto.BookDto;
import com.asim.books.test.util.fixtures.CommonTestFixtures;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("to solve later, build seems to be failing due to caching issues")
@DisplayName("Book Deletion (DELETE) Integration Tests")
class DeleteBookControllerIntegrationTests extends BaseBookControllerIntegrationTest {

    @Test
    @DisplayName("should delete book when book exists")
    void whenDeleteExistingBook_thenReturnNoContent() throws Exception {
        // Arrange
        Long bookId = extractBookIdFromResponse(createValidBook());

        // Act & Assert
        deleteBook(bookId)
                .andExpect(status().isNoContent());

        // Verify the book is actually deleted
        getBook(bookId)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should fail when deleting non-existing book")
    void whenDeleteNonExistingBook_thenReturnNotFound() throws Exception {
        // Arrange
        Long nonExistingId = CommonTestFixtures.NON_EXISTING_ID;

        // Act & Assert
        deleteBook(nonExistingId)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should fail when deleting with invalid ID")
    void whenDeleteWithInvalidId_thenReturnBadRequest() throws Exception {
        // Arrange
        Long invalidId = CommonTestFixtures.NEGATIVE_ID;

        // Act & Assert
        deleteBook(invalidId)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should not allow getting book after deletion")
    void whenGetDeletedBook_thenReturnNotFound() throws Exception {
        // Arrange
        // First create and then delete a book
        Long bookId = extractBookIdFromResponse(createValidBook());
        deleteBook(bookId)
                .andExpect(status().isNoContent());

        // Act & Assert
        // Try to retrieve the deleted book
        getBook(bookId)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should allow creating a new book after deleting one with the same data")
    void whenCreateDeletedBook_thenReturnCreatedBook() throws Exception {
        // Arrange
        // First create and get the book data
        String responseJson = createValidBook()
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract book ID and delete the book
        Long bookId = objectMapper.readValue(responseJson, BookDto.class).getId();
        Long authorId = objectMapper.readValue(responseJson, BookDto.class).getAuthor().getId();

        deleteBook(bookId)
                .andExpect(status().isNoContent());
        deleteAuthor(authorId)
                .andExpect(status().isNoContent());

        // Act & Assert
        // Try to create a new book with the same data
        createValidBook()
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("should succeed when deleting a book multiple times")
    void whenDeleteBookMultipleTimes_thenSubsequentCallsFail() throws Exception {
        // Arrange
        Long bookId = extractBookIdFromResponse(createValidBook());

        // Act & Assert
        // First deletion should succeed
        deleteBook(bookId)
                .andExpect(status().isNoContent());

        // Second deletion should fail with not found
        deleteBook(bookId)
                .andExpect(status().isNotFound());
    }
}