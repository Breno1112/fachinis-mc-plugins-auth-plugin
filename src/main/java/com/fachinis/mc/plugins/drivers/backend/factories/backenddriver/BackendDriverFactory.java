package com.fachinis.mc.plugins.drivers.backend.factories.backenddriver;

import com.fachinis.mc.plugins.domain.enums.BackendConfigurationSystem;
import com.fachinis.mc.plugins.drivers.backend.BackendDriver;
import com.fachinis.mc.plugins.drivers.backend.factories.backenddriver.concrete.ApiBackendDriver;
import com.fachinis.mc.plugins.drivers.backend.factories.backenddriver.concrete.FileBackendDriver;
import com.fachinis.mc.plugins.drivers.backend.factories.backenddriver.concrete.InMemoryDatabaseBackendDriver;
import com.fachinis.mc.plugins.drivers.backend.factories.backenddriver.concrete.RemoteDatabaseBackendDriver;
import com.fachinis.mc.plugins.singletons.PluginConfigurationSingleton;

public class BackendDriverFactory {
    
    private BackendDriverFactory() {}

    private static class BackendDriverFactoryInstanceHolder {
        private static BackendDriverFactory INSTANCE = new BackendDriverFactory();
    }

    public static BackendDriverFactory getInstance() {
        return BackendDriverFactoryInstanceHolder.INSTANCE;
    }

    private BackendDriver backendDriver;

    public void initialize() {
        final BackendConfigurationSystem backendConfigurationSystem = PluginConfigurationSingleton.getInstance().getBackendConfigurationSystem();

        switch (backendConfigurationSystem) {
            case API:
                this.backendDriver = new ApiBackendDriver();
                break;
            case REMOTE_DATABASE:
                this.backendDriver = new RemoteDatabaseBackendDriver();
                break;
            case IN_MEMORY_DATABASE:
                this.backendDriver = new InMemoryDatabaseBackendDriver();
                break;
            case FILE:
                this.backendDriver = new FileBackendDriver();
            default:
                throw new RuntimeException("No backend driver was properly configured. There seems to be a problem with your configuration!");
        }
    }

    public BackendDriver getBackendDriver() {
        return this.backendDriver;
    }
}
