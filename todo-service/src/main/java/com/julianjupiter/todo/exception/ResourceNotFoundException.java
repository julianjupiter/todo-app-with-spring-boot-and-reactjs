package com.julianjupiter.todo.exception;

import java.net.URI;
import java.util.List;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message, URI path) {
        super(message, path);
    }

    public ResourceNotFoundException(String message, List<String> messages, URI path) {
        super(message, messages, path);
    }

}
