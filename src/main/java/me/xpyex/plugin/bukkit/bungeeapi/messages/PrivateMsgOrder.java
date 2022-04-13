package me.xpyex.plugin.bukkit.bungeeapi.messages;

import java.util.Arrays;
import me.xpyex.plugin.bukkit.bungeeapi.BungeeAPI;
import org.bukkit.entity.Player;

public class PrivateMsgOrder extends BungeeMessage {
    public PrivateMsgOrder(String serverName, Player target, String... msgs) {
        super(serverName, BungeeAPI.PRIVATE_MSG_TITLE, ("[" + target.getName() + ", " + Arrays.toString(msgs).substring(1)).split(", "));
    }
}
