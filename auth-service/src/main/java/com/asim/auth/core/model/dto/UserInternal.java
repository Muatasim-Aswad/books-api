package com.asim.auth.core.model.dto;

import lombok.*;

/**
 * Internal user DTO that extends UserPublic and adds an ID field.
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInternal {
    private Long id;
    private String name;
}
