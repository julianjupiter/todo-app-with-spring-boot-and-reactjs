package com.julianjupiter.todo.config.jwt.generator;

import java.util.Optional;

public interface RefreshTokenGenerator {

    String createKey();

    Optional<String> generate(String token);

}