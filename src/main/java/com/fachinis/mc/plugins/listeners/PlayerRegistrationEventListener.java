package com.fachinis.mc.plugins.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import com.fachinis.mc.plugins.domain.constants.PlayerMetadataKeys;
import com.fachinis.mc.plugins.domain.events.PlayerRegistrationEvent;

public class PlayerRegistrationEventListener implements Listener {

    private JavaPlugin javaPlugin;

    public PlayerRegistrationEventListener(JavaPlugin plugin) {
        this.javaPlugin = plugin;
    }
    

    @EventHandler
    public void onPlayerRegistration(PlayerRegistrationEvent event) {
        event.getPlayer().sendMessage(event.getMessage());
        if (event.isSuccessfullOperation()) {
            event.getPlayer().setMetadata(PlayerMetadataKeys.PLAYER_LOGGED_IN, new FixedMetadataValue(this.javaPlugin, true));
        } else {
            event.getPlayer().setMetadata(PlayerMetadataKeys.PLAYER_LOGGED_IN, new FixedMetadataValue(this.javaPlugin, false));
        }
    }
}
