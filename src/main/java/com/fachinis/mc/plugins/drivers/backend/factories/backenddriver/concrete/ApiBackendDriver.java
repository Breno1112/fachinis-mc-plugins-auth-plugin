package com.fachinis.mc.plugins.drivers.backend.factories.backenddriver.concrete;

import java.util.concurrent.CompletableFuture;

import com.fachinis.mc.plugins.domain.entities.AuthenticatedUser;
import com.fachinis.mc.plugins.drivers.backend.BackendDriver;

public class ApiBackendDriver implements BackendDriver {

    @Override
    public CompletableFuture<AuthenticatedUser> doRegistration(String username, String password, String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'doRegistration'");
    }
    
}
