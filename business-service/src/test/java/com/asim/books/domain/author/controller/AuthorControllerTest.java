package com.asim.books.domain.author.controller;

import com.asim.books.common.util.SortUtils;
import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.service.AuthorService;
import com.asim.books.test.util.fixtures.AuthorTestFixtures;
import com.asim.books.test.util.fixtures.CommonTestFixtures;
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

    @Mock
    private SortUtils sortUtils;

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
        when(authorService.getAuthor(CommonTestFixtures.VALID_ID)).thenReturn(expectedAuthor);

        // Act
        AuthorDto result = authorController.getAuthor(CommonTestFixtures.VALID_ID);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAuthor.getId(), result.getId());
        assertEquals(expectedAuthor.getName(), result.getName());
        assertEquals(expectedAuthor.getAge(), result.getAge());
        verify(authorService).getAuthor(CommonTestFixtures.VALID_ID);
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
                .id(CommonTestFixtures.VALID_ID)
                .name(AuthorTestFixtures.UPDATED_NAME)
                .age(AuthorTestFixtures.UPDATED_AGE)
                .build();

        when(authorService.updateAuthor(eq(CommonTestFixtures.VALID_ID), any(AuthorDto.class))).thenReturn(updatedAuthor);

        // Act
        AuthorDto result = authorController.updateAuthor(CommonTestFixtures.VALID_ID, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(CommonTestFixtures.VALID_ID, result.getId());
        assertEquals(AuthorTestFixtures.UPDATED_NAME, result.getName());
        assertEquals(AuthorTestFixtures.UPDATED_AGE, result.getAge());
        verify(authorService).updateAuthor(eq(CommonTestFixtures.VALID_ID), any(AuthorDto.class));
    }

    @Test
    @DisplayName("should delete author when valid ID provided")
    void whenDeleteAuthor_thenCallService() {
        // Arrange
        doNothing().when(authorService).deleteAuthor(CommonTestFixtures.VALID_ID);

        // Act
        authorController.deleteAuthor(CommonTestFixtures.VALID_ID);

        // Assert
        verify(authorService).deleteAuthor(CommonTestFixtures.VALID_ID);
    }

    @Test
    @DisplayName("should retrieve all authors with pagination when requested")
    void whenGetAuthors_thenReturnPagedAuthors() {
        // Arrange
        String[] sortParams = {"name", "asc"};
        Sort mockSort = Sort.by(Sort.Direction.ASC, "name");
        Page<AuthorDto> authorPage = new PageImpl<>(authorDTOs);

        when(sortUtils.createObject(eq(sortParams), eq(AuthorDto.class))).thenReturn(mockSort);
        when(authorService.getAuthors(any(), eq(null))).thenReturn(authorPage);

        // Act
        Page<AuthorDto> result = authorController.getAuthors(0, 10, sortParams, null);

        // Assert
        assertNotNull(result);
        assertEquals(authorDTOs.size(), result.getContent().size());
        verify(sortUtils).createObject(eq(sortParams), eq(AuthorDto.class));
        verify(authorService).getAuthors(any(), eq(null));
    }

    @Test
    @DisplayName("should filter authors by name when name parameter provided")
    void whenGetAuthorsWithNameFilter_thenReturnFilteredAuthors() {
        // Arrange
        String[] sortParams = {"name", "asc"};
        Sort mockSort = Sort.by(Sort.Direction.ASC, "name");
        String nameFilter = "Name";
        List<AuthorDto> filteredAuthors = authorDTOs.stream()
                .filter(a -> a.getName().contains(nameFilter))
                .toList();
        Page<AuthorDto> filteredPage = new PageImpl<>(filteredAuthors);

        when(sortUtils.createObject(eq(sortParams), eq(AuthorDto.class))).thenReturn(mockSort);
        when(authorService.getAuthors(any(), eq(nameFilter))).thenReturn(filteredPage);

        // Act
        Page<AuthorDto> result = authorController.getAuthors(0, 10, sortParams, nameFilter);

        // Assert
        assertNotNull(result);
        assertEquals(filteredAuthors.size(), result.getContent().size());
        verify(sortUtils).createObject(eq(sortParams), eq(AuthorDto.class));
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
        Sort unsortedSort = Sort.unsorted();
        Page<AuthorDto> authorPage = new PageImpl<>(authorDTOs);

        when(sortUtils.createObject(eq(null), eq(AuthorDto.class))).thenReturn(unsortedSort);
        when(authorService.getAuthors(any(), eq(null))).thenReturn(authorPage);

        // Act
        authorController.getAuthors(0, 10, null, null);

        // Assert
        verify(sortUtils).createObject(eq(null), eq(AuthorDto.class));
        verify(authorService).getAuthors(argThat(pageable ->
                pageable.getSort().isUnsorted()), eq(null));
    }
}