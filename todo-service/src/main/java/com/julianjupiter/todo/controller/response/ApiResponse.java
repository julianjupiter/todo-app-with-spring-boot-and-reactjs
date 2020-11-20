package com.julianjupiter.todo.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse extends Response {

    private URI path;
    private Object data;

    private ApiResponse(HttpStatus httpStatus, OffsetDateTime createdAt) {
        super(httpStatus, createdAt);
    }

    public static ApiResponse of(HttpStatus httpStatus, OffsetDateTime createdAt) {
        return new ApiResponse(httpStatus, createdAt);
    }

    public URI getPath() {
        return path;
    }

    public ApiResponse setPath(URI path) {
        this.path = path;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ApiResponse setData(Object data) {
        this.data = data;
        return this;
    }
}
