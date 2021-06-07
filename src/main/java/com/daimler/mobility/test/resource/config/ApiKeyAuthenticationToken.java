package com.daimler.mobility.test.resource.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;

import java.util.Collection;

@Transient
public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private String username;

    public ApiKeyAuthenticationToken(String username, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.username = username;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return new AuthenticatedPrincipal() {
            @Override
            public String getName() {
                return username;
            }
        };
    }
}
