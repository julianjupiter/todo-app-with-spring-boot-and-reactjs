package com.julianjupiter.todo.service;

import com.julianjupiter.todo.dto.CreateTodoDto;
import com.julianjupiter.todo.dto.TodoDto;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    List<TodoDto> findAll();

    Optional<TodoDto> findById(Long id);

    List<TodoDto> findByUser(Long id);

    List<TodoDto> findByUser(String username);

    TodoDto create(String username, CreateTodoDto createTodoDto);

    TodoDto update(TodoDto todoDto);

    void deleteById(long id);
}
