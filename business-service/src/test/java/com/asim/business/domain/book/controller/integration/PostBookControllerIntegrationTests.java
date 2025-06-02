package com.asim.business.domain.book.controller.integration;

import com.asim.business.domain.author.model.dto.AuthorDto;
import com.asim.business.domain.book.model.dto.BookDto;
import com.asim.business.test.util.fixtures.AuthorTestFixtures;
import com.asim.business.test.util.fixtures.BookTestFixtures;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled("to solve later, build seems to be failing due to caching issues")
@DisplayName("Book Creation (POST) Integration Tests")
class PostBookControllerIntegrationTests extends BaseBookControllerIntegrationTest {

    @Test
    @DisplayName("should create book successfully when valid book data is provided")
    void whenCreateValidBook_thenReturnCreatedBookWithId() throws Exception {
        // Arrange
        BookDto bookDto = BookTestFixtures.getOneDto();

        // Act & Assert
        createBook(bookDto)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.isbn", is(bookDto.getIsbn())))
                .andExpect(jsonPath("$.title", is(bookDto.getTitle())))
                .andExpect(jsonPath("$.author.name", is(bookDto.getAuthor().getName())))
                .andExpect(jsonPath("$.author.age", is(bookDto.getAuthor().getAge())));
    }

    @Test
    @DisplayName("should create book with existing author when valid book with author ID is provided")
    void whenCreateBookWithExistingAuthor_thenReturnCreatedBook() throws Exception {
        // Arrange
        // First create an author through a book
        BookDto book = createBookDtoWithExistingAuthor();
        AuthorDto existingAuthor = book.getAuthor(); //assert it's exactly assigned to the new book in the db

        createBook(book)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author.id", is(existingAuthor.getId().intValue())))
                .andExpect(jsonPath("$.author.version", is(existingAuthor.getVersion())));
    }

    @Test
    @DisplayName("should fail when provided book data is invalid")
    void whenCreateInvalidBook_thenReturnBadRequest() throws Exception {
        // Arrange
        BookDto invalidBook = BookTestFixtures.getInvalidIsbnDto();

        // Act & Assert
        createBook(invalidBook)
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("com.asim.books.test.util.fixtures.BookTestFixtures#invalidIsbnsProvider")
    @DisplayName("should fail when provided ISBN is invalid")
    void whenCreateBookWithInvalidIsbn_thenReturnBadRequest(String invalidIsbn) throws Exception {
        // Arrange
        BookDto bookDto = BookTestFixtures.getOneDto();
        bookDto.setIsbn(invalidIsbn);

        // Act & Assert
        createBook(bookDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should fail when provided author data is invalid")
    void whenCreateBookWithInvalidAuthor_thenReturnBadRequest() throws Exception {
        // Arrange
        BookDto bookDto = BookTestFixtures.getOneDto();
        bookDto.getAuthor().setName(AuthorTestFixtures.TOO_SHORT_NAME);

        // Act & Assert
        createBook(bookDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should fail when title is missing")
    void whenCreateBookWithoutTitle_thenReturnBadRequest() throws Exception {
        // Arrange
        BookDto bookDto = BookTestFixtures.getOneDto();
        bookDto.setTitle(null);

        // Act & Assert
        createBook(bookDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should fail when ISBN is missing")
    void whenCreateBookWithoutIsbn_thenReturnBadRequest() throws Exception {
        // Arrange
        BookDto bookDto = BookTestFixtures.getOneDto();
        bookDto.setIsbn(null);

        // Act & Assert
        createBook(bookDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should fail when author is missing")
    void whenCreateBookWithoutAuthor_thenReturnBadRequest() throws Exception {
        // Arrange
        BookDto bookDto = BookTestFixtures.getOneDto();
        bookDto.setAuthor(null);

        // Act & Assert
        createBook(bookDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should fail when attempting to create book with invalid author ID")
    void whenCreateBookWithNonExistingAuthorId_thenReturnBadRequest() throws Exception {
        // Arrange
        BookDto bookDto = BookTestFixtures.getOneDto();
        bookDto.getAuthor().setId(9999L); // Non-existing ID

        // Act & Assert
        createBook(bookDto)
                .andExpect(status().isNotFound());
    }
}