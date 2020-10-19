package com.julianjupiter.todo.service;

import com.julianjupiter.todo.dto.TodoDto;
import com.julianjupiter.todo.mapper.TodoMapper;
import com.julianjupiter.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    TodoServiceImpl(TodoRepository todoRepository, TodoMapper todoMapper) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
    }

    @Override
    public List<TodoDto> findAll() {
        return todoRepository.findAll().stream()
                .map(todoMapper::fromEntityToDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<TodoDto> findById(Long id) {
        return todoRepository.findById(id)
                .map(todoMapper::fromEntityToDto);
    }

    @Override
    public TodoDto save(TodoDto todoDto) {
        var todo = todoMapper.fromDtoToEntity(todoDto);
        return todoMapper.fromEntityToDto(todo);
    }

    @Override
    public void deleteById(long id) {
        todoRepository.deleteById(id);
    }
}
