package com.asim.books.domain.book.model.dto;

import com.asim.books.common.annotation.validation.ReadOnly;
import com.asim.books.common.annotation.validation.ReadOnlyId;
import com.asim.books.common.annotation.validation.RequiredString;
import com.asim.books.common.annotation.validation.domain.BookTitle;
import com.asim.books.common.annotation.validation.domain.Isbn;
import com.asim.books.domain.author.model.dto.AuthorDto;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Data transfer object for book entities.
 * The following groups are used for external input validation:
 * - Default: enforces only validation but not (required)
 * - Required: enforces existence validation in addition to the default validations.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    @RequiredString(groups = {AuthorDto.Required.class})
    @Isbn
    private String isbn;

    @RequiredString(groups = {AuthorDto.Required.class})
    @BookTitle
    private String title;

    private AuthorDto author;

    // Read only fields
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
    @ReadOnly
    private Integer version;

    /**
     * Validation group to enforce the existence of the field in addition to the default validations. (required)
     */
    public interface Required extends Default {
    }
}