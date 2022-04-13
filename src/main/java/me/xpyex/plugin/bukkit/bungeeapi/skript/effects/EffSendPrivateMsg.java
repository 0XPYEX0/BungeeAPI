package me.xpyex.plugin.bukkit.bungeeapi.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffSendPrivateMsg extends Effect {
    static {
        Skript.registerEffect(EffSendPrivateMsg.class,
                "send private (msg|message) %strings% to %string%");
    }

    @Override
    protected void execute(Event e) {

    }

    @Override
    public String toString(Event e, boolean debug) {
        return null;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return false;
    }
}
