package com.asim.books.domain.user.service;

import com.asim.books.common.exception.DuplicateResourceException;
import com.asim.books.common.exception.ResourceNotFoundException;
import com.asim.books.common.exception.OptimisticLockException;
import com.asim.books.common.model.mapper.EntityDtoMapper;
import com.asim.books.domain.user.model.dto.UserCreateDto;
import com.asim.books.domain.user.model.dto.UserRoleUpdateDto;
import com.asim.books.domain.user.model.dto.UserViewDto;
import com.asim.books.domain.user.model.entity.Role;
import com.asim.books.domain.user.model.entity.User;
import com.asim.books.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EntityDtoMapper<User, UserViewDto> userMapper;
    private final EntityManager entityManager;

    @Override
    @Transactional
    @CachePut(value = "users", key = "#result.name")
    public UserViewDto createUser(UserCreateDto userCreateDto) {
        Long userId = userCreateDto.getId();
        String userName = userCreateDto.getName();
        // Check if user with same ID already exists
        if (userId != null && userRepository.existsById(userId)) {
            throw new DuplicateResourceException("User", "id", userCreateDto.getId().toString());
        }

        // Check if user with same name already exists
        if (userRepository.existsByName(userName)) {
            throw new DuplicateResourceException("User", "name", userCreateDto.getName());
        }

        // Create and save new user with default role READER
        User user = new User();
        user.setId(userId);
        user.setName(userName);
        user.setRole(Role.EDITOR); // Default role

        user = userRepository.save(user);
        entityManager.flush();
        
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    @CachePut(value = "users", key = "#result.name")
    public UserViewDto updateUserRole(UserRoleUpdateDto userRoleUpdateDto) {
        String userName = userRoleUpdateDto.getName();
        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User", userName));

        // Check version for optimistic locking
        if (!user.getVersion().equals(userRoleUpdateDto.getVersion())) {
            throw new OptimisticLockException("User has been modified by another request. Current version: " 
                    + user.getVersion() + ", provided version: " + userRoleUpdateDto.getVersion());
        }

        user.setRole(userRoleUpdateDto.getRole());
        user = userRepository.save(user);
        entityManager.flush();
        
        return userMapper.toDto(user);
    }

    @Override
    @Cacheable(value = "users", key = "#result.name")
    public UserViewDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
                
        return userMapper.toDto(user);
    }
}
