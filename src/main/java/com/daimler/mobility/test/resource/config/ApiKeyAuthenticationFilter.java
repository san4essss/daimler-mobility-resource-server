package com.daimler.mobility.test.resource.config;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiKeyAuthenticationFilter implements Filter {

    private static final String AUTH_METHOD = "api-key";

    private static final String API_KEY_PREFIX = "my-valid-api-key-";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        if(request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            String apiKey = getApiKey((HttpServletRequest) request);
            if (apiKey != null && apiKey.startsWith(API_KEY_PREFIX)) {
                String username = apiKey.replace(API_KEY_PREFIX, "");
                ApiKeyAuthenticationToken apiToken =
                        new ApiKeyAuthenticationToken(username, AuthorityUtils.createAuthorityList("ROLE_" + username.toUpperCase()));
                apiToken.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(apiToken);
            } else {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(401);
                httpResponse.getWriter().write("Invalid API Key");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private String getApiKey(HttpServletRequest httpRequest) {
        String apiKey = null;

        String authHeader = httpRequest.getHeader("Authorization");
        if(authHeader != null) {
            authHeader = authHeader.trim();
            if(authHeader.toLowerCase().startsWith(AUTH_METHOD + " ")) {
                apiKey = authHeader.substring(AUTH_METHOD.length()).trim();
            }
        }

        return apiKey;
    }
}