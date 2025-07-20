package com.fachinis.mc.plugins.drivers.backend.factories.backenddriver;

import com.fachinis.mc.plugins.clients.factories.stsclient.StsClientFactory;
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

    private void updateDriver() {
        final BackendConfigurationSystem backendConfigurationSystem = PluginConfigurationSingleton.getInstance().getBackendConfigurationSystem();
        switch (backendConfigurationSystem) {
            case API:
                this.backendDriver = new ApiBackendDriver(StsClientFactory.getInstance().getClient());
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
                this.backendDriver = null;
        }
    }

    public BackendDriver getBackendDriver() {
        this.updateDriver();
        return this.backendDriver;
    }
}
