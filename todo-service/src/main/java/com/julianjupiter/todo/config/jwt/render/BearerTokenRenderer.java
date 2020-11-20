package com.julianjupiter.todo.config.jwt.render;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BearerTokenRenderer implements TokenRenderer {

    public static final String BEARER_TOKEN_TYPE = "Bearer";

    @Override
    public AccessRefreshToken render(Integer expiresIn, String accessToken, String refreshToken) {
        return new AccessRefreshToken(accessToken, refreshToken, BEARER_TOKEN_TYPE, expiresIn);
    }

    @Override
    public AccessRefreshToken render(UserDetails userDetails, String accessToken, String refreshToken, Integer expiresIn) {
        var username = userDetails.getUsername();
        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableList());

        return new BearerAccessRefreshToken(username, roles, accessToken, refreshToken, expiresIn, BEARER_TOKEN_TYPE);
    }
}
