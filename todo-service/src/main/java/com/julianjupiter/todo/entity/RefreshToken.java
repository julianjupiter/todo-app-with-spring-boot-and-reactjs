package com.julianjupiter.todo.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

@Entity
public class RefreshToken {
    @Id
    private String id;
    private String username;
    @Column(name = "refresh_token_key")
    @Basic(fetch = FetchType.LAZY)
    private String key;
    private Boolean revoked;

    public String getId() {
        return id;
    }

    public RefreshToken setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public RefreshToken setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getKey() {
        return key;
    }

    public RefreshToken setKey(String key) {
        this.key = key;
        return this;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public RefreshToken setRevoked(Boolean revoked) {
        this.revoked = revoked;
        return this;
    }
}
