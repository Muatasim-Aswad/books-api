package com.asim.books.domain.book.model.dto;

import com.asim.books.domain.author.model.dto.AuthorDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Data transfer object for book entities.
 * The following groups are used for external input validation:
 * - Default: enforce all mandatory fields
 * - Optional: enforce validation but allow the absence of optional fields
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    @NotNull(message = "ISBN cannot be null", groups = Default.class)
    @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters",
            groups = {Default.class, Optional.class})
    private String isbn;

    @NotBlank(message = "Book title cannot be empty", groups = Default.class)
    @Size(min = 1, max = 255, message = "Book title must be between 1 and 255 characters",
            groups = {Default.class, Optional.class})
    private String title;

    private AuthorDto author;

    // Read only fields
    @Null(message = "Id is read only, to perform an action on a specific resource use the path parameter. e.g. book/1", groups = {Default.class, Optional.class})
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