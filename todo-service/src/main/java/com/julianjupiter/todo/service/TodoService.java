package com.julianjupiter.todo.service;

import com.julianjupiter.todo.dto.TodoDto;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    List<TodoDto> findAll();

    Optional<TodoDto> findById(Long id);

    TodoDto save(TodoDto todoDto);

    void deleteById(long id);
}
