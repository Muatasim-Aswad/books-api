package com.asim.auth.infrastructure.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String sessionId;

    public JwtAuthenticationToken(Object principal, Object credentials,
                                  Collection<? extends GrantedAuthority> authorities,
                                  String sessionId) {
        super(principal, credentials, authorities);
        this.sessionId = sessionId;
    }

    public JwtAuthenticationToken(Object principal, Object credentials, String sessionId) {
        super(principal, credentials);
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}