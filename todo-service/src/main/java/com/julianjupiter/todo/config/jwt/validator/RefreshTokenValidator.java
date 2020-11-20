package com.julianjupiter.todo.config.jwt.validator;

import java.util.Optional;

public interface RefreshTokenValidator {
    Optional<String> validate(String token);
}
