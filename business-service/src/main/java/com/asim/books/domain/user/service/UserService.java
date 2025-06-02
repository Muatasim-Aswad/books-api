package com.asim.books.domain.user.service;

import com.asim.books.common.exception.DuplicateResourceException;
import com.asim.books.common.exception.ResourceNotFoundException;
import com.asim.books.common.exception.OptimisticLockException;
import com.asim.books.domain.user.model.dto.UserCreateDto;
import com.asim.books.domain.user.model.dto.UserRoleUpdateDto;
import com.asim.books.domain.user.model.dto.UserViewDto;

public interface UserService {
    /**
     * Creates a new user
     *
     * @param userCreateDto User creation data
     * @return UserViewDto of the created user
     * @throws DuplicateResourceException if user with the same ID already exists
     */
    UserViewDto createUser(UserCreateDto userCreateDto) throws DuplicateResourceException;

    /**
     * Updates user's role
     *
     * @param userRoleUpdateDto User role update data
     * @return UserViewDto of the updated user
     * @throws ResourceNotFoundException if user not found
     * @throws OptimisticLockException   if version mismatch
     */
    UserViewDto updateUserRole(UserRoleUpdateDto userRoleUpdateDto)
            throws ResourceNotFoundException, OptimisticLockException;

    /**
     * Retrieves user by ID
     *
     * @param userId User ID
     * @return UserViewDto of the requested user
     * @throws ResourceNotFoundException if user not found
     */
    UserViewDto getUser(Long userId) throws ResourceNotFoundException;
}
