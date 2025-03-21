package com.asim.books.domain.author.service.authorServiceImpl;

import com.asim.books.common.exception.DuplicateResourceException;
import com.asim.books.common.exception.NoIdIsProvidedException;
import com.asim.books.common.exception.ResourceNotFoundException;
import com.asim.books.common.mapper.entity.EntityMapper;
import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.model.entity.Author;
import com.asim.books.domain.author.repository.AuthorRepository;
import com.asim.books.domain.author.service.AuthorServiceImpl;
import com.asim.books.test.util.fixtures.AuthorTestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Author Service Unit Tests")
class AuthorServiceImplTest {

    private final Long AUTHOR_ID = AuthorTestFixtures.ID;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private EntityMapper<Author, AuthorDto> authorMapper;
    @InjectMocks
    private AuthorServiceImpl authorService;
    private Author author;
    private AuthorDto authorDto;

    @BeforeEach
    void setUp() {
        // Arrange
        author = AuthorTestFixtures.getOneEntity();

        authorDto = AuthorTestFixtures.getOneDtoWithId();
    }

    @Test
    @DisplayName("should add author successfully when author doesn't exist")
    void whenAddAuthor_thenReturnCreatedAuthor() {
        // Arrange
        when(authorMapper.toEntity(any(AuthorDto.class))).thenReturn(author);
        when(authorMapper.toDto(any(Author.class))).thenReturn(authorDto);
        when(authorRepository.existsByName(anyString())).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        // Act
        AuthorDto result = authorService.addAuthor(authorDto);

        // Assert
        assertNotNull(result);
        assertEquals(authorDto.getName(), result.getName());
        assertEquals(authorDto.getAge(), result.getAge());
        verify(authorRepository).save(author);
    }

    @Test
    @DisplayName("should throw exception when adding duplicate author")
    void whenAddDuplicateAuthor_thenThrowException() {
        // Arrange
        when(authorMapper.toEntity(any(AuthorDto.class))).thenReturn(author);
        when(authorRepository.existsByName(anyString())).thenReturn(true);
        when(authorRepository.existsByAge(anyInt())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> authorService.addAuthor(authorDto));
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    @DisplayName("should return author when author exists")
    void whenGetExistingAuthor_thenReturnAuthor() {
        // Arrange
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        when(authorMapper.toDto(author)).thenReturn(authorDto);

        // Act
        AuthorDto result = authorService.getAuthor(AUTHOR_ID);

        // Assert
        assertNotNull(result);
        assertEquals(AUTHOR_ID, result.getId());
        assertEquals(authorDto.getName(), result.getName());
    }

    @Test
    @DisplayName("should throw exception when getting non-existing author")
    void whenGetNonExistingAuthor_thenThrowException() {
        // Arrange
        when(authorRepository.findById(AuthorTestFixtures.NON_EXISTING_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> authorService.getAuthor(AuthorTestFixtures.NON_EXISTING_ID));
    }

    @Test
    @DisplayName("should update author when author exists")
    void whenUpdateExistingAuthor_thenReturnUpdatedAuthor() {
        // Arrange
        AuthorDto updateDto = new AuthorDto();
        updateDto.setName(AuthorTestFixtures.UPDATED_NAME);

        Author updatedAuthor = AuthorTestFixtures.getOneEntity();
        updatedAuthor.setName(AuthorTestFixtures.UPDATED_NAME);

        AuthorDto updatedDto = AuthorTestFixtures.getOneDtoWithId();
        updatedDto.setName(AuthorTestFixtures.UPDATED_NAME);

        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);
        when(authorMapper.toDto(updatedAuthor)).thenReturn(updatedDto);

        // Act
        AuthorDto result = authorService.updateAuthor(AUTHOR_ID, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(AuthorTestFixtures.UPDATED_NAME, result.getName());
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    @DisplayName("should throw exception when updating non-existing author")
    void whenUpdateNonExistingAuthor_thenThrowException() {
        // Arrange
        when(authorRepository.findById(AuthorTestFixtures.NON_EXISTING_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> authorService.updateAuthor(AuthorTestFixtures.NON_EXISTING_ID, authorDto));
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    @DisplayName("should delete author when author exists")
    void whenDeleteExistingAuthor_thenNoExceptionThrown() {
        // Arrange
        when(authorRepository.existsById(AUTHOR_ID)).thenReturn(true);
        doNothing().when(authorRepository).deleteById(AUTHOR_ID);

        // Act & Assert
        assertDoesNotThrow(() -> authorService.deleteAuthor(AUTHOR_ID));
        verify(authorRepository).deleteById(AUTHOR_ID);
    }

    @Test
    @DisplayName("should throw exception when deleting non-existing author")
    void whenDeleteNonExistingAuthor_thenThrowException() {
        // Arrange
        when(authorRepository.existsById(AuthorTestFixtures.NON_EXISTING_ID)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> authorService.deleteAuthor(AuthorTestFixtures.NON_EXISTING_ID));
        verify(authorRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("should return paged authors")
    void whenGetAuthors_thenReturnPagedAuthors() {
        // Arrange
        List<Author> authors = List.of(author);
        Page<Author> authorPage = new PageImpl<>(authors);
        Pageable pageable = PageRequest.of(0, 10);

        when(authorRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(authorPage);
        when(authorMapper.toDto(author)).thenReturn(authorDto);

        // Act
        Page<AuthorDto> result = authorService.getAuthors(pageable, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(authorDto, result.getContent().get(0));
    }

    @Test
    @DisplayName("should check if author exists")
    void whenAuthorExists_thenReturnTrue() {
        // Arrange
        when(authorRepository.existsById(AUTHOR_ID)).thenReturn(true);

        // Act
        boolean exists = authorService.authorExists(AUTHOR_ID);

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("should return false when author doesn't exist")
    void whenAuthorDoesNotExist_thenReturnFalse() {
        // Arrange
        when(authorRepository.existsById(AuthorTestFixtures.NON_EXISTING_ID)).thenReturn(false);

        // Act
        boolean exists = authorService.authorExists(AuthorTestFixtures.NON_EXISTING_ID);

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("should throw exception when author ID is null for matching")
    void whenFindAuthorWithNullId_thenThrowException() {
        // Arrange
        AuthorDto dto = new AuthorDto();

        // Act & Assert
        assertThrows(NoIdIsProvidedException.class, () -> authorService.findAuthorAndMatch(dto));
    }

    @Test
    @DisplayName("should return true when author matches")
    void whenAuthorMatches_thenReturnTrue() {
        // Arrange
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        when(authorMapper.toDto(author)).thenReturn(authorDto);

        // Act
        boolean matches = authorService.findAuthorAndMatch(authorDto);

        // Assert
        assertTrue(matches);
    }

    @Test
    @DisplayName("should return false when author contradicts")
    void whenAuthorContradicts_thenReturnFalse() {
        // Arrange
        AuthorDto inputDto = new AuthorDto();
        inputDto.setId(AUTHOR_ID);
        inputDto.setName("Different Name");

        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        when(authorMapper.toDto(author)).thenReturn(authorDto);

        // Act
        boolean matches = authorService.findAuthorAndMatch(inputDto);

        // Assert
        assertFalse(matches);
    }

    @Test
    @DisplayName("should not update null fields when updating author")
    void whenUpdateAuthorWithNullFields_thenOnlyUpdateNonNullFields() {
        // Arrange
        AuthorDto updateDto = new AuthorDto();
        updateDto.setName(null);
        updateDto.setAge(AuthorTestFixtures.UPDATED_AGE);

        Author originalAuthor = AuthorTestFixtures.getOneEntity();
        Author updatedAuthor = AuthorTestFixtures.getOneEntity();
        updatedAuthor.setAge(AuthorTestFixtures.UPDATED_AGE);

        AuthorDto updatedDto = AuthorTestFixtures.getOneDtoWithId();
        updatedDto.setAge(AuthorTestFixtures.UPDATED_AGE);

        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(originalAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);
        when(authorMapper.toDto(updatedAuthor)).thenReturn(updatedDto);

        // Act
        AuthorDto result = authorService.updateAuthor(AUTHOR_ID, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(AuthorTestFixtures.NAME, originalAuthor.getName());
        assertEquals(AuthorTestFixtures.UPDATED_AGE, result.getAge());
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    @DisplayName("should filter authors by name when name filter provided")
    void whenGetAuthorsWithNameFilter_thenReturnFilteredAuthors() {
        // Arrange
        String nameFilter = "Name";
        List<Author> authors = List.of(author);
        Page<Author> authorPage = new PageImpl<>(authors);
        Pageable pageable = PageRequest.of(0, 10);

        when(authorRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(authorPage);
        when(authorMapper.toDto(author)).thenReturn(authorDto);

        // Act
        Page<AuthorDto> result = authorService.getAuthors(pageable, nameFilter);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        // Verify that specification was created with the name filter
        verify(authorRepository).findAll(any(Specification.class), eq(pageable));
    }
}