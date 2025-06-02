package com.asim.business.common.model.mapper;

/**
 * A generic interface for mapping between entities and DTOs.
 *
 * @param <Entity> the entity type
 * @param <Dto>    the DTO type
 */
public interface EntityDtoMapper<Entity, Dto> {

    /**
     * Converts a {@code Dto} to an {@code Entity}.
     *
     * @param dto the DTO to convert
     * @return the entity
     */
    Entity toEntity(Dto dto);

    /**
     * Converts an {@code Entity} to a {@code Dto}.
     *
     * @param entity the entity to convert
     * @return the DTO
     */
    Dto toDto(Entity entity);
}
