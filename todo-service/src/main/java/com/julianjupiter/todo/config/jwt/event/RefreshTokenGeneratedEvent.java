package com.julianjupiter.todo.config.jwt.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.userdetails.UserDetails;

public class RefreshTokenGeneratedEvent extends ApplicationEvent {

    private final UserDetails userDetails;
    private final String key;

    public RefreshTokenGeneratedEvent(Object source, UserDetails userDetails, String key) {
        super(source);
        this.userDetails = userDetails;
        this.key = key;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public String getKey() {
        return key;
    }
}

