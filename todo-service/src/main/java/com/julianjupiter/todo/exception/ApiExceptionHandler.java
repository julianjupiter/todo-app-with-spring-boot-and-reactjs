package com.julianjupiter.todo.exception;

import com.julianjupiter.todo.controller.response.ExceptionResponse;
import com.julianjupiter.todo.util.Applications;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ExceptionResponse.of(HttpStatus.NOT_FOUND, OffsetDateTime.now(), exception.messages(), exception.path());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ExceptionResponse handleValidationException(ValidationException exception) {
        return ExceptionResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, OffsetDateTime.now(), exception.messages(), exception.path());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInvalidTokenException(InvalidTokenException exception) {
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST, OffsetDateTime.now(), exception.messages(), exception.path());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleOtherException(Exception exception) {
        exception.printStackTrace();
        return ExceptionResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, OffsetDateTime.now(), exception.getMessage(), Applications.createUri());
    }

}
