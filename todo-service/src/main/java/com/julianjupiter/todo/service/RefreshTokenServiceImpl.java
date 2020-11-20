package com.julianjupiter.todo.service;

import com.julianjupiter.todo.config.jwt.event.RefreshTokenGeneratedEvent;
import com.julianjupiter.todo.entity.RefreshToken;
import com.julianjupiter.todo.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Optional<RefreshToken> findByKey(String key) {
        return refreshTokenRepository.findByKey(key);
    }

    @Override
    public RefreshToken save(RefreshTokenGeneratedEvent refreshTokenGeneratedEvent) {
        if (refreshTokenGeneratedEvent != null) {
            var key = refreshTokenGeneratedEvent.getKey();
            var userDetails = refreshTokenGeneratedEvent.getUserDetails();
            if (key != null && userDetails != null) {
                var username = userDetails.getUsername();
                if (username != null && !username.isBlank()) {
                    var refreshToken = new RefreshToken()
                            .setUsername(username)
                            .setKey(key)
                            .setRevoked(Boolean.FALSE);
                    return refreshTokenRepository.save(refreshToken);
                }
            }
        }

        throw new RuntimeException("Refresh token not persisted");
    }

    @Override
    public RefreshToken update(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }
}
