package com.asim.books.common.annotation.validation;

import com.asim.books.test.util.ValidationTestHelper;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FullName Annotation Tests")
class FullNameTest {

    @Test
    @DisplayName("should pass validation when name is valid")
    void validNameShouldPass() {
        // Arrange
        TestClass testObject = new TestClass("John Doe");

        // Act
        Set<ConstraintViolation<TestClass>> violations = ValidationTestHelper.validate(testObject);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should fail validation when name is too short")
    void tooShortNameShouldFail() {
        // Arrange
        TestClass testObject = new TestClass("A");

        // Act
        Set<ConstraintViolation<TestClass>> violations = ValidationTestHelper.validate(testObject);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Name must be between 2 and 100 characters");
    }

    @Test
    @DisplayName("should fail validation when name is too long")
    void tooLongNameShouldFail() {
        // Arrange
        String longName = "A".repeat(101);
        TestClass testObject = new TestClass(longName);

        // Act
        Set<ConstraintViolation<TestClass>> violations = ValidationTestHelper.validate(testObject);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Name must be between 2 and 100 characters");
    }

    @ParameterizedTest
    @DisplayName("should pass validation for boundary values")
    @ValueSource(strings = {"AB", "John", "A very long name with exactly one hundred characters............................................."})
    void boundaryValuesShouldPass(String name) {
        // Arrange
        TestClass testObject = new TestClass(name);

        // Act
        Set<ConstraintViolation<TestClass>> violations = ValidationTestHelper.validate(testObject);

        // Assert
        assertThat(violations).isEmpty();
    }

    static class TestClass {
        @FullName
        private String name;

        public TestClass(String name) {
            this.name = name;
        }
    }
}