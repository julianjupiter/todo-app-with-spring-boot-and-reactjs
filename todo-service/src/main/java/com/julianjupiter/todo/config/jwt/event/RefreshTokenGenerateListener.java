package com.julianjupiter.todo.config.jwt.event;

import com.julianjupiter.todo.service.RefreshTokenService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenGenerateListener {
    private final RefreshTokenService refreshTokenService;

    public RefreshTokenGenerateListener(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Async
    @EventListener
    public void handleRefreshTokenGenerated(RefreshTokenGeneratedEvent refreshTokenGeneratedEvent) {
        refreshTokenService.save(refreshTokenGeneratedEvent);
    }
}
