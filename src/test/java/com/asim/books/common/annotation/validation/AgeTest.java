package com.asim.books.common.annotation.validation;

import com.asim.books.common.annotation.validation.domain.Age;
import com.asim.books.test.util.ValidationTestHelper;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Age Annotation Tests")
class AgeTest {

    @Test
    @DisplayName("should pass validation when age is valid")
    void validAgeShouldPass() {
        // Arrange
        TestClass testObject = new TestClass(25);

        // Act
        Set<ConstraintViolation<TestClass>> violations = ValidationTestHelper.validate(testObject);

        // Assert
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @DisplayName("should fail validation when age is negative")
    @ValueSource(ints = {-1, -10, -100})
    void negativeAgeShouldFail(int age) {
        // Arrange
        TestClass testObject = new TestClass(age);

        // Act
        Set<ConstraintViolation<TestClass>> violations = ValidationTestHelper.validate(testObject);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Age must be a positive number");
    }

    @ParameterizedTest
    @DisplayName("should fail validation when age exceeds maximum")
    @ValueSource(ints = {151, 200, 1000})
    void excessiveAgeShouldFail(int age) {
        // Arrange
        TestClass testObject = new TestClass(age);

        // Act
        Set<ConstraintViolation<TestClass>> violations = ValidationTestHelper.validate(testObject);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Age must be less than 150");
    }

    @ParameterizedTest
    @DisplayName("should pass validation for boundary values")
    @ValueSource(ints = {0, 1, 149, 150})
    void boundaryValuesShouldPass(int age) {
        // Arrange
        TestClass testObject = new TestClass(age);

        // Act
        Set<ConstraintViolation<TestClass>> violations = ValidationTestHelper.validate(testObject);

        // Assert
        assertThat(violations).isEmpty();
    }

    static class TestClass {
        @Age
        private Integer age;

        public TestClass(Integer age) {
            this.age = age;
        }
    }
}