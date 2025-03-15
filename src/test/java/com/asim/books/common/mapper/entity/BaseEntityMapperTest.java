package com.asim.books.common.mapper.entity;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BaseEntityMapperTest {

    private TestEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TestEntityMapper(new ModelMapper());
    }

    @Test
    void shouldMapEntityToDto() {
        TestEntity entity = new TestEntity();
        entity.setId(1L);
        entity.setName("Test");
        entity.setValue(100);

        TestDto dto = mapper.toDto(entity);

        assertThat(dto, notNullValue());
        assertThat(dto.getId(), is(entity.getId()));
        assertThat(dto.getName(), is(entity.getName()));
        assertThat(dto.getValue(), is(entity.getValue()));
    }

    @Test
    void shouldMapDtoToEntity() {
        TestDto dto = new TestDto();
        dto.setId(1L);
        dto.setName("Test");
        dto.setValue(100);

        TestEntity entity = mapper.toEntity(dto);

        assertThat(entity, notNullValue());
        assertThat(entity.getId(), is(dto.getId()));
        assertThat(entity.getName(), is(dto.getName()));
        assertThat(entity.getValue(), is(dto.getValue()));
    }

    @Test
    void shouldHandleNullValuesInEntity() {
        TestEntity entity = new TestEntity();
        entity.setId(1L);
        entity.setName(null);
        entity.setValue(null);

        TestDto dto = mapper.toDto(entity);

        assertThat(dto, notNullValue());
        assertThat(dto.getId(), is(entity.getId()));
        assertThat(dto.getName(), nullValue());
        assertThat(dto.getValue(), nullValue());
    }

    @Test
    void shouldHandleNullValuesInDto() {
        TestDto dto = new TestDto();
        dto.setId(1L);
        dto.setName(null);
        dto.setValue(null);

        TestEntity entity = mapper.toEntity(dto);

        assertThat(entity, notNullValue());
        assertThat(entity.getId(), is(dto.getId()));
        assertThat(entity.getName(), nullValue());
        assertThat(entity.getValue(), nullValue());
    }

    @Test
    void shouldCorrectlyMapNullEntity() {
        assertThat(mapper.toDto(null), nullValue());
    }

    @Test
    void shouldCorrectlyMapNullDto() {
        assertThat(mapper.toEntity(null), nullValue());
    }

    // Test classes
    @Setter
    @Getter
    static class TestEntity {
        private Long id;
        private String name;
        private Integer value;
    }

    @Setter
    @Getter
    static class TestDto {
        private Long id;
        private String name;
        private Integer value;
    }

    // Concrete implementation of BaseEntityMapper
    static class TestEntityMapper extends BaseEntityMapper<TestEntity, TestDto> {
        public TestEntityMapper(ModelMapper modelMapper) {
            super(modelMapper, TestEntity.class, TestDto.class);
        }
    }
}