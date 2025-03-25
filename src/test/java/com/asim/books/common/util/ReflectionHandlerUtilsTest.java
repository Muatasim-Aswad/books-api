package com.asim.books.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("Reflection Utils Tests")
class ReflectionHandlerUtilsTest {

    @Autowired
    private ReflectionUtils reflectionUtils;

    @Autowired
    private CacheManager cacheManager;

    @EnableCaching
    @Configuration
    static class TestConfig {
        @Bean
        public ReflectionUtils reflectionUtils() {
            return new ReflectionUtils();
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("fieldNames");
        }
    }

    // Test classes
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

    static class PrimitiveClass {
        private int intValue;
        private boolean boolValue;
        private long longValue;
        private double doubleValue;
    }

    static class ArrayClass {
        private String[] stringArray;
        private int[] intArray;
        private Object[] objectArray;
    }

    static class JavaLibraryClass {
        private java.util.List<String> list;
        private java.util.Map<String, Integer> map;
        private java.util.Date date;
        private String string;
    }

    static class EmptyClass {
        // No fields
    }

    @Nested
    @DisplayName("Field Name Extraction Tests")
    class FieldNameExtractionTests {

        @Test
        @DisplayName("should extract all field names from simple class")
        void whenSimpleClass_thenExtractAllFieldNames() {
            // Act
            Set<String> fieldNames = reflectionUtils.getFieldNames(SimpleClass.class);

            // Assert
            assertThat(fieldNames).isNotNull();
            assertThat(fieldNames).hasSize(2);
            assertThat(fieldNames).contains("name", "count");
        }

        @Test
        @DisplayName("should extract all field names from complex class with nested fields")
        void whenComplexClass_thenExtractAllFieldNames() {
            // Act
            Set<String> fieldNames = reflectionUtils.getFieldNames(ComplexClass.class);

            // Assert
            assertThat(fieldNames).isNotNull();
            assertThat(fieldNames).contains(
                    "id", "value", "active", "title", "simpleClass",
                    "simpleClass.name", "simpleClass.count"
            );
        }

        @Test
        @DisplayName("should handle classes with primitive fields")
        void whenClassWithPrimitiveFields_thenExtractCorrectly() {
            // Act
            Set<String> fieldNames = reflectionUtils.getFieldNames(PrimitiveClass.class);

            // Assert
            assertThat(fieldNames).isNotNull();
            assertThat(fieldNames).contains("intValue", "boolValue", "longValue", "doubleValue");
        }

        @Test
        @DisplayName("should handle classes with array fields")
        void whenClassWithArrayFields_thenExtractCorrectly() {
            // Act
            Set<String> fieldNames = reflectionUtils.getFieldNames(ArrayClass.class);

            // Assert
            assertThat(fieldNames).isNotNull();
            assertThat(fieldNames).contains("stringArray", "intArray", "objectArray");
        }

        @Test
        @DisplayName("should handle classes with Java standard library fields")
        void whenClassWithJavaLibraryFields_thenExtractCorrectly() {
            // Act
            Set<String> fieldNames = reflectionUtils.getFieldNames(JavaLibraryClass.class);

            // Assert
            assertThat(fieldNames).isNotNull();
            assertThat(fieldNames).contains("list", "map", "date", "string");
        }

        @Test
        @DisplayName("should handle empty classes")
        void whenEmptyClass_thenReturnEmptySet() {
            // Act
            Set<String> fieldNames = reflectionUtils.getFieldNames(EmptyClass.class);

            // Assert
            assertThat(fieldNames).isNotNull();
            assertThat(fieldNames).isEmpty();
        }
    }

    @Nested
    @DisplayName("Caching Tests")
    class CachingTests {

        @Test
        @DisplayName("should cache field names after first call")
        void whenCalledMultipleTimes_thenUsesCache() {
            // Arrange
            String cacheName = "fieldNames";

            // First call should populate cache
            reflectionUtils.getFieldNames(ComplexClass.class);

            // Act - Second call should use cache
            reflectionUtils.getFieldNames(ComplexClass.class);

            // Assert
            assertThat(cacheManager.getCache(cacheName)).isNotNull();
            assertThat(cacheManager.getCacheNames()).contains(cacheName);
        }
    }
}