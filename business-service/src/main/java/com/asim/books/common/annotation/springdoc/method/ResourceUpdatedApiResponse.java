
package com.asim.books.common.annotation.springdoc.method;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Response for resource updated (200).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses(@ApiResponse(responseCode = "200", description = "Resource updated successfully",
        content = @Content(schema = @Schema(implementation = Object.class)))
)
public @interface ResourceUpdatedApiResponse {
}