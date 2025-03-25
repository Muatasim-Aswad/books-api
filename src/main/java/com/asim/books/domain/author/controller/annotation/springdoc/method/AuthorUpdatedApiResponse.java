package com.asim.books.domain.author.controller.annotation.springdoc.method;

import com.asim.books.domain.author.model.dto.AuthorDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * SpringDoc ApiResponse for author updated (200).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses(@ApiResponse(responseCode = "200", description = "Author updated successfully",
        content = @Content(schema = @Schema(implementation = AuthorDto.class)))
)
public @interface AuthorUpdatedApiResponse {
}
