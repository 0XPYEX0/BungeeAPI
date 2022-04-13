package me.xpyex.plugin.bukkit.bungeeapi;

import ch.njol.skript.Skript;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import me.xpyex.plugin.bukkit.bungeeapi.event.ReceiveBungeeMsgEvent;
import me.xpyex.plugin.bukkit.bungeeapi.messages.BroadcastOrder;
import me.xpyex.plugin.bukkit.bungeeapi.messages.PrivateMsgOrder;
import me.xpyex.plugin.bukkit.bungeeapi.messages.RunCmdOrder;
import me.xpyex.plugin.bukkit.bungeeapi.messages.TargetedCmdOrder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 该插件基于BungeeCord的收发包能力实现，为BC自带功能
 * 参考文献: https://www.spigotmc.org/wiki/bukkit-bungee-plugin-messaging-channel/
 */
public final class BungeeAPI extends JavaPlugin {
    public static final String BUNGEE_CHANNEL = "BungeeCord";
    //定义频道名

    public static final String BROADCAST_TITLE = "BungeeAPI_Broadcast";
    public static final String EXEC_CMD_TITLE = "BungeeAPI_RunCmd";
    public static final String PRIVATE_MSG_TITLE = "BungeeAPI_PrivateMsg";
    public static final String TARGETED_CMD_TITLE = "BungeeAPI_TargetedCmd";
    //定义所有Title

    public static BungeeAPI INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Skript.registerAddon(INSTANCE);
        Bukkit.getServerName();
        Bukkit.getMessenger().registerOutgoingPluginChannel(INSTANCE, BUNGEE_CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(INSTANCE, BUNGEE_CHANNEL, (channel, player, data) -> {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
            try {
                String subChannel = in.readUTF();  //获取频道
                short length = in.readShort();
                byte[] realData = new byte[length];
                in.readFully(realData);
                DataInputStream realIn = new DataInputStream(new ByteArrayInputStream(realData));
                switch (subChannel) {
                    case BROADCAST_TITLE:   //对所有服务器进行广播的情况
                        while (realIn.available() != 0) {
                            String message = realIn.readUTF();  //得到要发送的信息
                            Bukkit.broadcastMessage(message);
                        }
                        break;
                    case PRIVATE_MSG_TITLE:   //查找服务器内玩家并对其发送私聊
                        Player target = Bukkit.getPlayerExact(realIn.readUTF());  //读取玩家名，获取玩家实例
                        if (target == null) {
                            return;
                        }
                        while (realIn.available() != 0) {
                            target.sendMessage(realIn.readUTF());
                        }
                        break;
                    case EXEC_CMD_TITLE:  //令所有子服都执行命令的情况
                        while (realIn.available() != 0) {
                            String msg = realIn.readUTF();
                            try {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg);
                            } catch (Throwable ignored) {}  //当指令抛出异常时，也会抛到这里来，必须拦截
                        }
                        break;
                    case TARGETED_CMD_TITLE:
                        Player t = Bukkit.getPlayerExact(realIn.readUTF());  //读取玩家名，获取玩家实例
                        if (t == null) {
                            return;
                        }
                        while (realIn.available() != 0) {
                            try {
                                Bukkit.dispatchCommand(t, realIn.readUTF());
                            } catch (Throwable ignored) {}  //当指令抛出异常时，也会抛到这里来，必须拦截
                        }
                        break;
                    default:   //当非BungeeAPI需要的信息时，则广播出去给Skript处理
                        Bukkit.getPluginManager().callEvent(new ReceiveBungeeMsgEvent(new String(data, StandardCharsets.UTF_8).substring(1).trim()));  //直接整合信息并广播事件，不再判断
                        break;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("已卸载");
    }

    public static void broadcast(String targetServer, String... msg) {
        try {
            new BroadcastOrder(targetServer, msg).send();
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void runCMD(String targetServer, String... cmd) {
        try {
            new RunCmdOrder(targetServer,cmd).send();
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void runPlayerCMD(String targetServer, String target, String... cmd) {
        try {
            Player p = Bukkit.getPlayerExact(target);
            if (p == null) {
                return;
            }
            new TargetedCmdOrder(targetServer, p, cmd).send();
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void privateMsg(String targetServer, String target, String... msg) {
        try {
            Player p = Bukkit.getPlayerExact(target);
            if (p == null) {
                return;
            }
            new PrivateMsgOrder(targetServer, p, msg).send();
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}