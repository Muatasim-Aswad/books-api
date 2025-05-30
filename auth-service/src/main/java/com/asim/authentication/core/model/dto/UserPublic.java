package com.asim.authentication.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

/**
 * DTO for sending new user profile related information.
 * Used by business microservice.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPublic {
    private String name;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Integer version;
}
