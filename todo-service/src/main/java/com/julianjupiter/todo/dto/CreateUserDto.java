package com.julianjupiter.todo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.OffsetDateTime;

public class CreateUserDto {
    @NotBlank(message = "{NotBlank.createUserDto.username}")
    @Pattern(regexp = "^[a-z0-9_-]{3,15}$", message = "{createUserDto.username.Pattern}")
    private final String username;
    @NotBlank(message = "{NotBlank.createUserDto.password}")
    private String password;
    @NotNull(message = "{NotNull.createUserDto.roleId}")
    @Min(value = 1, message = "{Min.createUserDto.roleId}")
    @Max(value = 2, message = "{Max.createUserDto.roleId}")
    private final Integer roleId;
    @NotBlank(message = "{NotBlank.createUserDto.firstName}")
    private final String firstName;
    @NotBlank(message = "{NotBlank.createUserDto.lastName}")
    private final String lastName;
    @NotBlank(message = "{NotBlank.createUserDto.mobileNumber}")
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$", message = "{Pattern.createUserDto.mobileNumber}")
    private final String mobileNumber;
    @NotBlank(message = "{NotBlank.createUserDto.emailAddress}")
    @Email(regexp = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+", message = "{Email.createUserDto.emailAddress}")
    private final String emailAddress;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public CreateUserDto(String username,
                         String password,
                         Integer roleId,
                         String firstName,
                         String lastName,
                         String mobileNumber,
                         String emailAddress,
                         OffsetDateTime createdAt,
                         OffsetDateTime updatedAt) {
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
        this.createdAt =createdAt;
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public CreateUserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public CreateUserDto setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public CreateUserDto setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
