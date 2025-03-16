package com.asim.books.author.controller;

import com.asim.books.author.model.dto.AuthorDto;
import com.asim.books.common.annotation.springdoc.*;
import com.asim.books.common.annotation.springdoc.specific.AuthorIdParameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;

/**
 * API documentation for author management operations.
 */
@Tag(name = "Authors", description = "Author management API endpoints")
@InternalServerErrorResponse
public interface AuthorControllerDocs {

    @Operation(summary = "Create a new author")
    @ApiResponse(responseCode = "201", description = "Author created successfully",
            content = @Content(schema = @Schema(implementation = AuthorDto.class)))
    @ValidationErrorResponse
    @DuplicateResourceResponse
    AuthorDto addAuthor(@RequestBody(description = "Author details, excluding ID", required = true) AuthorDto author);


    @Operation(summary = "Get author by ID")
    @ApiResponse(responseCode = "200", description = "Author found",
            content = @Content(schema = @Schema(implementation = AuthorDto.class)))
    @ResourceNotFoundResponse
    AuthorDto getAuthor(@AuthorIdParameter Long id);


    @Operation(summary = "Update an existing author")
    @ApiResponse(responseCode = "200", description = "Author updated successfully",
            content = @Content(schema = @Schema(implementation = AuthorDto.class)))
    @ValidationErrorResponse
    @ResourceNotFoundResponse
    @ForbiddenOperationResponse
    AuthorDto updateAuthor(@AuthorIdParameter Long id,
                           @RequestBody(description = "Author details that should be updated", required = true)
                           AuthorDto author);


    @Operation(summary = "Delete an author")
    @ApiResponse(responseCode = "204", description = "Author deleted successfully",
            content = @Content(schema = @Schema(hidden = true)))
    @ResourceNotFoundResponse
    void deleteAuthor(@AuthorIdParameter Long id);


    @Operation(summary = "Get all authors with pagination, sorting and filtering")
    @ApiResponse(responseCode = "200", description = "List of authors retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class)))
    Page<AuthorDto> getAuthors(
            @Parameter(description = "Page number (0-indexed)") int page,
            @Parameter(description = "Page size") int size,
            @Parameter(description = "Sorting criteria: field,asc|desc", example = "sort=id,asc&sort=name,desc") String[] sort,
            @Parameter(description = "Filter by name (case-insensitive, partial match)") String name);
}