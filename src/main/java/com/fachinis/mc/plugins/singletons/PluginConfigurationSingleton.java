package com.fachinis.mc.plugins.singletons;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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

        final String authAPIURL = config.getString("auth.api.url.basepath");
        final String authAPIClientId = config.getString("auth.api.credentials.client_id");
        final String authAPIClientSecret = config.getString("auth.api.credentials.client_secret");
        if (
            authAPIURL != null && !authAPIURL.isEmpty() &&
            authAPIClientId != null && !authAPIClientId.isEmpty() &&
            authAPIClientSecret != null && !authAPIClientSecret.isEmpty()
        ) {
            loaded = true;
            loadedConfiguration = config;
        }
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public String getPropertyString(String key) {
        return this.loadedConfiguration.getString(key);
    }
}
