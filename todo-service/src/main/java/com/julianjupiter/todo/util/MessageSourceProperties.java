package com.julianjupiter.todo.util;

public enum MessageSourceProperties {
    INVALID_USERNAME_OR_PASSWORD("invalid.username.or.password"),
    USER_NOT_FOUND("user.not.found"),
    ROLE_NOT_FOUND("role.not.found"),
    TODO_NOT_FOUND("todo.not.found");

    private final String value;

    MessageSourceProperties(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
