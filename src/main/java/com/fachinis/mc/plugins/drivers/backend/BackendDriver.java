package com.fachinis.mc.plugins.drivers.backend;

import java.util.concurrent.CompletableFuture;

import com.fachinis.mc.plugins.domain.entities.AuthenticatedUser;

public interface BackendDriver {
    

    public CompletableFuture<AuthenticatedUser> doRegistration(String username, String password, String email);
}
