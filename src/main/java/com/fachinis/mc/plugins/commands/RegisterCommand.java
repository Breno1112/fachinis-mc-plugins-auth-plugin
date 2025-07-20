package com.fachinis.mc.plugins.commands;

import org.bukkit.entity.Player;

import com.fachinis.mc.plugins.services.AuthService;
import com.fachinis.mc.plugins.singletons.PluginConfigurationSingleton;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class RegisterCommand implements PluginCommandInterface {


    public RegisterCommand() {}

    public LiteralCommandNode<CommandSourceStack> buildCommand() {
        return Commands
            .literal("register")
            .requires(sender -> buildRequirements(sender))
            .then(
                Commands
                    .argument(
                        "email",
                        StringArgumentType.string()
                    )
                    .then(
                        Commands
                            .argument(
                                "password",
                                StringArgumentType.string()
                            )
                            .executes(this::runCommand)
                        )
                )
        .build();
    }

    private boolean buildRequirements(CommandSourceStack sender) {
        return PluginConfigurationSingleton.getInstance().isLoaded() &&
            sender.getSender().hasPermission("fachinis.auth.commands.register") &&
            sender.getExecutor() instanceof Player;
    }

    private int runCommand(CommandContext<CommandSourceStack> commandContext) {
        if ((commandContext.getSource().getExecutor() instanceof Player) == false) {
            return Command.SINGLE_SUCCESS;
        }
        final Player player = (Player) commandContext.getSource().getExecutor();
        player.updateCommands();
        final String email = StringArgumentType.getString(commandContext, "email");
        final String password = StringArgumentType.getString(commandContext, "password");
        player.sendMessage(Component.text("Signing you up...", NamedTextColor.GREEN));
        AuthService.getInstance().doRegistration(player, email, password);
        return Command.SINGLE_SUCCESS;
    }
}
