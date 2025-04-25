package com.asim.books.domain.user.model.dto;

import com.asim.books.common.annotation.validation.domain.Name;
import com.asim.books.common.annotation.validation.domain.ValidID;
import com.asim.books.domain.user.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new user.
 * Used by auth microservice.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDto {
    @ValidID
    private Long id;

    @Name
    private String name;
}