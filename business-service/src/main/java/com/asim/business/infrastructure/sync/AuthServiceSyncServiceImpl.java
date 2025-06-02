package com.asim.business.infrastructure.sync;

import com.asim.business.domain.user.facade.UserFacade;
import com.asim.business.domain.user.model.dto.UserCreateDto;
import com.asim.business.infrastructure.config.CacheConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceSyncServiceImpl implements AuthServiceSyncService {

    private final UserFacade userFacade;

    @Override
    public boolean processUserCreation(UserCreateDto user) {
        // Implement logic to handle new user data
        log.info("Processing user creation: {}", user.getName());

        try {
            var createdUser = userFacade.createUser(user);
            log.info("User created successfully: {}", createdUser.getName());

            return true;
        } catch (Exception e) {
            log.error("Error processing user creation: {}", e.getMessage());
            return false; // or handle the error as needed
        }

    }

    @Override
    @Cacheable(value = CacheConfig.REDIS_ONE_HOUR_CACHE, key = "#sessionId")
    public boolean invalidateToken(String sessionId) {
        log.info("Invalidating token with session ID: {}", sessionId);
        // The @Cacheable will store the sessionId in the "invalidated sessions" cache
        // Returning true means this session is now invalidated
        return true;
    }

}