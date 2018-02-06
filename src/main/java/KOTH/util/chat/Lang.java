package KOTH.util.chat;

import KOTH.Main;
import org.bukkit.ChatColor;

import java.util.Map;

/**
 * Created by Mike (c) 2017. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 9/9/2017 at 8:50 PM.
 */
public enum Lang {

    PREFIX("prefix", "&aKOTH &b»  "),
    NO_PERM("no-perm", "&cYou do not have the necessary permission to perform /$command."),
    INVALID_SYNTAX("syntax-error", "&cInvalid Syntax: Please use &c$usage&c."),
    INVALID_COMMAND("invalid-command", "&c/$command is an unhandled command. Please use &6/help &cand try again."),
    IN_GAME_ONLY("ig-only", "&cYou must be in-game to perform this command.");

    private final String path;
    private final String def;

    Lang(String path, String def) {
        this.path = path;
        this.def = def;
    }

    @Override
    public String toString() {
        String main = Main.get().getLanguageFile().getString(path);

        if (this == PREFIX) {
            return ChatColor.translateAlternateColorCodes('&', (main == null ? def : main) + "  ");
        }
        return ChatColor.translateAlternateColorCodes('&', (main == null ? def : main));
    }

}
