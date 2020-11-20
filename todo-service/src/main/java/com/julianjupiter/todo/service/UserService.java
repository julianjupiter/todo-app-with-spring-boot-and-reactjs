package com.julianjupiter.todo.service;

import com.julianjupiter.todo.dto.CreateUserDto;
import com.julianjupiter.todo.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> findAll();

    Optional<UserDto> findById(Long id);

    UserDto create(CreateUserDto createUserDto);

    UserDto update(UserDto userDto);

    void deleteById(Long id);
}
