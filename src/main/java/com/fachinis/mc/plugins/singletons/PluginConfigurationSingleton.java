package com.fachinis.mc.plugins.singletons;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.fachinis.mc.plugins.domain.constants.PluginConfigurationKeys;
import com.fachinis.mc.plugins.domain.enums.BackendConfigurationSystem;

public class PluginConfigurationSingleton {

    private JavaPlugin pluginInstance;
    private boolean loaded = false;
    private FileConfiguration loadedConfiguration;
    
    private PluginConfigurationSingleton() {}

    private static class PluginConfigurationSingletonInstanceHolder {
        private static final PluginConfigurationSingleton INSTANCE = new PluginConfigurationSingleton();
    }

    public static PluginConfigurationSingleton getInstance() {
        return PluginConfigurationSingletonInstanceHolder.INSTANCE;
    }

    public void init(JavaPlugin plugin) {
        this.pluginInstance = plugin;
        this.loadConfigurations();
    }

    private void loadConfigurations() {

        File configFile = new File(pluginInstance.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            pluginInstance.saveDefaultConfig(); // will copy from resources/config.yml in your JAR
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.loaded = this.checkLoadedConfiguration(config);

        final String authAPIURL = config.getString("auth.api.url.basepath");
        final String authAPIClientId = config.getString("auth.api.credentials.client_id");
        final String authAPIClientSecret = config.getString("auth.api.credentials.client_secret");
        if (
            authAPIURL != null && !authAPIURL.isEmpty() &&
            authAPIClientId != null && !authAPIClientId.isEmpty() &&
            authAPIClientSecret != null && !authAPIClientSecret.isEmpty()
        ) {
            this.loaded = true;
            this.loadedConfiguration = config;
        }
    }

    private boolean checkLoadedConfiguration(FileConfiguration config) {
        boolean valid = false;
        BackendConfigurationSystem backendSystem = BackendConfigurationSystem.parse(config.getString(PluginConfigurationKeys.BACKEND_SYSTEM_CONFIGURATION_STRING)); 
        if (backendSystem == BackendConfigurationSystem.UNKNOWN) {
            config.set(PluginConfigurationKeys.BACKEND_SYSTEM_CONFIGURATION_STRING, BackendConfigurationSystem.FILE);
            backendSystem = BackendConfigurationSystem.FILE;
        }
        switch (backendSystem) {
            case API:
                valid = checkAPIConfiguration(config);
                break;
            case REMOTE_DATABASE:
                valid = checkRemoteDatabaseConfiguration(config);
                break;
            case IN_MEMORY_DATABASE:
                valid = checkInMemoryDatabaseConfiguration(config);
                break;
            case FILE:
                valid = checkFileConfiguration(config);
                break;
            default:
                break;
        }
        return valid;
    }


    private boolean checkAPIConfiguration(FileConfiguration config) {
        boolean valid = false;

        

        return valid;
    }

    private boolean checkRemoteDatabaseConfiguration(FileConfiguration config) {
        boolean valid = false;

        return valid;
    }

    private boolean checkInMemoryDatabaseConfiguration(FileConfiguration config) {
        boolean valid = false;

        return valid;
    }

    private boolean checkFileConfiguration(FileConfiguration config) {
        boolean valid = false;

        return valid;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public String getPropertyString(String key) {
        return this.loadedConfiguration.getString(key);
    }
}
