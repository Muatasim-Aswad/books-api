package com.asim.business.domain.author.repository;

import com.asim.business.domain.author.model.entity.Author;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Author Repository Tests")
class AuthorRepositoryTest {
    private Author authorToSave;
    private Author savedAuthor;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        authorToSave = Author.builder()
                .name("Test Author")
                .age(30)
                .createdBy(1L)
                .build();

        savedAuthor = authorRepository.save(authorToSave);
        entityManager.flush();
    }

    @AfterEach
    void tearDown() {
        authorRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Should save author to database")
    void whenSavingAuthor_thenAuthorIsSaved() {
        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.getName()).isNotNull();
        assertThat(savedAuthor.getAge()).isNotNull();
        assertThat(savedAuthor.getCreatedBy()).isNotNull();
        assertThat(savedAuthor.getName()).isEqualTo(authorToSave.getName());
        assertThat(savedAuthor.getAge()).isEqualTo(authorToSave.getAge());
        assertThat(savedAuthor.getCreatedBy()).isEqualTo(authorToSave.getCreatedBy());
    }

    @Test
    @DisplayName("Should generate ID when persisting entity")
    void whenSavingAuthor_thenIdIsGenerated() {
        assertThat(savedAuthor.getId()).isNotNull();
    }

    @Test
    @DisplayName("Should auto-generate timestamps when persisting entity")
    void whenSavingAuthor_thenTimestampsAreGenerated() {
        assertThat(savedAuthor.getCreatedAt()).isNotNull();
        assertThat(savedAuthor.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should initialize version number when persisting entity")
    void whenSavingAuthor_thenVersionIsInitialized() {
        assertThat(savedAuthor.getVersion()).isNotNull();
        assertThat(savedAuthor.getVersion()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should increment version number when updating entity")
    void whenUpdatingAuthor_thenVersionIsIncremented() {
        // Arrange
        savedAuthor.setName("Updated Author");
        savedAuthor.setLastModifiedBy(2L);

        // Act
        authorRepository.save(savedAuthor);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Author updatedAuthor = authorRepository.findById(savedAuthor.getId()).orElseThrow();
        assertThat(updatedAuthor.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should only update updatedAt timestamp when updating entity")
    void whenUpdatingAuthor_thenUpdatedAtTimestampIsUpdated() {
        // Arrange
        // Wait to ensure timestamp would be different
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // Ignore
        }
        ZonedDateTime createdAt = savedAuthor.getCreatedAt();
        ZonedDateTime initialUpdatedAt = savedAuthor.getUpdatedAt();
        savedAuthor.setName("Updated Author");

        // Act
        authorRepository.save(savedAuthor);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Author updatedAuthor = authorRepository.findById(savedAuthor.getId()).orElseThrow();
        assertThat(updatedAuthor.getUpdatedAt()).isAfter(initialUpdatedAt);
        assertThat(updatedAuthor.getCreatedAt()).isEqualTo(createdAt);
    }
}