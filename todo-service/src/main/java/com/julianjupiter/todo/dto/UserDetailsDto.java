package com.julianjupiter.todo.dto;

import java.time.OffsetDateTime;

public class UserDetailsDto {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String mobileNumber;
    private final String emailAddress;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public UserDetailsDto(Long id,
                          String firstName,
                          String lastName,
                          String mobileNumber,
                          String emailAddress,
                          OffsetDateTime createdAt,
                          OffsetDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
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

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
