package me.xpyex.plugin.bukkit.bungeeapi.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.ExprStringCase;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.xpyex.plugin.bukkit.bungeeapi.BungeeAPI;
import org.bukkit.event.Event;

@Name("Make Server Run Cmd")
@Description("It can make server that is in BungeeCord run commands.")
@Examples("make server \"exampleServer\" run command \"abcdefg\"")
public class EffMakeServerRunCmd extends Effect {
    static {
        Skript.registerEffect(EffMakeServerRunCmd.class,
                "make server %string% run (cmd|command) %strings%",
                "make all server run (cmd|command) %strings%");
    }

    private Expression<String> targetServer;
    private Expression<String> cmds;

    @Override
    protected void execute(Event e) {
        String serverName = targetServer.getSingle(e) != null ? targetServer.getSingle(e) : "ALL";
        BungeeAPI.runCMD(serverName, cmds.getArray(e));
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "make " + (targetServer != null ? ("server " + targetServer.toString(e, debug)) : "all server") + " run cmd " + cmds.toString(e, debug);
    }

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            targetServer = (Expression<String>) exprs[0];
            if (targetServer.isSingle()) return false;
            cmds = (Expression<String>) exprs[1];
            return true;
        }
        if (matchedPattern == 1) {
            cmds = (Expression<String>) exprs[0];
            return true;
        }
        return false;
    }
}
