package com.fachinis.mc.plugins.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import com.fachinis.mc.plugins.domain.constants.PlayerMetadataKeys;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PlayerJoinListener implements Listener {

    private final JavaPlugin pluginRef;

    public PlayerJoinListener(JavaPlugin mainRef) {
        this.pluginRef = mainRef;
    }
    

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Welcome to the server!");
        // the code below is for testing permissions
        event.getPlayer().addAttachment(pluginRef, "fachinis.auth.commands.register", true);

        final Runnable logoutPlayerTask = buildLogoutCheckTask(event.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskLater(pluginRef, logoutPlayerTask, 20 * 60);
    }

    private Runnable buildLogoutCheckTask(UUID playerId) {
        return new Runnable() {
            @Override
            public void run() {
                final Player player = Bukkit.getPlayer(playerId);
                if (player == null || !player.isOnline()) {
                    return;
                }

                boolean shouldKickPlayer = true;

                final List<MetadataValue> values = player.getMetadata(PlayerMetadataKeys.PLAYER_LOGGED_IN);
                for (MetadataValue item: values) {
                    if (item.getOwningPlugin().equals(pluginRef)) {
                        final boolean playerLoggedIn = item.asBoolean();
                        shouldKickPlayer = !playerLoggedIn;
                        break;
                    }
                }
                if (shouldKickPlayer) {
                    player.kick(Component.text("You have been kicked out because you took too long to login", NamedTextColor.RED));                
                } else {
                    player.sendMessage("Cancelling task because player logged in");
                }
            }
        };
    }
}
