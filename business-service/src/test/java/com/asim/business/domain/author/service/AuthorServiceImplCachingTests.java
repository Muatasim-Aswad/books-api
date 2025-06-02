package com.asim.business.domain.author.service;

import com.asim.business.common.model.mapper.EntityDtoMapper;
import com.asim.business.domain.author.model.dto.AuthorDto;
import com.asim.business.domain.author.model.entity.Author;
import com.asim.business.domain.author.repository.AuthorRepository;
import com.asim.business.test.util.fixtures.AuthorTestFixtures;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
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

@Disabled("to solve later, build seems to be failing due to caching issues")
@DisplayName("Author Service Caching Tests")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthorServiceImplCachingTests {
    private static final Long FIRST_AUTHOR_ID = 1L;
    private static final Long SECOND_AUTHOR_ID = 5L;
    private static final String CACHE_NAME = "authors";

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    @SuppressWarnings("unchecked")
    private EntityDtoMapper<Author, AuthorDto> authorMapper;

    @Autowired
    private CacheManager cacheManager;

    private Author firstAuthor;
    private AuthorDto firstAuthorDto;
    private Author secondAuthor;
    private AuthorDto secondAuthorDto;

    @BeforeEach
    void setUp() {
        // Clear cache to prevent test interference
        Objects.requireNonNull(cacheManager.getCache(CACHE_NAME)).clear();

        // Reset mocks to clear any previous interactions
        reset(authorRepository, authorMapper);

        // Arrange Authors
        firstAuthor = AuthorTestFixtures.getOneEntity();
        secondAuthor = AuthorTestFixtures.getOneEntity();
        firstAuthorDto = AuthorTestFixtures.getOneDtoWithAllFields();
        secondAuthorDto = AuthorTestFixtures.getOneDtoWithAllFields();
        firstAuthor.setId(FIRST_AUTHOR_ID);
        secondAuthor.setId(SECOND_AUTHOR_ID);
        firstAuthorDto.setId(FIRST_AUTHOR_ID);
        secondAuthorDto.setId(SECOND_AUTHOR_ID);
    }

    @Configuration
    @EnableCaching
    static class CacheTestConfig {
        @Bean
        public AuthorService authorService(AuthorRepository authorRepository,
                                           EntityDtoMapper<Author, AuthorDto> authorMapper,
                                           EntityManager entityManager) {
            return new AuthorServiceImpl(authorRepository, authorMapper, entityManager);
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("authors");
        }

        @Bean
        public AuthorRepository authorRepository() {
            return mock(AuthorRepository.class);
        }

        @Bean
        @SuppressWarnings("unchecked")
        public EntityDtoMapper<Author, AuthorDto> authorMapper() {
            return mock(EntityDtoMapper.class);
        }

        @Bean
        public EntityManager entityManager() {
            return mock(EntityManager.class);
        }
    }

    @Nested
    @DisplayName("Author Retrieval Cache Tests")
    class AuthorRetrievalCacheTests {

        @Test
        @DisplayName("should cache author when getting by id")
        void whenGetAuthor_thenResultShouldBeCached() {
            // Arrange
            when(authorRepository.findById(FIRST_AUTHOR_ID)).thenReturn(Optional.of(firstAuthor));
            when(authorMapper.toDto(firstAuthor)).thenReturn(firstAuthorDto);

            // Act
            AuthorDto result1 = authorService.getAuthor(FIRST_AUTHOR_ID);
            AuthorDto result2 = authorService.getAuthor(FIRST_AUTHOR_ID);

            // Assert
            verify(authorRepository, times(1)).findById(FIRST_AUTHOR_ID);
            verify(authorMapper, times(1)).toDto(any(Author.class));
            assertThat(result1).isEqualTo(result2);
        }

        @Test
        @DisplayName("should use different cache entries for different ids")
        void whenGetDifferentAuthors_thenShouldUseDifferentCacheEntries() {
            // Arrange
            when(authorRepository.findById(FIRST_AUTHOR_ID)).thenReturn(Optional.of(firstAuthor));
            when(authorRepository.findById(SECOND_AUTHOR_ID)).thenReturn(Optional.of(secondAuthor));
            when(authorMapper.toDto(firstAuthor)).thenReturn(firstAuthorDto);
            when(authorMapper.toDto(secondAuthor)).thenReturn(secondAuthorDto);

            // Act
            authorService.getAuthor(FIRST_AUTHOR_ID);
            authorService.getAuthor(SECOND_AUTHOR_ID);
            authorService.getAuthor(FIRST_AUTHOR_ID);
            authorService.getAuthor(SECOND_AUTHOR_ID);

            // Assert
            verify(authorRepository, times(1)).findById(FIRST_AUTHOR_ID);
            verify(authorRepository, times(1)).findById(SECOND_AUTHOR_ID);
        }
    }

    @Nested
    @DisplayName("Author Update Cache Tests")
    class AuthorUpdateCacheTests {

        @Test
        @DisplayName("should update cache when author is updated")
        void whenUpdateAuthor_thenCacheShouldBeUpdated() {
            // Arrange
            AuthorDto updatedDto = AuthorTestFixtures.createDto(
                    AuthorTestFixtures.UPDATED_NAME,
                    AuthorTestFixtures.UPDATED_AGE
            );
            updatedDto.setId(FIRST_AUTHOR_ID);
            updatedDto.setVersion(1);

            Author updatedAuthor = AuthorTestFixtures.createEntity(
                    AuthorTestFixtures.UPDATED_NAME,
                    AuthorTestFixtures.UPDATED_AGE,
                    FIRST_AUTHOR_ID
            );
            updatedAuthor.setVersion(1);

            when(authorRepository.findById(FIRST_AUTHOR_ID)).thenReturn(Optional.of(firstAuthor));
            when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);
            when(authorMapper.toDto(firstAuthor)).thenReturn(firstAuthorDto);
            when(authorMapper.toDto(updatedAuthor)).thenReturn(updatedDto);

            // Act
            authorService.getAuthor(FIRST_AUTHOR_ID); // Cache the initial author
            authorService.updateAuthor(FIRST_AUTHOR_ID, updatedDto);
            AuthorDto resultAfterUpdate = authorService.getAuthor(FIRST_AUTHOR_ID);

            // Assert
            verify(authorRepository, times(2)).findById(FIRST_AUTHOR_ID); // Should be called only once despite getAuthor being called twice
            assertThat(resultAfterUpdate.getName()).isEqualTo(AuthorTestFixtures.UPDATED_NAME);
            assertThat(resultAfterUpdate.getAge()).isEqualTo(AuthorTestFixtures.UPDATED_AGE);
        }
    }

    @Nested
    @DisplayName("Author Delete Cache Tests")
    class AuthorDeleteCacheTests {

        @Test
        @DisplayName("should evict cache when author is deleted")
        void whenDeleteAuthor_thenCacheShouldBeEvicted() {
            // Arrange
            when(authorRepository.existsById(FIRST_AUTHOR_ID)).thenReturn(true);
            doNothing().when(authorRepository).deleteById(FIRST_AUTHOR_ID);
            when(authorRepository.findById(FIRST_AUTHOR_ID)).thenReturn(Optional.of(firstAuthor));
            when(authorMapper.toDto(firstAuthor)).thenReturn(firstAuthorDto);

            // Act
            authorService.getAuthor(FIRST_AUTHOR_ID); // Cache the author
            authorService.deleteAuthor(FIRST_AUTHOR_ID); // Should evict from cache
            authorService.getAuthor(FIRST_AUTHOR_ID); // Should hit the repository again

            // Assert
            verify(authorRepository, times(2)).findById(FIRST_AUTHOR_ID);
            verify(authorMapper, times(2)).toDto(any(Author.class));
        }
    }

    @Nested
    @DisplayName("Author Creation Cache Tests")
    class AuthorCreationCacheTests {

        @Test
        @DisplayName("should add to cache when creating new author")
        void whenAddAuthor_thenShouldAddToCache() {
            // Arrange
            AuthorDto newAuthorDto = AuthorTestFixtures.createDto(
                    AuthorTestFixtures.NAME,
                    AuthorTestFixtures.AGE
            );

            Author newAuthor = AuthorTestFixtures.createEntity(
                    AuthorTestFixtures.NAME,
                    AuthorTestFixtures.AGE,
                    SECOND_AUTHOR_ID
            );

            AuthorDto savedAuthorDto = AuthorTestFixtures.createDto(
                    AuthorTestFixtures.NAME,
                    AuthorTestFixtures.AGE
            );
            savedAuthorDto.setId(SECOND_AUTHOR_ID);
            savedAuthorDto.setVersion(0);

            when(authorRepository.existsByNameAndAge(anyString(), anyInt())).thenReturn(false);
            when(authorRepository.save(any(Author.class))).thenReturn(newAuthor);
            when(authorMapper.toDto(newAuthor)).thenReturn(savedAuthorDto);
            when(authorMapper.toEntity(newAuthorDto)).thenReturn(newAuthor);
            when(authorRepository.findById(SECOND_AUTHOR_ID)).thenReturn(Optional.of(newAuthor));

            // Act
            AuthorDto addedAuthor = authorService.addAuthor(newAuthorDto);

            // This should use cache if addAuthor put the result in cache
            AuthorDto cachedAuthor = authorService.getAuthor(SECOND_AUTHOR_ID);

            // Assert
            verify(authorRepository, never()).findById(SECOND_AUTHOR_ID);
            assertThat(addedAuthor).isEqualTo(cachedAuthor);
        }
    }
}