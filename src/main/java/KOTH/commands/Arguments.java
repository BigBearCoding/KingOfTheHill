package KOTH.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by BigBearCoding (c) 2018. All rights reserved.
 * Any code contained within KingOfTheHill (this document), and any associated APIs with similar branding
 * are the sole property of BigBearCoding.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 2/5/2018 at 10:43 PM.
 */
public class Arguments {

    private CommandSender sender;
    private Command command;
    private String name;
    private String[] args;

    protected Arguments(CommandSender sender, Command command, String name, String[] args, int subCommand) {
        String[] modArgs = new String[args.length - subCommand];
        for (int i = 0; i < args.length - subCommand; i++) {
            modArgs[i] = args[i + subCommand];
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append(name);
        for (int x = 0; x < subCommand; x++) {
            buffer.append(".").append(args[x]);
        }
        String cmdLabel = buffer.toString();
        this.sender = sender;
        this.command = command;
        this.name = cmdLabel;
        this.args = modArgs;
    }

    public CommandSender getSender() {
        return sender;
    }

    public Command getCommand() {
        return command;
    }

    public String getName() {
        return name;
    }

    public String[] getArgs() {
        return args;
    }

    public String getArgs(int index){
        return args[index];
    }

    public int length(){
        return args.length;
    }

    public boolean isPlayer(){
        return sender instanceof Player;
    }

    public Player getPlayer(){
        if(isPlayer()) return (Player) sender;
        return null;
    }
}
