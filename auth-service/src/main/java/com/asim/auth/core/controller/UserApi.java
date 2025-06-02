package com.asim.auth.core.controller;

import com.asim.auth.core.controller.annotations.springdoc.method.PasswordUpdatedApiResponse;
import com.asim.auth.core.controller.annotations.springdoc.method.UserCreatedApiResponse;
import com.asim.auth.core.controller.annotations.springdoc.method.UserDeletedApiResponse;
import com.asim.auth.core.controller.annotations.springdoc.method.general.DuplicateResourceApiResponse;
import com.asim.auth.core.controller.annotations.springdoc.method.general.UnauthorizedApiResponse;
import com.asim.auth.core.controller.annotations.springdoc.method.general.ValidationFailureApiResponse;
import com.asim.auth.core.model.dto.UserInput;
import com.asim.auth.core.model.dto.UserPublic;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
@Tag(name = "Users", description = "User management endpoints")
public interface UserApi {

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account"
    )
    @UserCreatedApiResponse
    @ValidationFailureApiResponse
    @DuplicateResourceApiResponse
    UserPublic register(@Valid
                        @RequestBody(
                                description = "User details",
                                required = true,
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = UserInput.class),
                                        examples = {
                                                @ExampleObject(
                                                        value = "{\"name\": \"new-user\", \"password\": \"Password.1\"}"
                                                )
                                        }
                                )
                        )
                        UserInput userInput);

    @Operation(
            summary = "Change user password",
            description = "Updates the password for the authenticated user",
            security = {@SecurityRequirement(name = "bearerAuth")}

    )
    @PasswordUpdatedApiResponse
    @UnauthorizedApiResponse
    @ValidationFailureApiResponse
    UserPublic changePassword(@Valid
                              @RequestBody(
                                      description = "User details",
                                      required = true,
                                      content = @Content(
                                              mediaType = "application/json",
                                              schema = @Schema(implementation = UserInput.class),
                                              examples = {
                                                      @ExampleObject(
                                                              value = "{\"name\": \"new-user\", \"password\": \"Password.2\"}"
                                                      )
                                              }
                                      )
                              )
                              UserInput userInput);

    @Operation(
            summary = "Delete user account",
            description = "Permanently removes the user account",
            security = {@SecurityRequirement(name = "bearerAuth")}

    )
    @UserDeletedApiResponse
    @UnauthorizedApiResponse
    void deleteUser();
}
