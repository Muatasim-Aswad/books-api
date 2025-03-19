package com.asim.books.domain.author.model.dto;

import com.asim.books.common.annotation.validation.Age;
import com.asim.books.common.annotation.validation.FullName;
import com.asim.books.common.model.ContradictionCheckable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Data transfer object for author entities.
 * The following groups are used for external input validation:
 * - Default: enforce all mandatory fields
 * - Optional: enforce validation but allow the absence of optional fields
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto implements ContradictionCheckable<AuthorDto> {
    @FullName(groups = {Default.class, Optional.class})
    @NotBlank(message = "Author name cannot be empty")
    private String name;

    @Age(groups = {Default.class, Optional.class})
    @NotNull(message = "Age cannot be null")
    private Integer age;

    //Read only fields
    @Null(message = "Id is read only, to perform an action on a specific resource use the path parameter. e.g. author/1", groups = {Default.class, Optional.class})
    private Long id;

    @Null(message = "CreatedAt is read-only", groups = {Default.class, Optional.class})
    private ZonedDateTime createdAt;
    @Null(message = "UpdatedAt is read-only", groups = {Default.class, Optional.class})
    private ZonedDateTime updatedAt;

    @Null(message = "Version is read-only", groups = {Default.class, Optional.class})
    private Integer version;

    /**
     * Validation group for optional fields.
     * Used for partial updates.
     */
    public interface Optional {
    }
}