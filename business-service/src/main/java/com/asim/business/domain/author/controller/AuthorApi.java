package com.asim.business.domain.author.controller;

import com.asim.business.common.annotation.springdoc.method.*;
import com.asim.business.common.annotation.springdoc.param.IdParam;
import com.asim.business.common.annotation.springdoc.param.PageNumberQuery;
import com.asim.business.common.annotation.springdoc.param.PageSizeQuery;
import com.asim.business.common.annotation.springdoc.param.SortQuery;
import com.asim.business.common.annotation.validation.domain.Name;
import com.asim.business.common.annotation.validation.domain.ValidID;
import com.asim.business.domain.author.controller.annotation.springdoc.method.AuthorCreatedApiResponse;
import com.asim.business.domain.author.controller.annotation.springdoc.method.AuthorRetrievedApiResponse;
import com.asim.business.domain.author.controller.annotation.springdoc.method.AuthorUpdatedApiResponse;
import com.asim.business.domain.author.controller.annotation.springdoc.param.AuthorNameQuery;
import com.asim.business.domain.author.model.dto.AuthorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

/**
 * API documentation for author management operations.
 * Contains validation annotations for method params as defining them in the class causes constraints conflict error, see Err: HV000151.
 * For reusability and readability, the annotation details are defined in common package, or for specific use cases relative to the user package.
 */
@Tag(name = "Authors", description = "Author management API endpoints")
@InternalServerErrorApiResponse
@ValidationFailureApiResponse
@UnauthorizedApiResponse
@ForbiddenOperationApiResponse
@Validated
public interface AuthorApi {

    @Operation(
            summary = "Create a new author",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @AuthorCreatedApiResponse
    @DuplicateResourceApiResponse
    AuthorDto addAuthor(@Validated(AuthorDto.OnCreate.class) @RequestBody(description = "Author details, excluding auto-generated fields", required = true)
                        AuthorDto author);


    @Operation(
            summary = "Update an author",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @AuthorUpdatedApiResponse
    @ResourceNotFoundApiResponse
    AuthorDto updateAuthor(@ValidID @IdParam Long id,
                           @Valid @RequestBody(description = "Author details that should be updated", required = true)
                           AuthorDto author);


    @Operation(
            summary = "Delete an author",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ResourceDeletedApiResponse
    @ResourceNotFoundApiResponse
    void deleteAuthor(@ValidID @IdParam Long id);


    @Operation(summary = "Get an author")
    @AuthorRetrievedApiResponse
    @ResourceNotFoundApiResponse
    AuthorDto getAuthor(@ValidID @IdParam Long id);


    @Operation(summary = "Get all authors with pagination, sorting and filtering")
    @ResourcesRetrievedApiResponse
    Page<AuthorDto> getAuthors(
            @PageNumberQuery int page,
            @PageSizeQuery int size,
            @SortQuery String[] sort,
            @Name @AuthorNameQuery String name);
}