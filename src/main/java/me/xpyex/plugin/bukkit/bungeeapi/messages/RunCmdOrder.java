package me.xpyex.plugin.bukkit.bungeeapi.messages;

import me.xpyex.plugin.bukkit.bungeeapi.BungeeAPI;

public class RunCmdOrder extends BungeeMessage {
    public RunCmdOrder(String serverName, String... cmds) {
        super(serverName, BungeeAPI.EXEC_CMD_TITLE, cmds);
    }
}
