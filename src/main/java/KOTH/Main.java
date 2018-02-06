package KOTH;

import KOTH.commands.Framework;
import KOTH.util.BFile;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by BigBearCoding (c) 2018. All rights reserved.
 * Any code contained within KingOfTheHill (this document), and any associated APIs with similar branding
 * are the sole property of BigBearCoding.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 2/5/2018 at 10:00 PM.
 */
public class Main extends JavaPlugin{

    private static Main instance;

    private BFile config;
    private BFile language;

    private Framework framework;

    @Override
    public void onEnable() {
        instance = this;

        framework = new Framework(this);
    }

    @Override
    public void onDisable() {

    }

    public static Main get() {
        return instance;
    }

    public WorldEditPlugin getWorldEdit(){
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if(p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
        else return null;
    }

    public BFile getConfigFile() {
        return config;
    }

    public BFile getLanguageFile() {
        return language;
    }

    private void registerCommands(){

    }

}
