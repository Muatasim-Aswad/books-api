package com.asim.books.domain.book.service;

import com.asim.books.common.exception.IllegalAttemptToModify;
import com.asim.books.common.exception.NoIdIsProvidedException;
import com.asim.books.common.exception.ResourceNotFoundException;
import com.asim.books.common.mapper.entity.EntityMapper;
import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.model.entity.Author;
import com.asim.books.domain.book.gateway.AuthorGateway;
import com.asim.books.domain.book.model.dto.BookDto;
import com.asim.books.domain.book.model.entity.Book;
import com.asim.books.domain.book.repository.BookRepository;
import com.asim.books.test.util.fixtures.AuthorTestFixtures;
import com.asim.books.test.util.fixtures.BookTestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Book Service Tests")
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private EntityMapper<Book, BookDto> bookMapper;

    @Mock
    private AuthorGateway authorGateway;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookDto bookDto;
    private Book book;
    private AuthorDto authorDto;

    @BeforeEach
    void setUp() {
        // Arrange
        bookDto = BookTestFixtures.getOneDto();
        book = BookTestFixtures.getOneEntity();
        authorDto = AuthorTestFixtures.getOneDto();
    }

    @Nested
    @DisplayName("Add Book Tests")
    class AddBookTests {
        @Test
        @DisplayName("should add a book successfully when valid data is provided")
        void whenAddBookWithValidData_thenBookIsCreated() {
            // Arrange
            when(authorGateway.findAuthorAndMatch(any(AuthorDto.class))).thenReturn(true);
            when(bookMapper.toEntity(bookDto)).thenReturn(book);
            when(bookRepository.save(book)).thenReturn(book);
            when(bookMapper.toDto(book)).thenReturn(bookDto);

            // Act
            BookDto result = bookService.addBook(bookDto);

            // Assert
            assertNotNull(result);
            assertEquals(bookDto, result);
            verify(authorGateway).findAuthorAndMatch(any(AuthorDto.class));
            verify(bookMapper).toEntity(bookDto);
            verify(bookRepository).save(book);
            verify(bookMapper).toDto(book);
        }

        @Test
        @DisplayName("should throw IllegalAttemptToModify when adding book with non-matching existing author")
        void whenAddBookWithNonMatchingExistingAuthor_thenThrowIllegalAttemptToModify() {
            // Arrange
            when(authorGateway.findAuthorAndMatch(any(AuthorDto.class))).thenReturn(false);

            // Act & Assert
            assertThrows(IllegalAttemptToModify.class, () -> bookService.addBook(bookDto));
            verify(authorGateway).findAuthorAndMatch(any(AuthorDto.class));
            verifyNoInteractions(bookMapper);
            verifyNoInteractions(bookRepository);
        }

        @Test
        @DisplayName("should validate new author when adding book with new author")
        void whenAddBookWithNewAuthor_thenValidateAuthor() {
            // Arrange
            when(authorGateway.findAuthorAndMatch(any(AuthorDto.class)))
                    .thenThrow(new NoIdIsProvidedException("Author"));
            doNothing().when(authorGateway).validateAuthorRequired(any(AuthorDto.class));
            when(bookMapper.toEntity(bookDto)).thenReturn(book);
            when(bookRepository.save(book)).thenReturn(book);
            when(bookMapper.toDto(book)).thenReturn(bookDto);

            // Act
            BookDto result = bookService.addBook(bookDto);

            // Assert
            assertNotNull(result);
            assertEquals(bookDto, result);
            verify(authorGateway).findAuthorAndMatch(any(AuthorDto.class));
            verify(authorGateway).validateAuthorRequired(any(AuthorDto.class));
            verify(bookMapper).toEntity(bookDto);
            verify(bookRepository).save(book);
            verify(bookMapper).toDto(book);
        }
    }

    @Nested
    @DisplayName("Get Book Tests")
    class GetBookTests {
        @Test
        @DisplayName("should retrieve a book when valid ID is provided")
        void whenGetBookWithValidId_thenBookIsReturned() {
            // Arrange
            Long id = 1L;
            when(bookRepository.findById(id)).thenReturn(Optional.of(book));
            when(bookMapper.toDto(book)).thenReturn(bookDto);

            // Act
            BookDto result = bookService.getBook(id);

            // Assert
            assertNotNull(result);
            assertEquals(bookDto, result);
            verify(bookRepository).findById(id);
            verify(bookMapper).toDto(book);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when retrieving non-existing book")
        void whenGetBookWithInvalidId_thenThrowResourceNotFoundException() {
            // Arrange
            Long id = 999L;
            when(bookRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> bookService.getBook(id));
            verify(bookRepository).findById(id);
            verifyNoInteractions(bookMapper);
        }
    }

    @Nested
    @DisplayName("Update Book Tests")
    class UpdateBookTests {
        @Test
        @DisplayName("should update book successfully when valid data is provided")
        void whenUpdateBookWithValidData_thenBookIsUpdated() {
            // Arrange
            Long id = 1L;
            BookDto updateDto = BookDto.builder()
                    .isbn(BookTestFixtures.UPDATED_ISBN)
                    .title(BookTestFixtures.UPDATED_TITLE)
                    .author(authorDto)
                    .build();

            Book updatedBook = Book.builder()
                    .id(id)
                    .isbn(BookTestFixtures.UPDATED_ISBN)
                    .title(BookTestFixtures.UPDATED_TITLE)
                    .author(Author.builder().id(1L).build())
                    .build();

            when(bookRepository.findById(id)).thenReturn(Optional.of(book));
            when(authorGateway.findAuthorAndMatch(any(AuthorDto.class))).thenReturn(true);
            when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
            when(bookMapper.toDto(updatedBook)).thenReturn(updateDto);

            // Act
            BookDto result = bookService.updateBook(id, updateDto);

            // Assert
            assertNotNull(result);
            assertEquals(updateDto, result);
            verify(bookRepository).findById(id);
            verify(authorGateway).findAuthorAndMatch(any(AuthorDto.class));
            verify(bookRepository).save(any(Book.class));
            verify(bookMapper).toDto(any(Book.class));
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when updating non-existing book")
        void whenUpdateNonExistingBook_thenThrowResourceNotFoundException() {
            // Arrange
            Long id = 999L;
            when(bookRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(id, bookDto));
            verify(bookRepository).findById(id);
            verifyNoMoreInteractions(bookRepository);
            verifyNoInteractions(bookMapper);
        }

        @Test
        @DisplayName("should throw IllegalAttemptToModify when updating book with non-matching existing author")
        void whenUpdateBookWithNonMatchingExistingAuthor_thenThrowIllegalAttemptToModify() {
            // Arrange
            Long id = 1L;
            when(bookRepository.findById(id)).thenReturn(Optional.of(book));
            when(authorGateway.findAuthorAndMatch(any(AuthorDto.class))).thenReturn(false);

            // Act & Assert
            assertThrows(IllegalAttemptToModify.class, () -> bookService.updateBook(id, bookDto));
            verify(bookRepository).findById(id);
            verify(authorGateway).findAuthorAndMatch(any(AuthorDto.class));
            verifyNoMoreInteractions(bookRepository);
        }
    }

    @Nested
    @DisplayName("Delete Book Tests")
    class DeleteBookTests {
        @Test
        @DisplayName("should delete book successfully when valid ID is provided")
        void whenDeleteBookWithValidId_thenBookIsDeleted() {
            // Arrange
            Long id = 1L;
            when(bookRepository.existsById(id)).thenReturn(true);
            doNothing().when(bookRepository).deleteById(id);

            // Act
            bookService.deleteBook(id);

            // Assert
            verify(bookRepository).existsById(id);
            verify(bookRepository).deleteById(id);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when deleting non-existing book")
        void whenDeleteNonExistingBook_thenThrowResourceNotFoundException() {
            // Arrange
            Long id = 999L;
            when(bookRepository.existsById(id)).thenReturn(false);

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(id));
            verify(bookRepository).existsById(id);
            verify(bookRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("Get Books Tests")
    class GetBooksTests {
        @Test
        @DisplayName("should retrieve all books with pagination when no filters are provided")
        void whenGetBooksWithoutFilters_thenAllBooksAreReturned() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            List<Book> books = Arrays.asList(BookTestFixtures.getManyEntities());
            Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

            when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(bookPage);
            when(bookMapper.toDto(any(Book.class))).thenAnswer(i -> {
                Book b = i.getArgument(0);
                return BookDto.builder()
                        .id(b.getId())
                        .isbn(b.getIsbn())
                        .title(b.getTitle())
                        .build();
            });

            // Act
            Page<BookDto> result = bookService.getBooks(pageable, null, null);

            // Assert
            assertNotNull(result);
            assertEquals(books.size(), result.getContent().size());
            verify(bookRepository).findAll(any(Specification.class), eq(pageable));
            verify(bookMapper, times(books.size())).toDto(any(Book.class));
        }

        @Test
        @DisplayName("should filter books by title when title filter is provided")
        void whenGetBooksWithTitleFilter_thenBooksAreFiltered() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            String titleFilter = "Java";
            List<Book> books = Arrays.asList(BookTestFixtures.getManyEntities()[0]);
            Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

            when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(bookPage);
            when(bookMapper.toDto(any(Book.class))).thenAnswer(i -> {
                Book b = i.getArgument(0);
                return BookDto.builder()
                        .id(b.getId())
                        .isbn(b.getIsbn())
                        .title(b.getTitle())
                        .build();
            });

            // Act
            Page<BookDto> result = bookService.getBooks(pageable, titleFilter, null);

            // Assert
            assertNotNull(result);
            assertEquals(books.size(), result.getContent().size());
            verify(bookRepository).findAll(any(Specification.class), eq(pageable));
            verify(bookMapper, times(books.size())).toDto(any(Book.class));
        }

        @Test
        @DisplayName("should filter books by author when author filter is provided")
        void whenGetBooksWithAuthorFilter_thenBooksAreFiltered() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            String authorFilter = "Name";
            List<Book> books = Arrays.asList(BookTestFixtures.getManyEntities()[0]);
            Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

            when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(bookPage);
            when(bookMapper.toDto(any(Book.class))).thenAnswer(i -> {
                Book b = i.getArgument(0);
                return BookDto.builder()
                        .id(b.getId())
                        .isbn(b.getIsbn())
                        .title(b.getTitle())
                        .build();
            });

            // Act
            Page<BookDto> result = bookService.getBooks(pageable, null, authorFilter);

            // Assert
            assertNotNull(result);
            assertEquals(books.size(), result.getContent().size());
            verify(bookRepository).findAll(any(Specification.class), eq(pageable));
            verify(bookMapper, times(books.size())).toDto(any(Book.class));
        }
    }
}