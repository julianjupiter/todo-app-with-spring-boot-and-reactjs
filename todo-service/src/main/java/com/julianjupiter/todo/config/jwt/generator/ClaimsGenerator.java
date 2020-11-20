package com.julianjupiter.todo.config.jwt.generator;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface ClaimsGenerator {
    Map<String, Object> generate(UserDetails userDetails, Integer expiration);
}
