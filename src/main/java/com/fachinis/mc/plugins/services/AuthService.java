package com.fachinis.mc.plugins.services;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.fachinis.mc.plugins.domain.entities.AuthenticatedUser;
import com.fachinis.mc.plugins.domain.events.PlayerRegistrationEvent;
import com.fachinis.mc.plugins.drivers.backend.BackendDriver;
import com.fachinis.mc.plugins.drivers.backend.factories.backenddriver.BackendDriverFactory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class AuthService extends ServiceInterface {

    private JavaPlugin plugin;

    public AuthService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private BackendDriver backendDriver = BackendDriverFactory.getInstance().getBackendDriver();

    public void doRegistration(Player player, String email, String password) {

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            backendDriver.doRegistration(password, password, email)
                .thenAccept(data -> { 
                    Bukkit
                        .getScheduler()
                        .runTask(
                            this.plugin, 
                            this.buildSuccessRegistrationResponseTask(data, player)
                        );
                }).exceptionally(exception -> {
                    Bukkit
                        .getScheduler()
                        .runTask(
                            this.plugin, 
                            this.buildFailedRegistrationResponseTask(exception, player)
                        );
                    return null;
                });
        });
    }

    private Runnable buildSuccessRegistrationResponseTask(AuthenticatedUser data, Player player) {
        return new Runnable() {
            @Override
            public void run() {
                final PlayerRegistrationEvent event = new PlayerRegistrationEvent();
                event.setMessage(Component.text("Logged in successfully!", NamedTextColor.GREEN));
                event.setSuccessfullOperation(true);
                event.setPlayer(player);
                event.callEvent();
            }
        };
    }

    private Runnable buildFailedRegistrationResponseTask(Throwable ex, Player player) {
        return new Runnable() {
            @Override
            public void run() {
                final PlayerRegistrationEvent event = new PlayerRegistrationEvent();
                event.setMessage(Component.text("Unable to register new account. Please try again!", NamedTextColor.RED));
                event.setSuccessfullOperation(false);
                event.setPlayer(player);
                event.callEvent();
            }
        };
    }
}
