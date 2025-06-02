package com.asim.business.common.annotation.springdoc.method;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;

import java.lang.annotation.*;

/**
 * Response for resources retrieved with pagination & sorting (200).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses(@ApiResponse(responseCode = "200", description = "Page of requested resources retrieved successfully",
        content = @Content(schema = @Schema(implementation = Page.class)))
)
public @interface ResourcesRetrievedApiResponse {
}
