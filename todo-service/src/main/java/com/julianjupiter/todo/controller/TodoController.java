package com.julianjupiter.todo.controller;

import com.julianjupiter.todo.controller.response.ApiResponse;
import com.julianjupiter.todo.dto.CreateTodoDto;
import com.julianjupiter.todo.dto.TodoDto;
import com.julianjupiter.todo.dto.UserDto;
import com.julianjupiter.todo.exception.ResourceNotFoundException;
import com.julianjupiter.todo.exception.ValidationException;
import com.julianjupiter.todo.service.TodoService;
import com.julianjupiter.todo.util.Applications;
import com.julianjupiter.todo.util.MessageSourceProperties;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.Valid;
import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.Locale;

@RestControllerAdvice
@RequestMapping("/v1/todos")
public class TodoController {
    private final TodoService todoService;
    private final MessageSource messageSource;

    public TodoController(TodoService todoService, MessageSource messageSource) {
        this.todoService = todoService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ApiResponse findAll() {
        var authenticatedUsername = Applications.getUsername();

        return ApiResponse.of(HttpStatus.OK, OffsetDateTime.now())
                .setPath(Applications.createUri())
                .setData(todoService.findByUser(authenticatedUsername));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody @Valid CreateTodoDto createTodoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var messages = Applications.validationErrors(bindingResult, messageSource);
            throw new ValidationException("Invalid data", messages, Applications.createUri());
        }

        var authenticatedUsername = Applications.getUsername();
        var createdTodo = todoService.create(authenticatedUsername, createTodoDto);
        var path = Applications.createUri("/{id}", createdTodo.getId());

        return ResponseEntity.created(path)
                .body(ApiResponse.of(HttpStatus.CREATED, OffsetDateTime.now())
                        .setPath(path)
                        .setData(createdTodo));
    }

    @GetMapping("/{id")
    public ApiResponse findById(@PathVariable Long id) {
        return todoService.findById(id)
                .map(userDto -> ApiResponse.of(HttpStatus.OK, OffsetDateTime.now())
                        .setPath(Applications.createUri())
                        .setData(userDto)
                )
                .orElseThrow(() -> {
                    var params = new Long[]{id};
                    var message = messageSource.getMessage(MessageSourceProperties.TODO_NOT_FOUND.toString(), params, Locale.ENGLISH);
                    return new ResourceNotFoundException(message, Applications.createUri());
                });
    }

    @PutMapping("/{id}")
    public ApiResponse update(@PathVariable Long id, @RequestBody @Valid TodoDto todoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var messages = Applications.validationErrors(bindingResult, messageSource);
            throw new ValidationException("Invalid data", messages, Applications.createUri());
        }

        return todoService.findById(id)
                .map(u -> ApiResponse.of(HttpStatus.OK, OffsetDateTime.now())
                        .setPath(Applications.createUri())
                        .setData(todoService.update(todoDto))
                )
                .orElseThrow(() -> {
                    var params = new Long[]{id};
                    var message = messageSource.getMessage(MessageSourceProperties.TODO_NOT_FOUND.toString(), params, Locale.ENGLISH);
                    return new ResourceNotFoundException(message, Applications.createUri());
                });
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteById(@PathVariable Long id) {
        return todoService.findById(id)
                .map(userDto -> {
                    todoService.deleteById(id);
                    return ApiResponse.of(HttpStatus.OK, OffsetDateTime.now())
                            .setPath(Applications.createUri());
                })
                .orElseThrow(() -> {
                    var params = new Long[]{id};
                    var message = messageSource.getMessage(MessageSourceProperties.TODO_NOT_FOUND.toString(), params, Locale.ENGLISH);
                    return new ResourceNotFoundException(message, Applications.createUri());
                });
    }
}
