package com.asim.auth.core.controller.annotations.springdoc.method;

import com.asim.auth.core.model.dto.UserPublic;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * SpringDoc ApiResponse for user created (201).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses(@ApiResponse(responseCode = "200", description = "Password updated successfully",
        content = @Content(schema = @Schema(implementation = UserPublic.class)))
)
public @interface PasswordUpdatedApiResponse {
}
