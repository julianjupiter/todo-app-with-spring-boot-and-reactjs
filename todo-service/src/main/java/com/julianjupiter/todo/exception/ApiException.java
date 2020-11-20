package com.julianjupiter.todo.exception;

import java.net.URI;
import java.util.List;

public class ApiException extends RuntimeException {
    private final List<String> messages;
    private final URI path;

    public ApiException(String message, URI path) {
        super(message);
        this.messages = List.of(message);
        this.path = path;
    }

    public ApiException(String message, List<String> messages, URI path) {
        super(message);
        this.messages = List.copyOf(messages);
        this.path = path;
    }

    public List<String> messages() {
        return messages;
    }

    public URI path() {
        return path;
    }
}
