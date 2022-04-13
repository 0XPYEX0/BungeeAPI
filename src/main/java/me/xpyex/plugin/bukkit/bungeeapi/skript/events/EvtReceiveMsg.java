package me.xpyex.plugin.bukkit.bungeeapi.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import me.xpyex.plugin.bukkit.bungeeapi.event.ReceiveBungeeMsgEvent;
import org.bukkit.event.Event;

public class EvtReceiveMsg extends SkriptEvent {
    static {
        Skript.registerEvent("Receive Message", EvtReceiveMsg.class, ReceiveBungeeMsgEvent.class, "A");
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return false;
    }

    @Override
    public boolean check(Event e) {
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return null;
    }
}
