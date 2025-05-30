package com.asim.books.infrastructure.grpc;

import com.asim.grpc.generated.NewUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSyncService {

    public void processUserCreation(NewUser user) {
        // Implement logic to handle new user data
        log.info("Processing user creation: {}", user.getName());
        // Map to domain model and save/process as needed
    }

    public void invalidateToken(String sessionId) {
        // Implement logic to invalidate token
        log.info("Invalidating token with session ID: {}", sessionId);
        // Add to blacklist or invalidate token in your system
    }
}