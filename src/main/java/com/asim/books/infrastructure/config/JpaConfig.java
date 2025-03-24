package com.asim.books.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorDefaultProvider")
public class JpaConfig {
    @Bean
    public AuditorAware<Long> auditorDefaultProvider() {
        return () -> Optional.of(123L);
    }
}