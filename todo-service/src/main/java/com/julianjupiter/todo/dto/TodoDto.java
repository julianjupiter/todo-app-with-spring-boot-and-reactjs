package com.julianjupiter.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class TodoDto {
    private final Long id;
    private final String description;
    @JsonProperty("status")
    private final StatusDto statusDto;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public TodoDto(Long id, String description, StatusDto statusDto, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.statusDto = statusDto;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public StatusDto getStatusDto() {
        return statusDto;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
