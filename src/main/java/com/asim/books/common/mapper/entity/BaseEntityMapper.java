package com.asim.books.common.mapper.entity;

import org.modelmapper.ModelMapper;

import java.util.Objects;

public abstract class BaseEntityMapper<Entity, Dto> implements EntityMapper<Entity, Dto> {

    protected final ModelMapper modelMapper;
    private final Class<Entity> entityClass;
    private final Class<Dto> dtoClass;

    protected BaseEntityMapper(ModelMapper modelMapper, Class<Entity> entityClass, Class<Dto> dtoClass) {
        this.modelMapper = Objects.requireNonNull(modelMapper, "ModelMapper must not be null");
        this.entityClass = Objects.requireNonNull(entityClass, "Entity class must not be null");
        this.dtoClass = Objects.requireNonNull(dtoClass, "DTO class must not be null");
    }

    @Override
    public Entity toEntity(Dto dto) {
        return dto == null ? null : modelMapper.map(dto, entityClass);
    }

    @Override
    public Dto toDto(Entity entity) {
        return entity == null ? null : modelMapper.map(entity, dtoClass);
    }
}