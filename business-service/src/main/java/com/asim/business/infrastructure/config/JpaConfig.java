package com.asim.business.infrastructure.config;

import com.asim.business.domain.user.service.UserService;
import com.asim.business.infrastructure.security.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorDefaultProvider")
@RequiredArgsConstructor
public class JpaConfig {

    private final UserService userService;

    @Bean
    public AuditorAware<String> auditorDefaultProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated() &&
                    authentication instanceof JwtAuthenticationToken) {

                Object principal = authentication.getPrincipal();
                if (principal instanceof Long) {
                    var user = userService.getUser((Long) principal);

                    return Optional.of(user.getName());
                }
            }

            // Fallback to a default value or return empty if no authenticated user
            return Optional.empty(); // or Optional.of(0L) for a default system user
        };
    }
}