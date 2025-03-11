package com.asim.books.common.mapper.entity;

public interface EntityMapper<Entity, Dto> {
    Entity toEntity(Dto dto);
    Dto toDto(Entity entity);
}
