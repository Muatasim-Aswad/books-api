package com.asim.books.author.controller;

import com.asim.books.author.controller.annotation.springdoc.method.AuthorCreatedApiResponse;
import com.asim.books.author.controller.annotation.springdoc.method.AuthorRetrievedApiResponse;
import com.asim.books.author.controller.annotation.springdoc.method.AuthorUpdatedApiResponse;
import com.asim.books.author.model.dto.AuthorDto;
import com.asim.books.common.annotation.springdoc.method.*;
import com.asim.books.common.annotation.springdoc.param.*;
import com.asim.books.common.annotation.validation.ValidID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

/**
 * API documentation for author management operations.
 * Contains validation annotations for method params as defining them in the class causes constraints conflict error, see Err: HV000151.
 */
@Tag(name = "Authors", description = "Author management API endpoints")
@InternalServerErrorApiResponse
@Validated
public interface AuthorApi {

    @Operation(summary = "Create a new author")
    @AuthorCreatedApiResponse
    @ValidationFailureApiResponse
    @DuplicateResourceApiResponse
    AuthorDto addAuthor(@Valid @RequestBody(description = "Author details, excluding ID", required = true) AuthorDto author);


    @Operation(summary = "Get an author")
    @AuthorRetrievedApiResponse
    @ResourceNotFoundApiResponse
    AuthorDto getAuthor(@ValidID @IdParam Long id);


    @Operation(summary = "Update an author")
    @AuthorUpdatedApiResponse
    @ValidationFailureApiResponse
    @ResourceNotFoundApiResponse
    AuthorDto updateAuthor(@ValidID @IdParam Long id,
                           @Validated(AuthorDto.Optional.class) @RequestBody(description = "Author details that should be updated", required = true)
                           AuthorDto author);


    @Operation(summary = "Delete an author")
    @ResourceDeletedApiResponse
    @ResourceNotFoundApiResponse
    void deleteAuthor(@ValidID @IdParam Long id);


    @Operation(summary = "Get all authors with pagination, sorting and filtering")
    @ResourcesRetrievedApiResponse
    Page<AuthorDto> getAuthors(
            @PageNumberQuery int page,
            @PageSizeQuery int size,
            @SortQuery String[] sort,
            @NameQuery String name);
}