package com.asim.business.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class RoleHierarchyConfig {

    @Bean
    public RoleHierarchy roleHierarchy() {
        String hierarchy = """
                ROLE_ADMIN > ROLE_EDITOR
                ROLE_EDITOR > ROLE_CONTRIBUTOR
                ROLE_CONTRIBUTOR > ROLE_VIEWER
                """;
        return RoleHierarchyImpl.fromHierarchy(hierarchy);
    }
}