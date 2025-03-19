package com.asim.books.common.util;

import com.asim.books.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Sort Utility Tests")
class SortUtilsTest {

    @Nested
    @DisplayName("Basic Sorting Tests")
    class BasicSortingTests {
        @Test
        @DisplayName("should create sort object with ascending direction when direction is ASC")
        void whenValidSortWithAscDirection_thenReturnCorrectSortObject() {
            // Arrange
            String[] sortParams = {"name", "asc"};

            // Act
            Sort result = SortUtils.createObject(sortParams, null);

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
            Sort result = SortUtils.createObject(sortParams, null);

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
            Sort result = SortUtils.createObject(sortParams, null);

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
            assertThrows(BadRequestException.class, () -> SortUtils.createObject(sortParams, null));
        }

        @Test
        @DisplayName("should throw BadRequestException when odd number of parameters is provided")
        void whenOddNumberOfParameters_thenThrowBadRequestException() {
            // Arrange
            String[] sortParams = {"name", "asc", "age"};

            // Act & Assert
            assertThrows(BadRequestException.class, () -> SortUtils.createObject(sortParams, null));
        }

        @Test
        @DisplayName("should return unsorted when no sort parameters are provided")
        void whenNoSortParameters_thenReturnUnsorted() {
            // Arrange
            String[] sortParams = {};

            // Act
            Sort result = SortUtils.createObject(sortParams, null);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.isUnsorted()).isTrue();
        }

        @Test
        @DisplayName("should return unsorted when sort parameters are null")
        void whenNullSortParameters_thenReturnUnsorted() {
            // Act
            Sort result = SortUtils.createObject(null, null);

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
            Sort result = SortUtils.createObject(sortParams, null);

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
            Sort result = SortUtils.createObject(sortParams, TestClass.class);

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
                    SortUtils.createObject(sortParams, TestClass.class));
        }

        @Test
        @DisplayName("should throw BadRequestException when one field is invalid in multiple fields")
        void whenOneFieldInvalidInMultipleFields_thenThrowBadRequestException() {
            // Arrange
            String[] sortParams = {"name", "asc", "invalidField", "desc"};

            // Act & Assert
            assertThrows(BadRequestException.class, () ->
                    SortUtils.createObject(sortParams, TestClass.class));
        }

        // Test class for field validation
        static class TestClass {
            private Long id;
            private String name;
            private Integer age;
        }
    }

    @Nested
    @DisplayName("Field Name Extraction Tests")
    class FieldNameExtractionTests {

        @Test
        @DisplayName("should extract all field names from simple class")
        void whenSimpleClass_thenExtractAllFieldNames() {
            // Act
            var fieldNames = SortUtils.getFieldNames(SimpleClass.class);

            // Assert
            assertThat(fieldNames).isNotNull();
            assertThat(fieldNames).hasSize(2);
            assertThat(fieldNames).contains("name", "count");
        }

        @Test
        @DisplayName("should extract all field names from complex class")
        void whenComplexClass_thenExtractAllFieldNames() {
            // Act
            var fieldNames = SortUtils.getFieldNames(ComplexClass.class);

            // Assert
            assertThat(fieldNames).isNotNull();
            assertThat(fieldNames).hasSize(5);
            assertThat(fieldNames).contains("id", "title", "value", "active", "simpleClass");
        }

        static class SimpleClass {
            private String name;
            private Integer count;
        }

        static class ComplexClass {
            public Boolean active;
            protected String title;
            private Long id;
            private Integer value;
            private SimpleClass simpleClass;
        }
    }

    @Nested
    @DisplayName("Mixed Class and No Class Tests")
    class MixedClassNoClassTests {

        @Test
        @DisplayName("should accept valid fields when class provided and invalid fields when no class provided")
        void whenClassProvidedOrNot_thenBehaveDifferently() {
            // Arrange
            String[] validForTestEntity = {"id", "asc"};
            String[] invalidForTestEntity = {"nonExistingField", "desc"};

            // Act & Assert - With class validation
            Sort validResult = SortUtils.createObject(validForTestEntity, TestEntity.class);
            assertThat(validResult.toList()).hasSize(1);
            assertThat(validResult.toList().getFirst().getProperty()).isEqualTo("id");

            // Should throw exception for invalid field when class is provided
            assertThrows(BadRequestException.class, () ->
                    SortUtils.createObject(invalidForTestEntity, TestEntity.class));

            // Should accept any field name when no class is provided
            Sort noClassResult = SortUtils.createObject(invalidForTestEntity, null);
            assertThat(noClassResult.toList()).hasSize(1);
            assertThat(noClassResult.toList().getFirst().getProperty()).isEqualTo("nonExistingField");
        }

        static class TestEntity {
            private Long id;
            private String name;
        }
    }
}