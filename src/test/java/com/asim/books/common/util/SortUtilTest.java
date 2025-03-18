package com.asim.books.common.util;

import com.asim.books.common.exception.custom.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Sort Utility Tests")
class SortUtilTest {

    @Test
    @DisplayName("should create sort object with ascending direction when direction is ASC")
    void whenValidSortWithAscDirection_thenReturnCorrectSortObject() {
        // Arrange
        String[] sortParams = {"name", "asc"};

        // Act
        Sort result = SortUtil.createObject(sortParams);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.toList()).hasSize(1);
        assertThat(result.toList().getFirst().getProperty()).isEqualTo("name");
        assertThat(result.toList().getFirst().getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    @DisplayName("should create sort object with descending direction when direction is DESC")
    void whenValidSortWithDescDirection_thenReturnCorrectSortObject() {
        // Arrange
        String[] sortParams = {"name", "desc"};

        // Act
        Sort result = SortUtil.createObject(sortParams);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.toList()).hasSize(1);
        assertThat(result.toList().getFirst().getProperty()).isEqualTo("name");
        assertThat(result.toList().getFirst().getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    @DisplayName("should create sort object with multiple criteria when multiple field-direction pairs are provided")
    void whenMultipleSortCriteria_thenReturnSortObjectWithAllCriteria() {
        // Arrange
        String[] sortParams = {"name", "asc", "age", "desc"};

        // Act
        Sort result = SortUtil.createObject(sortParams);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.toList()).hasSize(2);
        assertThat(result.toList().get(0).getProperty()).isEqualTo("name");
        assertThat(result.toList().get(0).getDirection()).isEqualTo(Sort.Direction.ASC);
        assertThat(result.toList().get(1).getProperty()).isEqualTo("age");
        assertThat(result.toList().get(1).getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    @DisplayName("should default to ASC direction when direction is not recognized")
    void whenDirectionNotRecognized_thenDefaultToAsc() {
        // Arrange
        String[] sortParams = {"name", "invalid"};

        // Act
        Sort result = SortUtil.createObject(sortParams);

        // Assert
        assertThat(result.toList().get(0).getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    @DisplayName("should throw BadRequestException when odd number of parameters is provided")
    void whenOddNumberOfParameters_thenThrowBadRequestException() {
        // Arrange
        String[] sortParams = {"name", "asc", "age"};

        // Act & Assert
        assertThrows(BadRequestException.class, () -> SortUtil.createObject(sortParams));
    }

    @Test
    @DisplayName("should return unsorted when no sort parameters are provided")
    void whenNoSortParameters_thenReturnUnsorted() {
        // Arrange
        String[] sortParams = {};

        // Act
        Sort result = SortUtil.createObject(sortParams);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.isUnsorted()).isTrue();
    }

    @Test
    @DisplayName("should return unsorted when sort parameters are null")
    void whenNullSortParameters_thenReturnUnsorted() {
        // Act
        Sort result = SortUtil.createObject(null);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.isUnsorted()).isTrue();
    }

    @Test
    @DisplayName("should handle case insensitive direction values")
    void whenCaseInsensitiveDirection_thenHandleCorrectly() {
        // Arrange
        String[] sortParams = {"name", "DESC", "age", "ASC"};

        // Act
        Sort result = SortUtil.createObject(sortParams);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.toList()).hasSize(2);
        assertThat(result.toList().get(0).getDirection()).isEqualTo(Sort.Direction.DESC);
        assertThat(result.toList().get(1).getDirection()).isEqualTo(Sort.Direction.ASC);
    }
}