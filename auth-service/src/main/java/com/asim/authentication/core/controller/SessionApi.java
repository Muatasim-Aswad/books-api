package com.asim.authentication.core.controller;

import com.asim.authentication.core.model.dto.RefreshTokenRequest;
import com.asim.authentication.core.model.dto.TokenResponse;
import com.asim.authentication.core.model.dto.UserInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Authentication and session management endpoints")
public interface SessionApi {

    @Operation(summary = "Login a user", description = "Authenticates user credentials and returns access and refresh tokens")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated",
            content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    TokenResponse login(@Valid
                        @RequestBody(
                                description = "User details",
                                required = true,
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema( implementation = UserInput.class),
                                        examples = {
                                                @ExampleObject(
                                                        value = "{\"name\": \"new-user\", \"password\": \"Password.1\"}"
                                                )
                                        }
                                )
                        )
                        UserInput user);

    @Operation(summary = "Refresh tokens", description = "Generate new access token using a valid refresh token")
    @ApiResponse(responseCode = "200", description = "Successfully refreshed tokens",
            content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    TokenResponse refresh(@Valid RefreshTokenRequest request);

    @Operation(summary = "Logout user", description = "Invalidates the current user session")
    @ApiResponse(responseCode = "204", description = "Successfully logged out")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    void logout(Authentication authentication);
}