package com.julianjupiter.todo.config.jwt.generator;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Optional;

public interface AccessTokenGenerator {

    Optional<String> generate(UserDetails userDetails, Integer expiration);

    Optional<String> generate(Map<String, Object> claims);
}
