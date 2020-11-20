package com.julianjupiter.todo.dto;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

public class CreateTodoDto {
    @NotBlank(message = "{NotBlank.createTodoDto.description}")
    private final String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public CreateTodoDto(String description, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public CreateTodoDto setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public CreateTodoDto setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
