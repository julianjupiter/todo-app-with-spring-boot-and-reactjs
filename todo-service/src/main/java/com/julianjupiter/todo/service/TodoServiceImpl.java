package com.julianjupiter.todo.service;

import com.julianjupiter.todo.dto.CreateTodoDto;
import com.julianjupiter.todo.dto.TodoDto;
import com.julianjupiter.todo.exception.ResourceNotFoundException;
import com.julianjupiter.todo.mapper.TodoMapper;
import com.julianjupiter.todo.repository.StatusRepository;
import com.julianjupiter.todo.repository.TodoRepository;
import com.julianjupiter.todo.repository.UserRepository;
import com.julianjupiter.todo.util.Applications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final TodoMapper todoMapper;

    TodoServiceImpl(TodoRepository todoRepository, UserRepository userRepository, StatusRepository statusRepository, TodoMapper todoMapper) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
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
    public List<TodoDto> findByUser(Long id) {
        return todoRepository.findByUserId(id).stream()
                .map(todoMapper::fromEntityToDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<TodoDto> findByUser(String username) {
        return todoRepository.findByUserUsername(username).stream()
                .map(todoMapper::fromEntityToDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public TodoDto create(String username, CreateTodoDto createTodoDto) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    if (createTodoDto.getCreatedAt() == null) {
                        var now = OffsetDateTime.now();
                        createTodoDto.setCreatedAt(now);
                        createTodoDto.setUpdatedAt(now);
                    }
                    var todo = todoMapper
                            .toNewEntity(createTodoDto)
                            .setUser(user);
                    statusRepository.findById(1).ifPresent(todo::setStatus);
                    var createdTodo = todoRepository.save(todo);

                    return todoMapper.fromEntityToDto(createdTodo);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist.", Applications.createUri()));
    }

    @Override
    public TodoDto update(TodoDto todoDto) {
        var todo = todoMapper.fromDtoToEntity(todoDto);
        return todoMapper.fromEntityToDto(todo);
    }

    @Override
    public void deleteById(long id) {
        todoRepository.deleteById(id);
    }
}
