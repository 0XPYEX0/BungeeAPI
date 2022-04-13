package me.xpyex.plugin.bukkit.bungeeapi.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.xpyex.plugin.bukkit.bungeeapi.BungeeAPI;
import org.bukkit.event.Event;

public class EffBungeeBroadcast extends Effect {
    static {
        Skript.registerEffect(EffBungeeBroadcast.class,
                "send %strings% to server %string%");
    }
    private Expression<String> serverName;
    private Expression<String> messages;

    @Override
    protected void execute(Event e) {
        for (String s : messages.getArray(e)) {
            BungeeAPI.broadcast(serverName.getSingle(e), s);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "send " + messages.toString(e, debug) + " to server " + serverName.toString(e, debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        serverName = (Expression<String>) exprs[1];
        messages = (Expression<String>) exprs[0];
        return true;
    }
}
