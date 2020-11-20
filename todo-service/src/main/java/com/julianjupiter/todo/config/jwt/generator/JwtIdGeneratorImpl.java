package com.julianjupiter.todo.config.jwt.generator;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class JwtIdGeneratorImpl implements JwtIdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
