package com.asim.books.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ContradictionUtils Tests")
class ContradictionHandlerUtilsTest {

    @Nested
    @DisplayName("Basic Value Contradiction Tests")
    class BasicContradictionTests {

        @Test
        @DisplayName("should not find contradiction when both values are null")
        void whenBothValuesNull_thenNoContradiction() {
            // Arrange
            String value1 = null;
            String value2 = null;

            // Act
            boolean result = ContradictionUtils.contradicts(value1, value2);

            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("should not find contradiction when first value is null")
        void whenFirstValueNull_thenNoContradiction() {
            // Arrange
            String value1 = null;
            String value2 = "test";

            // Act
            boolean result = ContradictionUtils.contradicts(value1, value2);

            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("should not find contradiction when second value is null")
        void whenSecondValueNull_thenNoContradiction() {
            // Arrange
            String value1 = "test";
            String value2 = null;

            // Act
            boolean result = ContradictionUtils.contradicts(value1, value2);

            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("should not find contradiction when values are equal")
        void whenValuesEqual_thenNoContradiction() {
            // Arrange
            String value1 = "test";
            String value2 = "test";

            // Act
            boolean result = ContradictionUtils.contradicts(value1, value2);

            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("should find contradiction when values are different")
        void whenValuesDifferent_thenContradiction() {
            // Arrange
            String value1 = "test1";
            String value2 = "test2";

            // Act
            boolean result = ContradictionUtils.contradicts(value1, value2);

            // Assert
            assertTrue(result);
        }

        @Test
        @DisplayName("should find contradiction when numeric values are different")
        void whenNumericValuesDifferent_thenContradiction() {
            // Arrange
            Integer value1 = 1;
            Integer value2 = 2;

            // Act
            boolean result = ContradictionUtils.contradicts(value1, value2);

            // Assert
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("Object Contradiction Tests")
    class ObjectContradictionTests {

        @Test
        @DisplayName("should find contradiction when one object is null")
        void whenOneObjectNull_thenContradiction() {
            // Arrange
            TestObject obj1 = new TestObject("name", 30);
            TestObject obj2 = null;

            // Act
            boolean result = ContradictionUtils.doesContradict(obj1, obj2);

            // Assert
            assertTrue(result);
        }

        @Test
        @DisplayName("should not find contradiction when both objects are null")
        void whenBothObjectsNull_thenNoContradiction() {
            // Arrange
            TestObject obj1 = null;
            TestObject obj2 = null;

            // Act
            boolean result = ContradictionUtils.doesContradict(obj1, obj2);

            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("should throw exception when comparing different types")
        void whenDifferentTypes_thenThrowException() {
            // Arrange
            TestObject obj1 = new TestObject("name", 30);
            String obj2 = "different type";

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () ->
                    ContradictionUtils.doesContradict(obj1, obj2));
        }

        @Test
        @DisplayName("should not find contradiction when objects are equal")
        void whenObjectsEqual_thenNoContradiction() {
            // Arrange
            TestObject obj1 = new TestObject("name", 30);
            TestObject obj2 = new TestObject("name", 30);

            // Act
            boolean result = ContradictionUtils.doesContradict(obj1, obj2);

            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("should find contradiction when fields have different values")
        void whenFieldsDifferent_thenContradiction() {
            // Arrange
            TestObject obj1 = new TestObject("name1", 30);
            TestObject obj2 = new TestObject("name2", 30);

            // Act
            boolean result = ContradictionUtils.doesContradict(obj1, obj2);

            // Assert
            assertTrue(result);
        }

        @Test
        @DisplayName("should not find contradiction when fields are null in one object")
        void whenFieldsNullInOneObject_thenNoContradiction() {
            // Arrange
            TestObject obj1 = new TestObject("name", null);
            TestObject obj2 = new TestObject(null, 30);

            // Act
            boolean result = ContradictionUtils.doesContradict(obj1, obj2);

            // Assert
            assertFalse(result);
        }

        static class TestObject {
            private String name;
            private Integer age;

            public TestObject(String name, Integer age) {
                this.name = name;
                this.age = age;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                TestObject that = (TestObject) o;
                if (name != null ? !name.equals(that.name) : that.name != null) return false;
                return age != null ? age.equals(that.age) : that.age == null;
            }
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {
        @Test
        @DisplayName("should not find contradiction for empty objects")
        void whenEmptyObjects_thenNoContradiction() {
            // Arrange
            EmptyObject obj1 = new EmptyObject();
            EmptyObject obj2 = new EmptyObject();

            // Act
            boolean result = ContradictionUtils.doesContradict(obj1, obj2);

            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("should throw exception when comparing boxed primitives")
        void whenBoxedPrimitives_thenNoException() {
            // Arrange
            Integer int1 = 1;
            Integer int2 = 2;

            // Act & Assert
            assertThrows(Exception.class, () ->
                    ContradictionUtils.doesContradict(int1, int2));

        }

        static class EmptyObject {
            // No fields
        }
    }
}