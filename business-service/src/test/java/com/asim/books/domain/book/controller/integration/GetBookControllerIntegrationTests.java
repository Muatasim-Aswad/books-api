package com.asim.books.domain.book.controller.integration;

import com.asim.books.domain.book.model.dto.BookDto;
import com.asim.books.test.util.fixtures.CommonTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Book Retrieval (GET) Integration Tests")
class GetBookControllerIntegrationTests extends BaseBookControllerIntegrationTest {

    @Test
    @DisplayName("should return book when book exists")
    void whenGetExistingBook_thenReturnBook() throws Exception {
        // Arrange
        // Create a book first to get a valid ID
        String responseJson = createValidBook()
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookDto createdBook = objectMapper.readValue(responseJson, BookDto.class);
        Long bookId = createdBook.getId();

        // Act & Assert
        getBook(bookId)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookId.intValue())))
                .andExpect(jsonPath("$.isbn", is(createdBook.getIsbn())))
                .andExpect(jsonPath("$.title", is(createdBook.getTitle())))
                .andExpect(jsonPath("$.author.name", is(createdBook.getAuthor().getName())))
                .andExpect(jsonPath("$.author.age", is(createdBook.getAuthor().getAge())));
    }

    @Test
    @DisplayName("should return not found when book does not exist")
    void whenGetNonExistingBook_thenReturnNotFound() throws Exception {
        // Arrange - Use a non-existing ID
        Long nonExistingId = CommonTestFixtures.NON_EXISTING_ID;

        // Act & Assert
        getBook(nonExistingId)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return bad request when ID is invalid")
    void whenGetWithInvalidId_thenReturnBadRequest() throws Exception {
        // Arrange - Use an invalid negative ID
        Long invalidId = CommonTestFixtures.NEGATIVE_ID;

        // Act & Assert
        getBook(invalidId)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return book with associated author when book exists")
    void whenGetExistingBook_thenReturnBookWithAuthor() throws Exception {
        // Arrange
        // Create a book first to get a valid ID
        String responseJson = createValidBook()
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookDto createdBook = objectMapper.readValue(responseJson, BookDto.class);
        Long bookId = createdBook.getId();
        Long authorId = createdBook.getAuthor().getId();

        // Act & Assert
        getBook(bookId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author", is(notNullValue())))
                .andExpect(jsonPath("$.author.id", is(authorId.intValue())))
                .andExpect(jsonPath("$.author.name", is(createdBook.getAuthor().getName())))
                .andExpect(jsonPath("$.author.age", is(createdBook.getAuthor().getAge())));
    }

    @Test
    @DisplayName("should return book with creator information when book exists")
    void whenGetExistingBook_thenReturnBookWithMetadata() throws Exception {
        // Arrange
        Long bookId = extractBookIdFromResponse(createValidBook());

        // Act & Assert
        getBook(bookId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdAt", is(notNullValue())))
                .andExpect(jsonPath("$.version", is(notNullValue())));
    }

    @Test
    @DisplayName("should return consistent data between creation and retrieval")
    void whenCreateThenGetBook_thenDataIsConsistent() throws Exception {
        // Arrange
        // Create a book with specific data
        BookDto bookDto = createBookDtoWithExistingAuthor(); // Using helper to create a book with an existing author
        String createResponseJson = createBook(bookDto)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookDto createdBook = objectMapper.readValue(createResponseJson, BookDto.class);
        Long bookId = createdBook.getId();

        // Act & Assert
        getBook(bookId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdBook.getId().intValue())))
                .andExpect(jsonPath("$.isbn", is(createdBook.getIsbn())))
                .andExpect(jsonPath("$.title", is(createdBook.getTitle())))
                .andExpect(jsonPath("$.author.id", is(createdBook.getAuthor().getId().intValue())))
                .andExpect(jsonPath("$.author.name", is(createdBook.getAuthor().getName())));
    }
}