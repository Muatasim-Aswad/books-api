package com.asim.auth.infrastructure.config;

import com.asim.auth.common.exception.DuplicateResourceException;
import com.asim.auth.core.model.dto.UserInput;
import com.asim.auth.core.model.dto.UserPublic;
import com.asim.auth.core.service.UserService.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder {
    private final UserService userService;

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Value("${app.admin.username:}")
    private String adminUsername;

    @Value("${app.admin.password:}")
    private String adminPassword;



    @Bean
    public CommandLineRunner seedDatabase() {
        return args -> seedAdminUser();
    }

    private void seedAdminUser() {
        if (activeProfile.equals("production") || activeProfile.equals("prod")) {
            log.warn("Creating admin user in production environment. Be cautious!");
        }

        if (adminUsername.isEmpty() || adminPassword.isEmpty()) {
            log.warn("Admin username or password not configured, skipping admin user creation.");
            return;
        }

        try {
            UserInput adminInput = new UserInput();
            adminInput.setName(adminUsername);
            adminInput.setPassword(adminPassword);

            UserPublic createdUser = userService.registerUser(adminInput);

            log.info("Admin user created successfully at: {}", createdUser.getCreatedAt());
        } catch (DuplicateResourceException e) {
            log.info("Admin user already exists, skipping creation: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Failed to create admin user", e);
        }
    }
}