package com.asim.business.domain.user.model.dto;

import com.asim.business.common.annotation.validation.RequiredNumber;
import com.asim.business.common.annotation.validation.domain.Name;
import com.asim.business.domain.user.model.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for admin to change a user's role.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleUpdateDto {
    @Name
    private String name;

    @NotNull
    private Role role;

    @RequiredNumber
    private Integer version;
}