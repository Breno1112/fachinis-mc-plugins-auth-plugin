package com.fachinis.mc.plugins.domain.entities;

public class UserEntity {
    
    private String id;

    private String username;

    private UserCredentials credentials;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(UserCredentials credentials) {
        this.credentials = credentials;
    }

    
}
