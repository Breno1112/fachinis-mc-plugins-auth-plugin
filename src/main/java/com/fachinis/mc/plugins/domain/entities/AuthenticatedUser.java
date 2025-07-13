package com.fachinis.mc.plugins.domain.entities;

public class AuthenticatedUser {
    
    private String userId;
    private String username;
    private String longTermAccessToken;

    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getLongTermAccessToken() {
        return longTermAccessToken;
    }
    public void setLongTermAccessToken(String longTermAccessToken) {
        this.longTermAccessToken = longTermAccessToken;
    }

    
}
