package com.asim.auth.core.controller.annotations.springdoc.method.general;

import com.asim.auth.infrastructure.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Response for forbidden operations (403).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface UnauthorizedApiResponse {
}