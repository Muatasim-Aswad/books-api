package com.asim.books.common.annotation.springdoc.method;

import com.asim.books.infrastructure.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Common response for forbidden operations (403).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses({
        @ApiResponse(responseCode = "403", description = "Operation not permitted",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface ForbiddenOperationApiResponse {
}