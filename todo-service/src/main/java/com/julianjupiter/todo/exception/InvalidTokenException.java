package com.julianjupiter.todo.exception;

import java.net.URI;

public class InvalidTokenException extends ApiException {

    public InvalidTokenException(String message, URI path) {
        super(message, path);
    }

}
