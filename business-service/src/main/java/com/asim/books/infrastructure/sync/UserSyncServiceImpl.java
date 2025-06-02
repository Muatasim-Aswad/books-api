package com.asim.books.infrastructure.sync;

import com.asim.books.domain.user.facade.UserFacade;
import com.asim.books.domain.user.model.dto.UserCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSyncServiceImpl implements UserSyncService {

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
    public boolean invalidateToken(String sessionId) {
        // Implement logic to invalidate token
        log.info("Invalidating token with session ID: {}", sessionId);
        // Add to blacklist or invalidate token in your system
        return true;
    }
}