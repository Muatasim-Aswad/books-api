package com.asim.books.common.util;

import com.asim.books.common.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DisplayName("Sort Utility Tests")
@ExtendWith(MockitoExtension.class)
class SortUtilsTest {

    @Mock
    private ReflectionUtils reflectionUtils;

    @InjectMocks
    private SortUtils sortUtils;

    @BeforeEach
    void setUp() {
        // Test class for field validation
        Set<String> validFields = new HashSet<>();
        validFields.add("name");
        validFields.add("age");
        validFields.add("id");

        when(reflectionUtils.getFieldNames(TestClass.class)).thenReturn(validFields);
    }

    static class TestClass {
        private Long id;
        private String name;
        private Integer age;
    }

    @Nested
    @DisplayName("Basic Sorting Tests")
    class BasicSortingTests {
        @Test
        @DisplayName("should create sort object with ascending direction when direction is ASC")
        void whenValidSortWithAscDirection_thenReturnCorrectSortObject() {
            // Arrange
            String[] sortParams = {"name", "asc"};

            // Act
            Sort result = sortUtils.createObject(sortParams, TestClass.class);

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
            Sort result = sortUtils.createObject(sortParams, TestClass.class);

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
            Sort result = sortUtils.createObject(sortParams, TestClass.class);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.toList()).hasSize(2);
            assertThat(result.toList().get(0).getProperty()).isEqualTo("name");
            assertThat(result.toList().get(0).getDirection()).isEqualTo(Sort.Direction.ASC);
            assertThat(result.toList().get(1).getProperty()).isEqualTo("age");
            assertThat(result.toList().get(1).getDirection()).isEqualTo(Sort.Direction.DESC);
        }

        @Test
        @DisplayName("should throw BadRequestException when direction is not recognized")
        void whenDirectionNotRecognized_thenThrowBadRequestException() {
            // Arrange
            String[] sortParams = {"name", "invalid"};

            // Act & Assert
            assertThrows(BadRequestException.class, () -> sortUtils.createObject(sortParams, TestClass.class));
        }

        @Test
        @DisplayName("should throw BadRequestException when odd number of parameters is provided")
        void whenOddNumberOfParameters_thenThrowBadRequestException() {
            Mockito.reset(reflectionUtils);

            // Arrange
            String[] sortParams = {"name", "asc", "age"};

            // Act & Assert
            assertThrows(BadRequestException.class, () -> sortUtils.createObject(sortParams, TestClass.class));
        }

        @Test
        @DisplayName("should return unsorted when no sort parameters are provided")
        void whenNoSortParameters_thenReturnUnsorted() {
            Mockito.reset(reflectionUtils);

            // Arrange
            String[] sortParams = {};

            // Act
            Sort result = sortUtils.createObject(sortParams, TestClass.class);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.isUnsorted()).isTrue();
        }

        @Test
        @DisplayName("should return unsorted when sort parameters are null")
        void whenNullSortParameters_thenReturnUnsorted() {
            Mockito.reset(reflectionUtils);

            // Act
            Sort result = sortUtils.createObject(null, TestClass.class);

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
            Sort result = sortUtils.createObject(sortParams, TestClass.class);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.toList()).hasSize(2);
            assertThat(result.toList().get(0).getDirection()).isEqualTo(Sort.Direction.DESC);
            assertThat(result.toList().get(1).getDirection()).isEqualTo(Sort.Direction.ASC);
        }
    }

    @Nested
    @DisplayName("Field Validation Tests")
    class FieldValidationTests {

        @Test
        @DisplayName("should not throw exception when sorting by valid fields")
        void whenSortingByValidFields_thenNoException() {
            // Arrange
            String[] sortParams = {"name", "asc", "age", "desc"};

            // Act
            Sort result = sortUtils.createObject(sortParams, TestClass.class);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.toList()).hasSize(2);
            assertThat(result.toList().get(0).getProperty()).isEqualTo("name");
            assertThat(result.toList().get(1).getProperty()).isEqualTo("age");
        }

        @Test
        @DisplayName("should throw BadRequestException when sorting by invalid field")
        void whenSortingByInvalidField_thenThrowBadRequestException() {
            // Arrange
            String[] sortParams = {"invalidField", "asc"};

            // Act & Assert
            assertThrows(BadRequestException.class, () ->
                    sortUtils.createObject(sortParams, TestClass.class));
        }

        @Test
        @DisplayName("should throw BadRequestException when one field is invalid in multiple fields")
        void whenOneFieldInvalidInMultipleFields_thenThrowBadRequestException() {
            // Arrange
            String[] sortParams = {"name", "asc", "invalidField", "desc"};

            // Act & Assert
            assertThrows(BadRequestException.class, () ->
                    sortUtils.createObject(sortParams, TestClass.class));
        }
    }

    @Nested
    @DisplayName("Format Handling Tests")
    class FormatHandlingTests {

        @Test
        @DisplayName("should handle comma-separated format correctly")
        void whenCommaSeparatedFormat_thenParseCorrectly() {
            // Arrange
            String[] sortParams = {"name,asc", "age,desc"};

            // Act
            Sort result = sortUtils.createObject(sortParams, TestClass.class);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.toList()).hasSize(2);
            assertThat(result.toList().get(0).getProperty()).isEqualTo("name");
            assertThat(result.toList().get(0).getDirection()).isEqualTo(Sort.Direction.ASC);
            assertThat(result.toList().get(1).getProperty()).isEqualTo("age");
            assertThat(result.toList().get(1).getDirection()).isEqualTo(Sort.Direction.DESC);
        }
    }
}