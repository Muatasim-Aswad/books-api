package com.asim.business.common.annotation.springdoc.method;

import com.asim.business.infrastructure.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Response for optimistic lock exception (409).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses({
        @ApiResponse(responseCode = "409", description = "Optimistic lock conflict - resource has been modified by another request",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface OptimisticLockApiResponse {
}