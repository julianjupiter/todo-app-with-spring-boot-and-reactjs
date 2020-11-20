package com.julianjupiter.todo.config.jwt;

import com.julianjupiter.todo.config.jwt.render.AccessRefreshToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AccessRefreshTokenService {
    Optional<AccessRefreshToken> generate(UserDetails userDetails);

    Optional<AccessRefreshToken> generate(UserDetails userDetails, String refreshToken);

    Optional<String> generateRefreshToken(UserDetails userDetails);
}
