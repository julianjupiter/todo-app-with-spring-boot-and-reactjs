package com.julianjupiter.todo.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ExceptionResponse extends Response {

    private final URI path;
    @JsonProperty("errors")
    private final List<Message> messages;

    private ExceptionResponse(HttpStatus httpStatus, OffsetDateTime createdAt, List<String> messages, URI path) {
        super(httpStatus, createdAt);
        this.path = path;
        this.messages = messages.stream()
                .map(Message::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public static ExceptionResponse of(HttpStatus httpStatus, OffsetDateTime createdAt, String message, URI path) {
        return new ExceptionResponse(httpStatus, createdAt, List.of(message), path);
    }

    public static ExceptionResponse of(HttpStatus httpStatus, OffsetDateTime createdAt, List<String> messages, URI path) {
        return new ExceptionResponse(httpStatus, createdAt, messages, path);
    }

    public URI getPath() {
        return path;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
