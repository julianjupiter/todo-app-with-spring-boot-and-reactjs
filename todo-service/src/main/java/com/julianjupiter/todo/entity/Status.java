package com.julianjupiter.todo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.OffsetDateTime;

@Entity
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;@Column(name = "name")
    @Enumerated(EnumType.STRING)
    private StatusType statusType;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public Status setId(Integer id) {
        this.id = id;
        return this;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public Status setStatusType(StatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public Status setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Status setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    enum StatusType {
        INCOMPLETE, COMPLETE
    }
}
