package com.fachinis.mc.plugins;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.fachinis.mc.plugins.clients.AuthClient;
import com.fachinis.mc.plugins.drivers.backend.factories.backenddriver.BackendDriverFactory;
import com.fachinis.mc.plugins.listeners.PlayerJoinListener;
import com.fachinis.mc.plugins.listeners.PlayerRegistrationEventListener;
import com.fachinis.mc.plugins.services.AuthService;
import com.fachinis.mc.plugins.services.InjectorService;
import com.fachinis.mc.plugins.singletons.PluginConfigurationSingleton;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        InjectorService.getInstance().registerMultipleComponents(List.of(
            new AuthClient(),
            new AuthService(this)
        ));
    }

    @Override
    public void onEnable() {
        PluginConfigurationSingleton.getInstance().init(this);
        if (!PluginConfigurationSingleton.getInstance().isLoaded()) {
            getComponentLogger().warn(Component.text(String.format("Fachini's Auth Plugin could not be enabled due to missing configurations! If it is the first time you are loading this plugin, please refer to the setup documentation here: https://plugins.mc.fachinis.com/auth-plugin/docs/setup\nThe property %s was not configured properly!", PluginConfigurationSingleton.getInstance().getMissingConfigurationProperty()), NamedTextColor.YELLOW));
            return;
        }
        BackendDriverFactory.getInstance().initialize();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRegistrationEventListener(this), this);
        getComponentLogger().info(Component.text("Fachini's Auth Plugin has been enabled!", NamedTextColor.GREEN));
    }

    @Override
    public void onDisable() {
        getComponentLogger().info(Component.text("Fachini's Auth Plugin has been disabled.", NamedTextColor.GRAY));
    }
}