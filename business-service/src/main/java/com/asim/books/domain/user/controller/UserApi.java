package com.asim.books.domain.user.controller;

import com.asim.books.common.annotation.springdoc.method.*;
import com.asim.books.common.annotation.springdoc.param.IdParam;
import com.asim.books.common.annotation.validation.domain.ValidID;
import com.asim.books.domain.user.model.dto.UserRoleUpdateDto;
import com.asim.books.domain.user.model.dto.UserViewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
@Validated
public interface UserApi {

    @Operation(summary = "Update user role")
    @ResourceUpdatedApiResponse
    @ResourceNotFoundApiResponse
    @OptimisticLockApiResponse
    UserViewDto updateUserRole(
            @Valid @RequestBody(description = "User role update details", required = true)
            UserRoleUpdateDto userRoleUpdateDto
    );
}