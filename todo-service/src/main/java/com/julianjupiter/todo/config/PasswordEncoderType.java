package com.julianjupiter.todo.config;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.function.Supplier;

public enum PasswordEncoderType {
    ARGON2("argon2", Argon2PasswordEncoder::new),
    BCRYPT("bcrypt", BCryptPasswordEncoder::new),
    PBKDF2("pbkdf2", Pbkdf2PasswordEncoder::new),
    SCRYPT("scrypt", SCryptPasswordEncoder::new);

    private final String value;
    private final Supplier<PasswordEncoder> passwordEncoderSupplier;

    PasswordEncoderType(String value, Supplier<PasswordEncoder> passwordEncoderSupplier) {
        this.value = value;
        this.passwordEncoderSupplier = passwordEncoderSupplier;
    }

    public String value() {
        return value;
    }

    public PasswordEncoder newPasswordEncoder() {
        return passwordEncoderSupplier.get();
    }

    @Override
    public String toString() {
        return value;
    }
}
