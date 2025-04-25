package com.asim.books.domain.user.model.dto;

import com.asim.books.domain.user.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Data transfer object for user entity.
 * Meant to be used for public exposure.
 * Should not contain any sensitive information.
 * Should not be used for input.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserViewDto {
    private String name;
    private Role role;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Integer version;
}
