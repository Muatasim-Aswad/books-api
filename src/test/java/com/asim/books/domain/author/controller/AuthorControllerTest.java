package com.asim.books.domain.author.controller;

import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.service.AuthorService;
import com.asim.books.test.util.fixtures.AuthorTestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Author Controller Tests")
class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private AuthorDto authorDto;
    private List<AuthorDto> authorDTOs;

    @BeforeEach
    void setUp() {
        // Arrange
        authorDto = AuthorTestFixtures.getOneDto();
        authorDTOs = Arrays.asList(AuthorTestFixtures.getManyDTOs());
    }

    @Test
    @DisplayName("should add author when valid author provided")
    void whenAddAuthor_thenReturnCreatedAuthor() {
        // Arrange
        AuthorDto expectedAuthor = AuthorTestFixtures.getOneDtoWithId();
        when(authorService.addAuthor(any(AuthorDto.class))).thenReturn(expectedAuthor);

        // Act
        AuthorDto result = authorController.addAuthor(authorDto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAuthor.getId(), result.getId());
        assertEquals(expectedAuthor.getName(), result.getName());
        assertEquals(expectedAuthor.getAge(), result.getAge());
        verify(authorService).addAuthor(any(AuthorDto.class));
    }

    @Test
    @DisplayName("should retrieve author when valid ID provided")
    void whenGetAuthor_thenReturnAuthor() {
        // Arrange
        AuthorDto expectedAuthor = AuthorTestFixtures.getOneDtoWithId();
        when(authorService.getAuthor(AuthorTestFixtures.ID)).thenReturn(expectedAuthor);

        // Act
        AuthorDto result = authorController.getAuthor(AuthorTestFixtures.ID);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAuthor.getId(), result.getId());
        assertEquals(expectedAuthor.getName(), result.getName());
        assertEquals(expectedAuthor.getAge(), result.getAge());
        verify(authorService).getAuthor(AuthorTestFixtures.ID);
    }

    @Test
    @DisplayName("should update author when valid data provided")
    void whenUpdateAuthor_thenReturnUpdatedAuthor() {
        // Arrange
        AuthorDto updateDto = AuthorDto.builder()
                .name(AuthorTestFixtures.UPDATED_NAME)
                .age(AuthorTestFixtures.UPDATED_AGE)
                .build();

        AuthorDto updatedAuthor = AuthorDto.builder()
                .id(AuthorTestFixtures.ID)
                .name(AuthorTestFixtures.UPDATED_NAME)
                .age(AuthorTestFixtures.UPDATED_AGE)
                .build();

        when(authorService.updateAuthor(eq(AuthorTestFixtures.ID), any(AuthorDto.class))).thenReturn(updatedAuthor);

        // Act
        AuthorDto result = authorController.updateAuthor(AuthorTestFixtures.ID, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(AuthorTestFixtures.ID, result.getId());
        assertEquals(AuthorTestFixtures.UPDATED_NAME, result.getName());
        assertEquals(AuthorTestFixtures.UPDATED_AGE, result.getAge());
        verify(authorService).updateAuthor(eq(AuthorTestFixtures.ID), any(AuthorDto.class));
    }

    @Test
    @DisplayName("should delete author when valid ID provided")
    void whenDeleteAuthor_thenCallService() {
        // Arrange
        doNothing().when(authorService).deleteAuthor(AuthorTestFixtures.ID);

        // Act
        authorController.deleteAuthor(AuthorTestFixtures.ID);

        // Assert
        verify(authorService).deleteAuthor(AuthorTestFixtures.ID);
    }

    @Test
    @DisplayName("should retrieve all authors with pagination when requested")
    void whenGetAuthors_thenReturnPagedAuthors() {
        // Arrange
        Page<AuthorDto> authorPage = new PageImpl<>(authorDTOs);
        when(authorService.getAuthors(any(), eq(null))).thenReturn(authorPage);

        // Act
        Page<AuthorDto> result = authorController.getAuthors(0, 10, new String[]{"name", "asc"}, null);

        // Assert
        assertNotNull(result);
        assertEquals(authorDTOs.size(), result.getContent().size());
        verify(authorService).getAuthors(any(), eq(null));
    }

    @Test
    @DisplayName("should filter authors by name when name parameter provided")
    void whenGetAuthorsWithNameFilter_thenReturnFilteredAuthors() {
        // Arrange
        String nameFilter = "Name";
        List<AuthorDto> filteredAuthors = authorDTOs.stream()
                .filter(a -> a.getName().contains(nameFilter))
                .toList();
        Page<AuthorDto> filteredPage = new PageImpl<>(filteredAuthors);

        when(authorService.getAuthors(any(), eq(nameFilter))).thenReturn(filteredPage);

        // Act
        Page<AuthorDto> result = authorController.getAuthors(0, 10, new String[]{"name", "asc"}, nameFilter);

        // Assert
        assertNotNull(result);
        assertEquals(filteredAuthors.size(), result.getContent().size());
        verify(authorService).getAuthors(any(), eq(nameFilter));
    }

    @Test
    @Disabled("Default sorting removed - may be reintroduced later")
    @DisplayName("should use default sort when no sort parameter provided")
    void whenGetAuthorsWithNoSort_thenUseDefaultSort() {
        // Arrange
        Page<AuthorDto> authorPage = new PageImpl<>(authorDTOs);
        when(authorService.getAuthors(any(), eq(null))).thenReturn(authorPage);

        // Act
        authorController.getAuthors(0, 10, null, null);

        // Assert
        verify(authorService).getAuthors(argThat(pageable ->
                pageable.getSort().equals(Sort.by(Sort.Order.asc("name")))), eq(null));
    }

    @Test
    @DisplayName("should use unsorted when no sort parameter provided")
    void whenGetAuthorsWithNoSort_thenUseUnsorted() {
        // Arrange
        Page<AuthorDto> authorPage = new PageImpl<>(authorDTOs);
        when(authorService.getAuthors(any(), eq(null))).thenReturn(authorPage);

        // Act
        authorController.getAuthors(0, 10, null, null);

        // Assert
        verify(authorService).getAuthors(argThat(pageable ->
                pageable.getSort().isUnsorted()), eq(null));
    }
}