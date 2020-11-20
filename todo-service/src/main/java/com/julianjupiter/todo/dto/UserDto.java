package com.julianjupiter.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class UserDto {
    private final Long id;
    private final String username;
    private final String password;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    @JsonProperty("userDetails")
    private UserDetailsDto userDetailsDto;

    public UserDto(Long id,
                   String username,
                   String password, OffsetDateTime createdAt,
                   OffsetDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UserDetailsDto getUserDetailsDto() {
        return userDetailsDto;
    }

    public UserDto setUserDetailsDto(UserDetailsDto userDetailsDto) {
        this.userDetailsDto = userDetailsDto;
        return this;
    }
}
