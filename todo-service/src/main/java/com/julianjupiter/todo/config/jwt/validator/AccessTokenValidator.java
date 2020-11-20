package com.julianjupiter.todo.config.jwt.validator;

import com.nimbusds.jwt.JWT;

import java.util.Optional;

public interface AccessTokenValidator {
    Optional<JWT> validate(String token);
}
