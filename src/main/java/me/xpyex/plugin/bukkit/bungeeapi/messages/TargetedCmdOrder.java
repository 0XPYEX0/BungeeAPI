package me.xpyex.plugin.bukkit.bungeeapi.messages;

import java.util.Arrays;
import me.xpyex.plugin.bukkit.bungeeapi.BungeeAPI;
import org.bukkit.entity.Player;

public class TargetedCmdOrder extends BungeeMessage {
    public TargetedCmdOrder(String serverName, Player target, String... cmds) {
        super(serverName, BungeeAPI.TARGETED_CMD_TITLE, ("[" + target.getName() + ", " + Arrays.toString(cmds).substring(1)).split(", "));
    }
}
