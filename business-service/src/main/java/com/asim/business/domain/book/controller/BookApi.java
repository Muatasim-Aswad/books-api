package com.asim.business.domain.book.controller;

import com.asim.business.common.annotation.springdoc.method.*;
import com.asim.business.common.annotation.springdoc.param.IdParam;
import com.asim.business.common.annotation.springdoc.param.PageNumberQuery;
import com.asim.business.common.annotation.springdoc.param.PageSizeQuery;
import com.asim.business.common.annotation.springdoc.param.SortQuery;
import com.asim.business.common.annotation.validation.domain.BookTitle;
import com.asim.business.common.annotation.validation.domain.Name;
import com.asim.business.common.annotation.validation.domain.ValidID;
import com.asim.business.domain.book.controller.annotation.springdoc.method.BookCreatedApiResponse;
import com.asim.business.domain.book.controller.annotation.springdoc.method.BookRetrievedApiResponse;
import com.asim.business.domain.book.controller.annotation.springdoc.method.BookUpdatedApiResponse;
import com.asim.business.domain.book.controller.annotation.springdoc.param.AuthorNameQuery;
import com.asim.business.domain.book.controller.annotation.springdoc.param.BookTitleQuery;
import com.asim.business.domain.book.model.dto.BookDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

/**
 * API documentation for book management operations.
 * Contains validation annotations for method params as defining them in the class causes constraints conflict error, see Err: HV000151.
 * For reusability and readability, the annotation details are defined in common package, or for specific use cases relative to the user package.
 */
@Tag(name = "Books", description = "Book management API endpoints")
@InternalServerErrorApiResponse
@ValidationFailureApiResponse
@Validated
public interface BookApi {

    @Operation(
            summary = "Create a new book",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @BookCreatedApiResponse
    @DuplicateResourceApiResponse
    @ForbiddenOperationApiResponse
    BookDto addBook(@Validated(BookDto.OnCreate.class)
                    @RequestBody(description = "Book details, excluding auto-generated fields", required = true)
                    BookDto book);


    @Operation(
            summary = "Update a book",
            description = "Updates a book by its ID. The ID must be a valid positive number. Only fields that are not auto-generated can be updated.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @BookUpdatedApiResponse
    @ResourceNotFoundApiResponse
    @ForbiddenOperationApiResponse
    BookDto updateBook(@ValidID @IdParam Long id,
                       @Valid @RequestBody(description = "Book details that should be updated", required = true)
                       BookDto book);


    @Operation(
            summary = "Delete a book",
            description = "Deletes a book by its ID. The ID must be a valid positive number.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ResourceDeletedApiResponse
    @ResourceNotFoundApiResponse
    void deleteBook(@ValidID @IdParam Long id);


    @Operation(
            summary = "Get a book",
            description = "Retrieves a book by its ID. The ID must be a valid positive number."
    )
    @BookRetrievedApiResponse
    @ResourceNotFoundApiResponse
    BookDto getBook(@ValidID @IdParam Long id);


    @Operation(
            summary = "Get all books with pagination, sorting and filtering",
            description = "Retrieves a paginated list of books. Supports sorting and filtering by title and author name."
    )
    @ResourcesRetrievedApiResponse
    Page<BookDto> getBooks(
            @PageNumberQuery int page,
            @PageSizeQuery int size,
            @SortQuery String[] sort,
            @BookTitle @BookTitleQuery String title,
            @Name @AuthorNameQuery String author);
}