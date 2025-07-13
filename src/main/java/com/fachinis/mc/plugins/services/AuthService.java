package com.fachinis.mc.plugins.services;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.fachinis.mc.plugins.clients.AuthClient;
import com.fachinis.mc.plugins.domain.events.PlayerRegistrationEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class AuthService extends ServiceInterface {

    private JavaPlugin plugin;

    public AuthService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private AuthClient authClient;

    public void doRegistration(Player player, String email, String password) {

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            reloadAuthClient();
            authClient
                .doRegistration(password, password, email)
                .thenAccept(data -> { 
                    Bukkit
                        .getScheduler()
                        .runTask(this.plugin, () -> {
                            final PlayerRegistrationEvent event = new PlayerRegistrationEvent();
                            event.setMessage(Component.text("Logged in successfully!", NamedTextColor.GREEN));
                            event.setSuccessfullOperation(true);
                            event.setPlayer(player);
                            event.callEvent();
                        });
                }).exceptionally(exception -> {
                    Bukkit.getScheduler().runTask(this.plugin, () -> {
                        final PlayerRegistrationEvent event = new PlayerRegistrationEvent();
                        event.setMessage(Component.text("Unable to register new account. Please try again!", NamedTextColor.RED));
                        event.setSuccessfullOperation(false);
                        event.setPlayer(player);
                        event.callEvent();
                    });
                    return null;
                });
        });
    }


    private void reloadAuthClient() {
        if (this.authClient == null) {
            this.authClient = InjectorService.getInstance().inject(AuthClient.class);
        }
    }
}
