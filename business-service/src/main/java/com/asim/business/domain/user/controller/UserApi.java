package com.asim.business.domain.user.controller;

import com.asim.business.common.annotation.springdoc.method.*;
import com.asim.business.domain.user.model.dto.UserRoleUpdateDto;
import com.asim.business.domain.user.model.dto.UserViewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * API documentation for user management operations.
 * Contains validation annotations for method params.
 */
@Tag(name = "Users", description = "User management API endpoints")
@InternalServerErrorApiResponse
@ValidationFailureApiResponse
@UnauthorizedApiResponse
@ForbiddenOperationApiResponse
@Validated
public interface UserApi {

    @Operation(
            summary = "Update user role",
            description = "Updates user role. Only users with the 'ADMIN' role can perform this operation.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ResourceUpdatedApiResponse
    @ResourceNotFoundApiResponse
    @OptimisticLockApiResponse
    UserViewDto updateUserRole(
            @Valid @RequestBody(description = "User role update details", required = true)
            UserRoleUpdateDto userRoleUpdateDto
    );
}