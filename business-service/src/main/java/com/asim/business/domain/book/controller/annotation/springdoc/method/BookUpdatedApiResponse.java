package com.asim.business.domain.book.controller.annotation.springdoc.method;

import com.asim.business.domain.book.model.dto.BookDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * SpringDoc Api Response for book updated (200).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses(@ApiResponse(responseCode = "200", description = "Book updated successfully",
        content = @Content(schema = @Schema(implementation = BookDto.class)))
)
public @interface BookUpdatedApiResponse {
}
