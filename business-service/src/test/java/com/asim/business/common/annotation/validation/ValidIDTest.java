package com.asim.business.common.annotation.validation;

import com.asim.business.common.annotation.validation.domain.ValidID;
import com.asim.business.test.util.ValidationTestHelper;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ValidID Annotation Tests")
class ValidIDTest {

    @Test
    @DisplayName("should pass validation when ID is valid")
    void validIdShouldPass() {
        // Arrange
        TestClass testObject = new TestClass(1L);

        // Act
        Set<ConstraintViolation<TestClass>> violations = ValidationTestHelper.validate(testObject);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should fail validation when ID is null")
    void nullIdShouldFail() {
        // Arrange
        TestClass testObject = new TestClass(null);

        // Act
        Set<ConstraintViolation<TestClass>> violations = ValidationTestHelper.validate(testObject);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ID cannot be null");
    }

    @ParameterizedTest
    @DisplayName("should fail validation when ID is not positive")
    @ValueSource(longs = {0, -1, -100})
    void nonPositiveIdShouldFail(long id) {
        // Arrange
        TestClass testObject = new TestClass(id);

        // Act
        Set<ConstraintViolation<TestClass>> violations = ValidationTestHelper.validate(testObject);

        // Assert
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ID must be positive");
    }

    static class TestClass {
        @ValidID
        private Long id;

        public TestClass(Long id) {
            this.id = id;
        }
    }
}