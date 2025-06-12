package com.asim.business.domain.user.service;

import com.asim.business.common.exception.DuplicateResourceException;
import com.asim.business.common.exception.OptimisticLockException;
import com.asim.business.common.exception.ResourceNotFoundException;
import com.asim.business.common.model.mapper.EntityDtoMapper;
import com.asim.business.domain.user.model.dto.UserCreateDto;
import com.asim.business.domain.user.model.dto.UserRoleUpdateDto;
import com.asim.business.domain.user.model.dto.UserViewDto;
import com.asim.business.domain.user.model.entity.Role;
import com.asim.business.domain.user.model.entity.User;
import com.asim.business.domain.user.repository.UserRepository;
import com.asim.business.infrastructure.config.CacheConfigs;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = CacheConfigs.USERS)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EntityDtoMapper<User, UserViewDto> userMapper;
    private final EntityManager entityManager;
    private final CacheManager cacheManager;

    @Override
    @Transactional
    @CachePut(key = "#userCreateDto.id")
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
        user.setRole(Role.CONTRIBUTOR); // Default role

        user = userRepository.save(user);
        entityManager.flush();

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
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

        var result = userMapper.toDto(user);

        Cache cache = cacheManager.getCache(CacheConfigs.USERS);
        if (cache != null) {
            cache.put(user.getId(), result);
        }

        return result;
    }

    @Override
    @Cacheable(key = "#userId")
    public UserViewDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        return userMapper.toDto(user);
    }
}
