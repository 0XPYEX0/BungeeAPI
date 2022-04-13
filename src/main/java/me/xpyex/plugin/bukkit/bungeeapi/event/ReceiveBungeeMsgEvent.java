package me.xpyex.plugin.bukkit.bungeeapi.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 当服务端收到来自BungeeCord信息时，将触发这个事件
 */
public class ReceiveBungeeMsgEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final String message;

    public ReceiveBungeeMsgEvent(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
