package com.asim.books.domain.user.model.dto;

import com.asim.books.common.annotation.validation.RequiredNumber;
import com.asim.books.common.annotation.validation.domain.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating user's own name.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserNameUpdateDto {
    @Name
    private String name;

    @RequiredNumber
    private Integer version;
}