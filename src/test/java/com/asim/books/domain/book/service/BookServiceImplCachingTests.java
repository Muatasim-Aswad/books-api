package com.asim.books.domain.book.service;

import com.asim.books.common.mapper.entity.EntityMapper;
import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.model.entity.Author;
import com.asim.books.domain.author.model.mapper.AuthorMapper;
import com.asim.books.domain.book.gateway.AuthorGateway;
import com.asim.books.domain.book.model.dto.BookDto;
import com.asim.books.domain.book.model.entity.Book;
import com.asim.books.domain.book.repository.BookRepository;
import com.asim.books.test.util.fixtures.AuthorTestFixtures;
import com.asim.books.test.util.fixtures.BookTestFixtures;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Book Service Caching Tests")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class BookServiceImplCachingTests {
    private static final Long FIRST_BOOK_ID = 1L;
    private static final Long SECOND_BOOK_ID = 5L;
    private static final String CACHE_NAME = "books";

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    @SuppressWarnings("unchecked")
    private EntityMapper<Book, BookDto> bookMapper;

    @Autowired
    private AuthorGateway authorGateway;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EntityManager entityManager;

    private Book firstBook;
    private BookDto firstBookDto;
    private Book secondBook;
    private BookDto secondBookDto;
    private AuthorDto authorDto;
    private Author author;

    @BeforeEach
    void setUp() {
        // Clear cache to prevent test interference
        Objects.requireNonNull(cacheManager.getCache(CACHE_NAME)).clear();

        // Reset mocks to clear any previous interactions
        reset(bookRepository, bookMapper, authorGateway);

        // Arrange Books

        firstBook = BookTestFixtures.getOneEntity();
        secondBook = BookTestFixtures.getOneEntity();
        firstBookDto = BookTestFixtures.getOneDtoWithAllFields();
        secondBookDto = BookTestFixtures.getOneDtoWithAllFields();
        firstBook.setId(FIRST_BOOK_ID);
        secondBook.setId(SECOND_BOOK_ID);
        firstBookDto.setId(FIRST_BOOK_ID);
        secondBookDto.setId(SECOND_BOOK_ID);

        authorDto = AuthorTestFixtures.getOneDtoWithAllFields();
        author = AuthorTestFixtures.getOneEntity();
    }

    @Configuration
    @EnableCaching
    static class CacheTestConfig {
        @Bean
        public BookService bookService(BookRepository bookRepository,
                                       EntityMapper<Book, BookDto> bookMapper,
                                       AuthorGateway authorGateway,
                                       EntityManager entityManager,
                                       AuthorMapper authorMapper) {
            return new BookServiceImpl(bookRepository, bookMapper, authorGateway, entityManager, authorMapper);
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("books");
        }

        @Bean
        public BookRepository bookRepository() {
            return mock(BookRepository.class);
        }

        @Bean
        @SuppressWarnings("unchecked")
        public EntityMapper<Book, BookDto> bookMapper() {
            return mock(EntityMapper.class);
        }

        @Bean
        public AuthorGateway authorGateway() {
            return mock(AuthorGateway.class);
        }

        @Bean
        public EntityManager entityManager() {
            return mock(EntityManager.class);
        }

        @Bean
        public AuthorMapper authorMapper() {
            return mock(AuthorMapper.class);
        }
    }

    @Nested
    @DisplayName("Book Retrieval Cache Tests")
    class BookRetrievalCacheTests {

        @Test
        @DisplayName("should cache book when getting by id")
        void whenGetBook_thenResultShouldBeCached() {
            // Arrange
            when(bookRepository.findById(FIRST_BOOK_ID)).thenReturn(Optional.of(firstBook));
            when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);

            // Act
            BookDto result1 = bookService.getBook(FIRST_BOOK_ID);
            BookDto result2 = bookService.getBook(FIRST_BOOK_ID);

            // Assert
            verify(bookRepository, times(1)).findById(FIRST_BOOK_ID);
            verify(bookMapper, times(1)).toDto(any(Book.class));
            assertThat(result1).isEqualTo(result2);
        }

        @Test
        @DisplayName("should use different cache entries for different ids")
        void whenGetDifferentBooks_thenShouldUseDifferentCacheEntries() {
            // Arrange
            when(bookRepository.findById(FIRST_BOOK_ID)).thenReturn(Optional.of(firstBook));
            when(bookRepository.findById(SECOND_BOOK_ID)).thenReturn(Optional.of(secondBook));
            when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
            when(bookMapper.toDto(secondBook)).thenReturn(secondBookDto);

            // Act
            bookService.getBook(FIRST_BOOK_ID);
            bookService.getBook(SECOND_BOOK_ID);
            bookService.getBook(FIRST_BOOK_ID);
            bookService.getBook(SECOND_BOOK_ID);

            // Assert
            verify(bookRepository, times(1)).findById(FIRST_BOOK_ID);
            verify(bookRepository, times(1)).findById(SECOND_BOOK_ID);
        }
    }

    @Nested
    @DisplayName("Book Update Cache Tests")
    class BookUpdateCacheTests {

        @Test
        @DisplayName("should update cache when book is updated")
        void whenUpdateBook_thenCacheShouldBeUpdated() {
            // Arrange
            BookDto updatedDto = firstBookDto;
            updatedDto.setTitle("Updated Title");
            updatedDto.setIsbn("9876543210123");

            Book updatedBook = firstBook;
            updatedBook.setTitle("Updated Title");
            updatedBook.setIsbn("9876543210123");

            when(bookRepository.findById(FIRST_BOOK_ID)).thenReturn(Optional.of(firstBook));
            when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
            when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
            when(bookMapper.toDto(updatedBook)).thenReturn(updatedDto);
            when(authorGateway.findMatchingAuthor(authorDto)).thenReturn(authorDto);
            when(entityManager.getReference(eq(Author.class), anyLong())).thenReturn(new Author());

            // Act
            bookService.getBook(FIRST_BOOK_ID); // Cache the initial book
            bookService.updateBook(FIRST_BOOK_ID, updatedDto);
            BookDto resultAfterUpdate = bookService.getBook(FIRST_BOOK_ID);

            // Assert
            verify(bookRepository, times(2)).findById(FIRST_BOOK_ID);
            assertThat(resultAfterUpdate.getTitle()).isEqualTo("Updated Title");
            assertThat(resultAfterUpdate.getIsbn()).isEqualTo("9876543210123");
        }
    }

    @Nested
    @DisplayName("Book Delete Cache Tests")
    class BookDeleteCacheTests {

        @Test
        @DisplayName("should evict cache when book is deleted")
        void whenDeleteBook_thenCacheShouldBeEvicted() {
            // Arrange
            when(bookRepository.existsById(FIRST_BOOK_ID)).thenReturn(true);
            doNothing().when(bookRepository).deleteById(FIRST_BOOK_ID);
            when(bookRepository.findById(FIRST_BOOK_ID)).thenReturn(Optional.of(firstBook));
            when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);

            // Act
            bookService.getBook(FIRST_BOOK_ID);// Cache the book
            bookService.deleteBook(FIRST_BOOK_ID);// Should evict from cache
            bookService.getBook(FIRST_BOOK_ID); // Should hit the repository again

            // Assert
            verify(bookRepository, times(2)).findById(FIRST_BOOK_ID);
            verify(bookMapper, times(2)).toDto(any(Book.class));
        }
    }

    @Nested
    @DisplayName("Book Creation Cache Tests")
    class BookCreationCacheTests {

        @Test
        @DisplayName("should add to cache when creating new book")
        void whenAddBook_thenShouldAddToCache() {
            // Arrange
            BookDto newBookDto = BookTestFixtures.createDto(
                    "New Book",
                    "1234567890123",
                    authorDto
            );

            Book newBook = BookTestFixtures.createEntity(
                    "New Book",
                    "1234567890123",
                    author,
                    SECOND_BOOK_ID
            );

            BookDto savedBookDto = BookTestFixtures.getOneDtoWithAllFields();
            savedBookDto.setId(SECOND_BOOK_ID);

            when(authorGateway.findMatchingAuthor(authorDto)).thenReturn(authorDto);
            when(bookRepository.save(any(Book.class))).thenReturn(newBook);
            when(bookMapper.toDto(newBook)).thenReturn(savedBookDto);
            when(bookMapper.toEntity(newBookDto)).thenReturn(newBook);
            when(bookRepository.findById(SECOND_BOOK_ID)).thenReturn(Optional.of(newBook));
            when(entityManager.getReference(eq(Author.class), anyLong())).thenReturn(new Author());

            // Act
            BookDto addedBook = bookService.addBook(newBookDto);

            // This should use cache if addBook put the result in cache
            BookDto cachedBook = bookService.getBook(SECOND_BOOK_ID);

            // Assert
            verify(bookRepository, never()).findById(SECOND_BOOK_ID);
            assertThat(addedBook).isEqualTo(cachedBook);
        }
    }
}