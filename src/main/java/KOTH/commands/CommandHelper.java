package KOTH.commands;

import KOTH.util.chat.ChatUtil;
import KOTH.util.chat.Lang;
import org.bukkit.command.CommandSender;

/**
 * Created by BigBearCoding (c) 2018. All rights reserved.
 * Any code contained within KingOfTheHill (this document), and any associated APIs with similar branding
 * are the sole property of BigBearCoding.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 2/6/2018 at 9:55 PM.
 */
public abstract class CommandHelper {

    public abstract String[] getHelpMessage();

    public void send(CommandSender sender){
        for(String s : getHelpMessage()){
            ChatUtil.sendMessage(sender, Lang.PREFIX.toString() + s);
        }
    }

}
