package com.asim.books.author.controller;

import com.asim.books.author.model.dto.AuthorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * REST controller for author operations.
 * Provides endpoints for CRUD operations on authors.
 */
@Tag(name = "Author", description = "Author management APIs")
public interface AuthorControllerDocs {
    /**
     * Creates a new author.
     *
     * @param author Author data
     * @return The created author with generated ID
     */
    @Operation(summary = "Add a new author", description = "Creates a new author in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author created successfully",
                    content = @Content(schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Author already exists")
    })
    AuthorDto addAuthor(
            @RequestBody(description = "Author data to be created (ID must be omitted)", required = true)
            AuthorDto author);

    /**
     * Retrieves an author by ID.
     *
     * @param id Author ID
     * @return The author data
     */
    @Operation(summary = "Get an author by ID", description = "Returns a single author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author found",
                    content = @Content(schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content)
    })
    AuthorDto getAuthor(
            @Parameter(description = "ID of the author to be retrieved", required = true) Long id);

    /**
     * Updates an existing author.
     *
     * @param id     Author ID
     * @param author Author data to update
     * @return The updated author
     */
    @Operation(summary = "Update an author", description = "Updates an existing author's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully",
                    content = @Content(schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Illegal attempt to modify the author",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content)
    })
    AuthorDto updateAuthor(
            @Parameter(description = "ID of the author to be updated", required = true) Long id,
            @Parameter(description = "Updated author data", required = true) AuthorDto author);

    /**
     * Deletes an author.
     *
     * @param id Author ID
     */
    @Operation(summary = "Delete an author", description = "Deletes an author from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Author cannot be deleted due to constraints",
                    content = @Content)
    })
    public void deleteAuthor(
            @Parameter(description = "ID of the author to be deleted", required = true) Long id);

    /**
     * Retrieves all authors.
     *
     * @return List of all authors
     */
    @Operation(summary = "Get all authors", description = "Returns a list of all authors in the system")
    @ApiResponse(responseCode = "200", description = "List of authors retrieved successfully",
            content = @Content(schema = @Schema(implementation = AuthorDto.class)))
    List<AuthorDto> getAuthors();
}