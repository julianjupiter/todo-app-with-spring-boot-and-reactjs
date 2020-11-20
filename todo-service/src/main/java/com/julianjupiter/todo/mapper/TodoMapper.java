package com.julianjupiter.todo.mapper;

import com.julianjupiter.todo.dto.CreateTodoDto;
import com.julianjupiter.todo.dto.StatusDto;
import com.julianjupiter.todo.dto.TodoDto;
import com.julianjupiter.todo.entity.Status;
import com.julianjupiter.todo.entity.Todo;
import org.springframework.stereotype.Component;

@Component
public class TodoMapper {

    public TodoDto fromEntityToDto(Todo todo) {
        var status = todo.getStatus();
        var statusDto = new StatusDto(status.getId(), status.getName());

        return new TodoDto(
                todo.getId(),
                todo.getDescription(),
                statusDto,
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }

    public Todo fromDtoToEntity(TodoDto todoDto) {
        return new Todo()
                .setId(todoDto.getId())
                .setDescription(todoDto.getDescription())
                .setCreatedAt(todoDto.getCreatedAt())
                .setUpdatedAt(todoDto.getUpdatedAt());
    }

    public Todo toNewEntity(CreateTodoDto createTodoDto) {
        return new Todo()
                .setDescription(createTodoDto.getDescription())
                .setCreatedAt(createTodoDto.getCreatedAt())
                .setUpdatedAt(createTodoDto.getUpdatedAt());
    }

}
