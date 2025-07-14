package com.fachinis.mc.plugins.bootstrap;

import java.util.List;

import com.fachinis.mc.plugins.commands.PluginCommandInterface;
import com.fachinis.mc.plugins.commands.RegisterCommand;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class AuthPluginBootstrap implements PluginBootstrap {

    public AuthPluginBootstrap() {
        commands = List.of(
            new RegisterCommand()
        );
    }

    private ComponentLogger logger;

    private List<PluginCommandInterface> commands;

    @Override
    public void bootstrap(BootstrapContext context) {
        this.logger = context.getLogger();
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            for (PluginCommandInterface item: this.commands) {
                commands.registrar().register(item.buildCommand());
            }
        });
        this.logger.debug(Component.text("Commands registered!", NamedTextColor.BLUE));
    }
    
}
