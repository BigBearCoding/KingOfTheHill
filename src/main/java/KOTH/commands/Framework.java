package KOTH.commands;


import KOTH.util.chat.ChatUtil;
import KOTH.util.chat.Lang;
import KOTH.util.chat.log.LogLevel;
import KOTH.util.chat.log.LogUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BigBearCoding (c) 2018. All rights reserved.
 * Any code contained within KingOfTheHill (this document), and any associated APIs with similar branding
 * are the sole property of BigBearCoding.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 2/5/2018 at 10:18 PM.
 */
public class Framework implements CommandExecutor{

    private Map<String, Map.Entry<Method, Object>> cMap = new HashMap<String, Map.Entry<Method, Object>>();
    private CommandMap map;
    private Plugin plugin;

    public Framework(Plugin plugin) {
        this.plugin = plugin;
        if (plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
            SimplePluginManager manager = (SimplePluginManager) plugin.getServer().getPluginManager();
            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                map = (CommandMap) field.get(manager);
            } catch (IllegalArgumentException e) {
                LogUtil.log(LogLevel.ERROR, "Exception caught when registering Framework.");
            } catch (SecurityException e) {
                LogUtil.log(LogLevel.ERROR, "Exception caught when registering Framework.");
            } catch (IllegalAccessException e) {
                LogUtil.log(LogLevel.ERROR, "Exception caught when registering Framework.");
            } catch (NoSuchFieldException e) {
                LogUtil.log(LogLevel.ERROR, "Exception caught when registering Framework.");
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String name, String[] args){
        return handle(commandSender, command, name, args);
    }

    public boolean handle(CommandSender sender, Command cmd, String name, String[] args){
        for (int i = args.length; i >= 0; i--) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(name.toLowerCase());
            for (int x = 0; x < i; x++) {
                buffer.append(".").append(args[x].toLowerCase());
            }
            String cmdLabel = buffer.toString();
            if (cMap.containsKey(cmdLabel)) {
                Method method = cMap.get(cmdLabel).getKey();
                Object methodObject = cMap.get(cmdLabel).getValue();
                EasyCommand command = method.getAnnotation(EasyCommand.class);
                if (!command.permission().equals("") && !sender.hasPermission(command.permission())) {
                    ChatUtil.sendMessage(sender, Lang.PREFIX.toString() + Lang.NO_PERM.toString().replace("$command", name).replace("$permission", command.permission()));
                    return true;
                }
                if (command.inGameOnly() && !(sender instanceof Player)) {
                    ChatUtil.sendMessage(sender, Lang.PREFIX.toString() + Lang.IN_GAME_ONLY.toString().replace("$command", name));
                    return true;
                }
                try {
                    method.invoke(methodObject, new Arguments(sender, cmd, name, args,
                            cmdLabel.split("\\.").length - 1));
                } catch (IllegalArgumentException e) {
                    LogUtil.log(LogLevel.ERROR, "Exception caught when handling " + cmdLabel + ". Exception: " + e.getMessage());
                } catch (IllegalAccessException e) {
                    LogUtil.log(LogLevel.ERROR, "Exception caught when handling " + cmdLabel + ". Exception: " + e.getMessage());
                } catch (InvocationTargetException e) {
                    LogUtil.log(LogLevel.ERROR, "Exception caught when handling " + cmdLabel + ". Exception: " + e.getMessage());
                }
                return true;
            }
        }
        defaultCommand(new Arguments(sender, cmd, name, args, 0));
        return true;
    }

    public void registerCommands(Object obj) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(EasyCommand.class) != null) {
                EasyCommand command = m.getAnnotation(EasyCommand.class);
                if (m.getParameterTypes().length > 1 || m.getParameterTypes()[0] != Arguments.class) {
                    LogUtil.log(LogLevel.WARN, "Unable to register command " + m.getName() + ". Unexpected method arguments");
                    continue;
                }
                registerCommand(command, command.name(), m, obj);
                for (String alias : command.aliases()) {
                    registerCommand(command, alias, m, obj);
                }
            }
        }
    }

    private void registerCommand(EasyCommand command, String name, Method m, Object obj) {
        cMap.put(name.toLowerCase(), new AbstractMap.SimpleEntry<Method, Object>(m, obj));
        cMap.put(this.plugin.getName() + ':' + name.toLowerCase(), new AbstractMap.SimpleEntry<Method, Object>(m, obj));
        String cmdLabel = name.split("\\.")[0].toLowerCase();
        if (map.getCommand(cmdLabel) == null) {
            Command cmd = new AutoCommand(cmdLabel, this, plugin);
            map.register(plugin.getName(), cmd);
        }
        if (!command.description().equalsIgnoreCase("") && cmdLabel.equals(name)) {
            map.getCommand(cmdLabel).setDescription(command.description());
        }
        if (!command.usage().equalsIgnoreCase("") && cmdLabel.equals(name)) {
            map.getCommand(cmdLabel).setUsage(command.usage());
        }
    }

    private void defaultCommand(Arguments args) {
        ChatUtil.sendMessage(args.getSender(), Lang.PREFIX.toString() + Lang.INVALID_COMMAND.toString().replace("$command", args.getName()));
    }

}
