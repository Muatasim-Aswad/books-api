package com.asim.books.author.controller.annotation.springdoc.method;

import com.asim.books.author.model.dto.AuthorDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Response for author retrieved (200).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses(@ApiResponse(responseCode = "200", description = "Author retrieved successfully",
        content = @Content(schema = @Schema(implementation = AuthorDto.class)))
)
public @interface AuthorRetrievedApiResponse {
}
