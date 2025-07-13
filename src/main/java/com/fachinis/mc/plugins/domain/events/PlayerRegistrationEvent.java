package com.fachinis.mc.plugins.domain.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

public class PlayerRegistrationEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private Component message;
    private Player player;
    private boolean successfullOperation = false;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Component getMessage() {
        return message;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isSuccessfullOperation() {
        return successfullOperation;
    }

    public void setSuccessfullOperation(boolean successfullOperation) {
        this.successfullOperation = successfullOperation;
    }

    

    

    


    
}
