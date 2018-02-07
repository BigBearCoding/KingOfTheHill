package KOTH.commands.commandcenter;

import KOTH.commands.Arguments;
import KOTH.commands.CommandHelper;
import KOTH.commands.EasyCommand;
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
 * Created on 2/6/2018 at 1:40 PM.
 */
public class KOTHCommand extends CommandHelper {

    @Override
    public String[] getHelpMessage() {
        return new String[]
                {"&e========KOTH========",
                 "&6/koth help &7- &5Print out a list of all KOTH commands",
                 "&e===================="};
    }

    @EasyCommand(name = "koth",
            description = "The parent command to handle everything 'King of the Hill' related",
            usage = "/koth help",
            inGameOnly = true)
    public void kothParent(Arguments arguments){
        CommandSender sender = arguments.getSender();
        String name = arguments.getName();
        String[] args = arguments.getArgs();
        if(args.length != 0){
            ChatUtil.sendMessage(sender, Lang.PREFIX.toString() + Lang.INVALID_SYNTAX.toString().replace("$command", name).replace("$usage", arguments.getCommand().getUsage()));
        }else{
            kothHelp(arguments);
        }
    }

    @EasyCommand(name = "koth.help",
            aliases = {"koth.h"},
            description = "Print a list of all KOTH commands",
            usage = "/koth help",
            inGameOnly = true)
    public void kothHelp(Arguments arguments){
        super.send(arguments.getSender());
    }
}
