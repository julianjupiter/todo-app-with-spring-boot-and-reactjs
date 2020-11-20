package com.julianjupiter.todo.config.jwt.render;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenRenderer {

    AccessRefreshToken render(Integer expiresIn, String accessToken, String refreshToken);

    AccessRefreshToken render(UserDetails userDetails, String accessToken, String refreshToken, Integer expiresIn);

}
