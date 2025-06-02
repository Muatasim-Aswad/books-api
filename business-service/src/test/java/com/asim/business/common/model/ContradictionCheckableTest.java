package com.asim.business.common.model;

import com.asim.business.common.util.ContradictionCheckable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ContradictionCheckable Tests")
class ContradictionCheckableTest {

    // Test implementations of ContradictionCheckable
    static class TestModel implements ContradictionCheckable<TestModel> {
        private String name;
        private Integer age;

        public TestModel(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestModel testModel = (TestModel) o;
            if (name != null ? !name.equals(testModel.name) : testModel.name != null) return false;
            return age != null ? age.equals(testModel.age) : testModel.age == null;
        }
    }

    static class NestedModel {
        private String detail;

        public NestedModel(String detail) {
            this.detail = detail;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NestedModel that = (NestedModel) o;
            return detail != null ? detail.equals(that.detail) : that.detail == null;
        }
    }

    static class ComplexTestModel implements ContradictionCheckable<ComplexTestModel> {
        private String name;
        private Integer age;
        private NestedModel nestedModel;

        public ComplexTestModel(String name, Integer age, NestedModel nestedModel) {
            this.name = name;
            this.age = age;
            this.nestedModel = nestedModel;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ComplexTestModel that = (ComplexTestModel) o;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (age != null ? !age.equals(that.age) : that.age != null) return false;
            return nestedModel != null ? nestedModel.equals(that.nestedModel) : that.nestedModel == null;
        }
    }

    @Nested
    @DisplayName("Default Implementation Tests")
    class DefaultImplementationTests {

        @Test
        @DisplayName("should not find contradiction when both objects are equal")
        void whenObjectsEqual_thenNoContradiction() {
            // Arrange
            TestModel model1 = new TestModel("test", 10);
            TestModel model2 = new TestModel("test", 10);

            // Act
            boolean result = model1.doesContradict(model2);

            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("should find contradiction when objects have different values")
        void whenObjectsHaveDifferentValues_thenContradiction() {
            // Arrange
            TestModel model1 = new TestModel("test1", 10);
            TestModel model2 = new TestModel("test2", 10);

            // Act
            boolean result = model1.doesContradict(model2);

            // Assert
            assertTrue(result);
        }

        @Test
        @DisplayName("should find contradiction when one object is null")
        void whenOtherObjectNull_thenContradiction() {
            // Arrange
            TestModel model = new TestModel("test", 10);

            // Act
            boolean result = model.doesContradict(null);

            // Assert
            assertTrue(result);
        }

        @Test
        @DisplayName("should not find contradiction when null fields are compared")
        void whenNullFields_thenNoContradiction() {
            // Arrange
            TestModel model1 = new TestModel("test", null);
            TestModel model2 = new TestModel(null, 10);

            // Act
            boolean result = model1.doesContradict(model2);

            // Assert
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("Integration with Models Tests")
    class ModelIntegrationTests {

        @Test
        @DisplayName("should correctly implement doesContradict for complex models")
        void whenComplexModel_thenCorrectContradictionDetection() {
            // Arrange
            ComplexTestModel model1 = new ComplexTestModel("name", 25, new NestedModel("detail"));
            ComplexTestModel model2 = new ComplexTestModel("name", 25, new NestedModel("detail"));

            // Act
            boolean result = model1.doesContradict(model2);

            // Assert
            assertFalse(result);
        }

        @Test
        @DisplayName("should detect contradiction in nested objects")
        void whenNestedObjectDiffers_thenContradiction() {
            // Arrange
            ComplexTestModel model1 = new ComplexTestModel("name", 25, new NestedModel("detail1"));
            ComplexTestModel model2 = new ComplexTestModel("name", 25, new NestedModel("detail2"));

            // Act
            boolean result = model1.doesContradict(model2);

            // Assert
            assertTrue(result);
        }

        @Test
        @DisplayName("should not find contradiction when nested object is null")
        void whenNestedObjectNull_thenNoContradiction() {
            // Arrange
            ComplexTestModel model1 = new ComplexTestModel("name", 25, null);
            ComplexTestModel model2 = new ComplexTestModel("name", 25, new NestedModel("detail"));

            // Act
            boolean result = model1.doesContradict(model2);

            // Assert
            assertFalse(result);
        }
    }
}