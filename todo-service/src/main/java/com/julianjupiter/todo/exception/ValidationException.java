package com.julianjupiter.todo.exception;

import java.net.URI;
import java.util.List;

public class ValidationException extends ApiException {

    public ValidationException(String message, URI path) {
        super(message, path);
    }

    public ValidationException(String message, List<String> messages, URI path) {
        super(message, messages, path);
    }

}
