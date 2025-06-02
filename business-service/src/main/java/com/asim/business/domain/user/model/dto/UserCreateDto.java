package com.asim.business.domain.user.model.dto;

import com.asim.business.common.annotation.validation.domain.Name;
import com.asim.business.common.annotation.validation.domain.ValidID;
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