package com.asim.books.common.mapper.entity;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BaseEntityMapper Tests")
class BaseEntityMapperTest {

    private ModelMapper modelMapper;
    private TestEntityMapper mapper;

    @BeforeEach
    void setUp() {
        // Arrange
        modelMapper = new ModelMapper();
        mapper = new TestEntityMapper(modelMapper);
    }

    @Test
    @DisplayName("should map DTO to entity when DTO is provided")
    void whenMappingFromDto_thenReturnEntity() {
        // Arrange
        TestDto dto = new TestDto();
        dto.setId(1L);
        dto.setName("Test Name");

        // Act
        TestEntity entity = mapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    @DisplayName("should return null when mapping null DTO to entity")
    void whenMappingFromNullDto_thenReturnNull() {
        // Act
        TestEntity entity = mapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    @DisplayName("should map entity to DTO when entity is provided")
    void whenMappingFromEntity_thenReturnDto() {
        // Arrange
        TestEntity entity = new TestEntity();
        entity.setId(1L);
        entity.setName("Test Name");

        // Act
        TestDto dto = mapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    @DisplayName("should return null when mapping null entity to DTO")
    void whenMappingFromNullEntity_thenReturnNull() {
        // Act
        TestDto dto = mapper.toDto(null);

        // Assert
        assertNull(dto);
    }

    @Test
    @DisplayName("should throw NullPointerException when creating mapper with null ModelMapper")
    void whenCreatingMapperWithNullModelMapper_thenThrowNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new TestEntityMapper(null));
    }

    @Test
    @DisplayName("should throw NullPointerException when creating mapper with null entity class")
    void whenCreatingMapperWithNullEntityClass_thenThrowNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new InvalidEntityMapper(modelMapper));
    }

    @Test
    @DisplayName("should throw NullPointerException when creating mapper with null DTO class")
    void whenCreatingMapperWithNullDtoClass_thenThrowNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new InvalidDtoMapper(modelMapper));
    }

    // Helper test classes
    @Setter
    @Getter
    static class TestEntity {
        private Long id;
        private String name;

    }

    @Setter
    @Getter
    static class TestDto {
        private Long id;
        private String name;

    }

    static class TestEntityMapper extends BaseEntityMapper<TestEntity, TestDto> {
        public TestEntityMapper(ModelMapper modelMapper) {
            super(modelMapper, TestEntity.class, TestDto.class);
        }
    }

    static class InvalidEntityMapper extends BaseEntityMapper<TestEntity, TestDto> {
        public InvalidEntityMapper(ModelMapper modelMapper) {
            super(modelMapper, null, TestDto.class);
        }
    }

    static class InvalidDtoMapper extends BaseEntityMapper<TestEntity, TestDto> {
        public InvalidDtoMapper(ModelMapper modelMapper) {
            super(modelMapper, TestEntity.class, null);
        }
    }
}