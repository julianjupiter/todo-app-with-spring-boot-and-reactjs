package com.julianjupiter.todo.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

public class Response {
    private final int status;
    private final String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final OffsetDateTime createdAt;

    public Response(HttpStatus httpStatus, OffsetDateTime createdAt) {
        this.status = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
