package com.julianjupiter.todo.service;

import com.julianjupiter.todo.config.jwt.event.RefreshTokenGeneratedEvent;
import com.julianjupiter.todo.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByKey(String refreshToken);

    RefreshToken save(RefreshTokenGeneratedEvent refreshTokenGeneratedEvent);

    RefreshToken update(RefreshToken refreshToken);
}
