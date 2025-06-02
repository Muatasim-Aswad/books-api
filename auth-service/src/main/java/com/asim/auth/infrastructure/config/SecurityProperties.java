package com.asim.auth.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.security")
@Getter
@Setter
public class SecurityProperties {

    private List<String> publicEndpoints;
    private List<String> swaggerEndpoints;

    public List<String> getAllPublicEndpoints() {
        List<String> allEndpoints = new java.util.ArrayList<>(publicEndpoints);
        allEndpoints.addAll(swaggerEndpoints);
        return allEndpoints;
    }
}