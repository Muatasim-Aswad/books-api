package com.asim.books.domain.author.model.dto;

import com.asim.books.common.annotation.validation.ReadOnly;
import com.asim.books.common.annotation.validation.ReadOnlyId;
import com.asim.books.common.annotation.validation.RequiredNumber;
import com.asim.books.common.annotation.validation.RequiredString;
import com.asim.books.common.annotation.validation.domain.Age;
import com.asim.books.common.annotation.validation.domain.FullName;
import com.asim.books.common.model.ContradictionCheckable;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Data transfer object for author entities.
 * The following groups are used for external input validation:
 * - Default: enforces only validation but not (required).
 * - Required: enforces existence validation in addition to the default validations.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto implements ContradictionCheckable<AuthorDto> {
    @FullName
    @RequiredString(groups = {Required.class})
    private String name;

    @Age
    @RequiredNumber(groups = {Required.class})
    private Integer age;

    //Auto generated fields
    @ReadOnlyId
    private Long id;

    @ReadOnly
    private ZonedDateTime createdAt;
    @ReadOnly
    private ZonedDateTime updatedAt;
    @ReadOnly
    private Long createdBy;
    @ReadOnly
    private Long updatedBy;

    @ReadOnly(groups = {Required.class})
    private Integer version;

    /**
     * Validation group to enforce the existence of the field in addition to the default validations. (required)
     */
    public interface Required extends Default {
    }
}