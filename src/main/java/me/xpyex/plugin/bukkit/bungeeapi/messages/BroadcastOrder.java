package me.xpyex.plugin.bukkit.bungeeapi.messages;

import me.xpyex.plugin.bukkit.bungeeapi.BungeeAPI;

public class BroadcastOrder extends BungeeMessage {
    public BroadcastOrder(String serverName, String... messages) {
        super(serverName, BungeeAPI.BROADCAST_TITLE, messages);
    }
}
