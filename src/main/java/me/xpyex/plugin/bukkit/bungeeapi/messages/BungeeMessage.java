package me.xpyex.plugin.bukkit.bungeeapi.messages;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import me.xpyex.plugin.bukkit.bungeeapi.BungeeAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * 该类及其子类均为了发送而创造！接收部分与此无关！
 */
public class BungeeMessage {
    private static final String BROADCAST_TITLE = "BungeeAPI_Broadcast";
    private static final String EXEC_CMD_TITLE = "BungeeAPI_RunCmd";
    private static final String PRIVATE_MSG_TITLE = "BungeeAPI_PrivateMsg";
    private static final String TARGETED_CMD_TITLE = "BungeeAPI_TargetedCmd";

    private final String serverName;
    private final String title;
    private final String[] messages;
    private final ByteArrayDataOutput packet;

    /**
     * 获得一个BungeeMessage对象，可用于发送
     * @param serverName 目标服务器在BungeeCord配置文件内的名字，若传入ALL则表示所有服务器均收到(除当前服务器)
     * @param title 这条信息的标题，用于接收时判断消息类型
     * @param messages 消息本体，可传入多条
     */
    public BungeeMessage(String serverName, String title, String... messages) {
        this.serverName = serverName;
        this.title = title;
        this.messages = messages;
        ByteArrayDataOutput temp = ByteStreams.newDataOutput();
        temp.writeUTF("Forward");  //定义该信息为发送给BC，而非玩家，类型为自定义信息
        temp.writeUTF(serverName);  //指定服务器
        temp.writeUTF(title);  //定义信息标题，将在接收时判断信息类型
        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);
        for (String s : messages) {
            try {  //不在外部try catch，这样即使有一条信息无法添加，其余信息依然可以发送出去
                msgOut.writeUTF(s);  //写入真信息
            } catch (Throwable ignored) {}
        }
        temp.writeShort(msgBytes.toByteArray().length);
        temp.write(msgBytes.toByteArray());
        this.packet = temp;
    }

    public void send() {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            return;  //没有玩家无法发送
        }
        if (title.equals(TARGETED_CMD_TITLE)) {
            Player p = Bukkit.getPlayerExact(messages[0]);
            if (p != null) {
                for (int i = 1; i < messages.length; i++) {
                    try {
                        Bukkit.dispatchCommand(p, messages[i]);
                    } catch (Throwable ignored) {}
                }
                return;  //玩家就在这个服务器，不需要再发包了
            }
        } else if (title.equals(PRIVATE_MSG_TITLE)) {
            Player p = Bukkit.getPlayerExact(messages[0]);
            if (p != null) {
                for (int i = 1; i < messages.length; i++) {
                    p.sendMessage(messages[i]);
                }
                return;  //玩家就在这个服务器，不需要再发包了
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendPluginMessage(BungeeAPI.INSTANCE, BungeeAPI.BUNGEE_CHANNEL, packet.toByteArray());
            break;
        }
        if (title.equals(BROADCAST_TITLE)) {  //在该子服也要处理信息
            for (String s : messages) {
                Bukkit.broadcastMessage(s);
            }
        } else if (title.equals(EXEC_CMD_TITLE)) {
            for (String s : messages) {
                try {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
                } catch (Throwable ignored) {}
            }
        }
    }
}
