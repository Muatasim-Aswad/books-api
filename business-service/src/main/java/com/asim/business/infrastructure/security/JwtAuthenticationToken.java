package com.asim.business.infrastructure.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Custom Spring Security Authentication token for JWT-based authentication.
 * This class extends AbstractAuthenticationToken to represent an authenticated user
 * in the Spring Security context.
 * <p>
 * Key features:
 * - Stores user ID as the principal
 * - Stores session ID for session management
 * - Supports Spring Security's authority/role system
 * - Automatically marks the token as authenticated upon creation
 */
@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    // The authenticated user's identifier (typically user ID)
    private final Object principal;
    // Session identifier for tracking user sessions
    private final String sessionId;
    // User credentials (typically null for JWT tokens as they're stateless)
    private Object credentials;

    /**
     * Constructor for creating an authenticated JWT token.
     *
     * @param principal   The user identifier (typically user ID)
     * @param credentials User credentials (can be null for JWT)
     * @param authorities Collection of user authorities/roles
     * @param sessionId   Session identifier for the user
     */
    public JwtAuthenticationToken(Object principal, Object credentials,
                                  Collection<? extends GrantedAuthority> authorities,
                                  String sessionId) {
        // Call parent constructor with authorities
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.sessionId = sessionId;

        // Mark this token as authenticated since JWT validation already occurred
        setAuthenticated(true);
    }

    /**
     * Returns the user credentials.
     * For JWT tokens, this is typically null as the token itself serves as the credential.
     *
     * @return The user credentials
     */
    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    /**
     * Returns the principal (user identifier).
     * This is typically the user ID extracted from the JWT token.
     *
     * @return The user principal
     */
    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    /**
     * Erases sensitive credential information from memory.
     * This is a security best practice to prevent credential leakage.
     */
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}