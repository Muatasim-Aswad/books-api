package com.asim.auth.core.controller.annotations.springdoc.method;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Response for resource deleted (204).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses(@ApiResponse(responseCode = "204", description = "User deleted successfully",
        content = @Content(schema = @Schema(implementation = Void.class)))
)
public @interface UserDeletedApiResponse {
}
