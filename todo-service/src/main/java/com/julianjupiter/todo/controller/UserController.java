package com.julianjupiter.todo.controller;

import com.julianjupiter.todo.controller.response.ApiResponse;
import com.julianjupiter.todo.dto.CreateUserDto;
import com.julianjupiter.todo.dto.UserDto;
import com.julianjupiter.todo.exception.ResourceNotFoundException;
import com.julianjupiter.todo.exception.ValidationException;
import com.julianjupiter.todo.service.UserService;
import com.julianjupiter.todo.util.Applications;
import com.julianjupiter.todo.util.MessageSourceProperties;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.Locale;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final MessageSource messageSource;
    private final UserService userService;

    public UserController(MessageSource messageSource, UserService userService) {
        this.messageSource = messageSource;
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse findAll() {
        return ApiResponse.of(HttpStatus.OK, OffsetDateTime.now())
                .setPath(Applications.createUri())
                .setData(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody @Valid CreateUserDto createUserDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var messages = Applications.validationErrors(bindingResult, messageSource);
            throw new ValidationException("Invalid data", messages, Applications.createUri());
        }

        var createdUser = userService.create(createUserDto);
        var path = Applications.createUri("/{id}", createdUser.getId());

        return ResponseEntity.created(path)
                .body(ApiResponse.of(HttpStatus.CREATED, OffsetDateTime.now())
                        .setPath(path)
                        .setData(createdUser));
    }

    @GetMapping("/{id")
    public ApiResponse findById(@PathVariable Long id) {
        return userService.findById(id)
                .map(userDto -> ApiResponse.of(HttpStatus.OK, OffsetDateTime.now())
                        .setPath(Applications.createUri())
                        .setData(userDto)
                )
                .orElseThrow(() -> {
                    var params = new Long[]{id};
                    var message = messageSource.getMessage(MessageSourceProperties.USER_NOT_FOUND.toString(), params, Locale.ENGLISH);
                    return new ResourceNotFoundException(message, Applications.createUri());
                });
    }

    @PutMapping("/{id}")
    public ApiResponse update(@PathVariable Long id, @RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var messages = Applications.validationErrors(bindingResult, messageSource);
            throw new ValidationException("Invalid data", messages, Applications.createUri());
        }

        return userService.findById(id)
                .map(u -> ApiResponse.of(HttpStatus.OK, OffsetDateTime.now())
                        .setPath(Applications.createUri())
                        .setData(userService.update(userDto))
                )
                .orElseThrow(() -> {
                    var params = new Long[]{id};
                    var message = messageSource.getMessage(MessageSourceProperties.USER_NOT_FOUND.toString(), params, Locale.ENGLISH);
                    return new ResourceNotFoundException(message, Applications.createUri());
                });
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteById(@PathVariable Long id) {
        return userService.findById(id)
                .map(userDto -> {
                    userService.deleteById(id);
                    return ApiResponse.of(HttpStatus.OK, OffsetDateTime.now())
                            .setPath(Applications.createUri());
                })
                .orElseThrow(() -> {
                    var params = new Long[]{id};
                    var message = messageSource.getMessage(MessageSourceProperties.USER_NOT_FOUND.toString(), params, Locale.ENGLISH);
                    return new ResourceNotFoundException(message, Applications.createUri());
                });
    }
}
