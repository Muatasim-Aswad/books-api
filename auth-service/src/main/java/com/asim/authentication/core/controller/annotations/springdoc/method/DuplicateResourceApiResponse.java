package com.asim.authentication.core.controller.annotations.springdoc.method;

import com.asim.authentication.infrastructure.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Response for duplicate resource (409).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses({
        @ApiResponse(responseCode = "409", description = "Resource already exists",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface DuplicateResourceApiResponse {
}