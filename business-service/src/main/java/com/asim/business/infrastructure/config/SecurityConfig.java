package com.asim.business.infrastructure.config;

import com.asim.business.infrastructure.security.CustomAccessDeniedHandler;
import com.asim.business.infrastructure.security.CustomAuthenticationEntryPoint;
import com.asim.business.infrastructure.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security configuration for JWT-based authentication.
 * This configuration sets up a stateless security architecture suitable for REST APIs.
 * <p>
 * Key features:
 * - JWT-based authentication (stateless)
 * - CORS support for cross-origin requests
 * - Public endpoint configuration
 * - Custom authentication error handling
 * - Integration with existing JWT validation logic
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final RoleHierarchy roleHierarchy;

    private final List<String> docsEndpoints = List.of(
            "/swagger-ui/**",
            "/api-docs/**"
    );

    private final List<String> resourcesEndpoints = List.of(
            "/api/v1/authors/*",
            "/api/v1/books/*",
            "/api/v1/authors",
            "/api/v1/books"
    );

    private final List<String> adminEndpoints = List.of(
            "/api/v1/users/role"
    );

    // Configuration properties for security settings

    /**
     * Configures the main security filter chain for the application.
     * This method defines how Spring Security should handle different types of requests.
     *
     * @param http HttpSecurity configuration builder
     * @return Configured SecurityFilterChain
     * @throws Exception If configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF protection (not needed for stateless JWT APIs)
                .csrf(AbstractHttpConfigurer::disable)

                // Enable CORS with custom configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configure session management to be stateless
                // No server-side sessions will be created or used
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configure custom authentication error handling
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))

                // Configure authorization rules for different endpoints
                .authorizeHttpRequests(auth -> {
                    // Manage access
                    docsEndpoints.forEach(path ->
                            auth.requestMatchers(path).permitAll());

                    resourcesEndpoints.forEach(path ->
                            auth.requestMatchers(HttpMethod.GET, path).permitAll()
                                    .requestMatchers(HttpMethod.POST, path).hasRole("CONTRIBUTOR")
                                    .requestMatchers(HttpMethod.PUT, path).hasRole("EDITOR")
                                    .requestMatchers(HttpMethod.PATCH, path).hasRole("EDITOR")
                                    .requestMatchers(HttpMethod.DELETE, path).hasRole("EDITOR")
                    );

                    adminEndpoints.forEach(path ->
                            auth.requestMatchers(HttpMethod.POST, path).hasRole("ADMIN"));

                    // Guard for all other endpoints
                    // This ensures that any request not explicitly allowed above requires admin privileges
                    auth.anyRequest().hasRole("ADMIN");
                })

                // Add custom JWT filter before the standard username/password filter
                // This ensures JWT processing happens first in the filter chain
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }


    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings.
     * This allows the API to be accessed from web applications running on different domains.
     *
     * @return Configured CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow requests from any origin (configure more restrictively in production)
        configuration.setAllowedOrigins(List.of("*"));

        // Allow common HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow all headers in requests
        configuration.setAllowedHeaders(List.of("*"));

        // Expose these headers to the client
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Apply CORS configuration to all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}