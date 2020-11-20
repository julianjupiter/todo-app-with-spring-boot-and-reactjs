package com.julianjupiter.todo.config.jwt.render;

import java.util.Collection;

public class BearerAccessRefreshToken extends AccessRefreshToken {
    private final String username;
    private final Collection<String> roles;

    public BearerAccessRefreshToken(String username,
                                    Collection<String> roles,
                                    String accessToken,
                                    String refreshToken,
                                    Integer expiresIn,
                                    String tokenType) {
        super(accessToken, refreshToken, tokenType, expiresIn);
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public Collection<String> getRoles() {
        return roles;
    }

}
