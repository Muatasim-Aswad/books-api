package com.asim.books.domain.book.controller;

import com.asim.books.common.annotation.springdoc.method.*;
import com.asim.books.common.annotation.springdoc.param.IdParam;
import com.asim.books.common.annotation.springdoc.param.PageNumberQuery;
import com.asim.books.common.annotation.springdoc.param.PageSizeQuery;
import com.asim.books.common.annotation.springdoc.param.SortQuery;
import com.asim.books.common.annotation.validation.domain.BookTitle;
import com.asim.books.common.annotation.validation.domain.FullName;
import com.asim.books.common.annotation.validation.domain.ValidID;
import com.asim.books.domain.book.controller.annotation.springdoc.method.BookCreatedApiResponse;
import com.asim.books.domain.book.controller.annotation.springdoc.method.BookRetrievedApiResponse;
import com.asim.books.domain.book.controller.annotation.springdoc.method.BookUpdatedApiResponse;
import com.asim.books.domain.book.controller.annotation.springdoc.param.AuthorNameQuery;
import com.asim.books.domain.book.controller.annotation.springdoc.param.BookTitleQuery;
import com.asim.books.domain.book.model.dto.BookDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

    @Operation(summary = "Create a new book", description = "Creates a book. Author can be created, or selected from existing authors by ID.")
    @BookCreatedApiResponse
    @DuplicateResourceApiResponse
    @ForbiddenOperationApiResponse
    BookDto addBook(@Validated(BookDto.Required.class)
                    @RequestBody(description = "Book details, excluding auto-generated fields", required = true)
                    BookDto book);


    @Operation(summary = "Get a book")
    @BookRetrievedApiResponse
    @ResourceNotFoundApiResponse
    BookDto getBook(@ValidID @IdParam Long id);


    @Operation(summary = "Update a book")
    @BookUpdatedApiResponse
    @ResourceNotFoundApiResponse
    @ForbiddenOperationApiResponse
    BookDto updateBook(@ValidID @IdParam Long id,
                       @Valid @RequestBody(description = "Book details that should be updated", required = true)
                       BookDto book);


    @Operation(summary = "Delete a book")
    @ResourceDeletedApiResponse
    @ResourceNotFoundApiResponse
    void deleteBook(@ValidID @IdParam Long id);


    @Operation(summary = "Get all books with pagination, sorting and filtering")
    @ResourcesRetrievedApiResponse
    Page<BookDto> getBooks(
            @PageNumberQuery int page,
            @PageSizeQuery int size,
            @SortQuery String[] sort,
            @BookTitle @BookTitleQuery String title,
            @FullName @AuthorNameQuery String author);
}