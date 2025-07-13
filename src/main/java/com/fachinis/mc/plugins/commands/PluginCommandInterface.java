package com.fachinis.mc.plugins.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface PluginCommandInterface {

    LiteralCommandNode<CommandSourceStack> buildCommand();
}
