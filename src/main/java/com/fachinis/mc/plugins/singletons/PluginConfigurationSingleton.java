package com.fachinis.mc.plugins.singletons;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.fachinis.mc.plugins.domain.constants.PluginConfigurationKeys;
import com.fachinis.mc.plugins.domain.enums.ApiStsAuthenticationType;
import com.fachinis.mc.plugins.domain.enums.BackendConfigurationSystem;

public class PluginConfigurationSingleton {

    private JavaPlugin pluginInstance;
    private boolean loaded = false;
    private FileConfiguration loadedConfiguration;
    private String missingConfigurationProperty;
    
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
    }

    private boolean checkLoadedConfiguration(FileConfiguration config) {
        boolean valid = false;
        BackendConfigurationSystem backendSystem = BackendConfigurationSystem.parse(config.getString(PluginConfigurationKeys.BACKEND_SYSTEM_CONFIGURATION_STRING)); 
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
            case UNKNOWN:
                config.set(PluginConfigurationKeys.BACKEND_SYSTEM_CONFIGURATION_STRING, BackendConfigurationSystem.FILE);
                backendSystem = BackendConfigurationSystem.FILE;
                valid = checkFileConfiguration(config);
            default:
                break;
        }
        return valid;
    }


    private boolean checkAPIConfiguration(FileConfiguration config) {
        final String basePath = config.getString(PluginConfigurationKeys.BACKEND_API_URL_BASE_PATH);

        if (basePath == null || basePath.isEmpty() || basePath.isBlank()) {
            this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_API_URL_BASE_PATH);
            return false;
        }

        final ApiStsAuthenticationType authenticationType = ApiStsAuthenticationType.parse(config.getString(PluginConfigurationKeys.BACKEND_API_STS_AUTHENTICATION_TYPE));

        switch (authenticationType) {
            case CLIENT_CREDENTIALS:
                if(!validApiClientCredentialsConfiguration(config)) {
                    return false;
                }
                break;
            case API_KEY:
                if (!validApiKeyConfiguration(config)) {
                    return false;
                }
                break;
            case UNKNOWN:
                config.set(PluginConfigurationKeys.BACKEND_API_STS_AUTHENTICATION_TYPE, ApiStsAuthenticationType.NONE.getPropertyValue());
                return false;
            default:
                break;
        }

        if (!validApiPathsConfiguration(config)) {
            return false;
        }

        return true;
    }

    private boolean validApiClientCredentialsConfiguration(FileConfiguration config) {
        final String clientId = config.getString(PluginConfigurationKeys.BACKEND_API_STS_CLIENT_CREDENTIALS_CLIENT_ID);
        if (clientId == null || clientId.isEmpty() || clientId.isBlank()) {
            this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_API_STS_CLIENT_CREDENTIALS_CLIENT_ID);
            return false;
        }

        final String clientSecret = config.getString(PluginConfigurationKeys.BACKEND_API_STS_CLIENT_CREDENTIALS_CLIENT_SECRET);
        if (clientSecret == null || clientSecret.isEmpty() || clientSecret.isBlank()) {
            this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_API_STS_CLIENT_CREDENTIALS_CLIENT_SECRET);
            return false;
        }
        return true;
    }

    private boolean validApiKeyConfiguration(FileConfiguration config) {
        final String apiKey = config.getString(PluginConfigurationKeys.BACKEND_API_STS_API_KEY);
        if (apiKey == null || apiKey.isEmpty() || apiKey.isBlank()) {
            this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_API_STS_API_KEY);
            return false;
        }
        return true;
    }

    private boolean validApiPathsConfiguration(FileConfiguration config) {
        final ArrayList<String> validPaths = new ArrayList<>();
        final String registerPath = config.getString(PluginConfigurationKeys.BACKEND_API_URL_REGISTER_PATH);
        if (registerPath == null || registerPath.isEmpty() || registerPath.isBlank()) {
            this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_API_URL_REGISTER_PATH);
            return false;
        }
        validPaths.add(registerPath);

        final String loginPath = config.getString(PluginConfigurationKeys.BACKEND_API_URL_LOGIN_PATH);
        if (loginPath == null || loginPath.isEmpty() || loginPath.isBlank() || validPaths.contains(loginPath)) {
            this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_API_URL_LOGIN_PATH);
            return false;
        }
        validPaths.add(loginPath);

        final boolean loginHistoryFeatureEnabled = config.getBoolean(PluginConfigurationKeys.FEATURES_LOGIN_HISTORY);
        if (loginHistoryFeatureEnabled) {
            final String loginHistoryPath = config.getString(PluginConfigurationKeys.BACKEND_API_URL_SAVE_LOGIN_HISTORY_PATH);
            if (loginHistoryPath == null || loginHistoryPath.isEmpty() || loginHistoryPath.isBlank() || validPaths.contains(loginHistoryPath)) {
                this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_API_URL_SAVE_LOGIN_HISTORY_PATH);
                return false;
            }
            validPaths.add(loginHistoryPath);
        }
        return true;
    }

    private boolean checkRemoteDatabaseConfiguration(FileConfiguration config) {
        final String url = config.getString(PluginConfigurationKeys.BACKEND_REMOTE_DATABASE_CONNECTION_URL);
        if (url == null || url.isEmpty() || url.isBlank()) {
            this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_REMOTE_DATABASE_CONNECTION_URL);
            return false;
        }

        final String database = config.getString(PluginConfigurationKeys.BACKEND_REMOTE_DATABASE_CONNECTION_DATABASE);
        if (database == null || database.isEmpty() || database.isBlank()) {
            this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_REMOTE_DATABASE_CONNECTION_DATABASE);
            return false;
        }

        final String usersTable = config.getString(PluginConfigurationKeys.BACKEND_REMOTE_DATABASE_TABLES_USERS_TABLE);
        if (usersTable == null || usersTable.isEmpty() || usersTable.isBlank()) {
            this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_REMOTE_DATABASE_TABLES_USERS_TABLE);
            return false;
        }

        final boolean loginHistoryFeatureEnabled = config.getBoolean(PluginConfigurationKeys.FEATURES_LOGIN_HISTORY);
        if (loginHistoryFeatureEnabled) {
            final String logisHistoryTable = config.getString(PluginConfigurationKeys.BACKEND_REMOTE_DATABASE_TABLES_LOGIN_HISTORY_TABLE);
            if (logisHistoryTable == null || logisHistoryTable.isEmpty() || logisHistoryTable.isBlank()) {
                this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_REMOTE_DATABASE_TABLES_LOGIN_HISTORY_TABLE);
                return false;
            }   
        }
        return true;
    }

    private boolean checkInMemoryDatabaseConfiguration(FileConfiguration config) {
        final String usersTable = config.getString(PluginConfigurationKeys.BACKEND_IN_MEMORY_DATABASE_TABLES_USERS_TABLE);
        if (usersTable == null || usersTable.isEmpty() || usersTable.isBlank()) {
            this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_IN_MEMORY_DATABASE_TABLES_USERS_TABLE);
            return false;
        }

        final boolean loginHistoryFeatureEnabled = config.getBoolean(PluginConfigurationKeys.FEATURES_LOGIN_HISTORY);
        if (loginHistoryFeatureEnabled) {
            final String logisHistoryTable = config.getString(PluginConfigurationKeys.BACKEND_IN_MEMORY_DATABASE_TABLES_LOGIN_HISTORY_TABLE);
            if (logisHistoryTable == null || logisHistoryTable.isEmpty() || logisHistoryTable.isBlank()) {
                this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_IN_MEMORY_DATABASE_TABLES_LOGIN_HISTORY_TABLE);
                return false;
            }  
        }
        return true;
    }

    private boolean checkFileConfiguration(FileConfiguration config) {
        final String fileName = config.getString(PluginConfigurationKeys.BACKEND_FILE_FILE_NAME);
        if (fileName == null || fileName.isEmpty() || fileName.isBlank()) {
            this.setMissingConfurationProperty(PluginConfigurationKeys.BACKEND_FILE_FILE_NAME);
            return false;
        }
        return true;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public String getPropertyString(String key) {
        return this.loadedConfiguration.getString(key);
    }

    public BackendConfigurationSystem getBackendConfigurationSystem() {
        return BackendConfigurationSystem.parse(this.loadedConfiguration.getString(PluginConfigurationKeys.BACKEND_SYSTEM_CONFIGURATION_STRING));
    }

    public String getMissingConfigurationProperty() {
        return this.missingConfigurationProperty;
    }

    private void setMissingConfurationProperty(String key) {
        this.missingConfigurationProperty = key;
    }

    public ApiStsAuthenticationType getApiStsConfigurationType() {
        return ApiStsAuthenticationType.parse(this.loadedConfiguration.getString(PluginConfigurationKeys.BACKEND_API_STS_AUTHENTICATION_TYPE));
    }
}
