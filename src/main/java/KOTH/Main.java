package KOTH;

import KOTH.arenas.ArenaManager;
import KOTH.commands.Framework;
import KOTH.commands.commandcenter.KOTHCommand;
import KOTH.util.BFile;
import KOTH.util.chat.Lang;
import KOTH.util.chat.log.LogLevel;
import KOTH.util.chat.log.LogUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.TimeZone;

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
        initialSetup();
        if(getWorldEdit() == null){
            LogUtil.log(LogLevel.ERROR, "WorldEdit was not located.");
        }
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

    private void initialSetup(){
        registerLanguage();
        registerCommands();
        registerConfig();
        registerEvents();
        ArenaManager.getManager().setupArenas();
        //TODO Register all rewards
    }

    private void registerCommands(){
        framework = new Framework(this);
        framework.registerCommands(new KOTHCommand());
    }

    private void registerConfig(){

    }

    private void registerLanguage(){
        language = new BFile(getDataFolder(), "language");
        if(!language.fileExists()){
            if(language.createFile()){
                language.loadFile();
                for(Lang item : Lang.values()){
                    if(language.getString(item.getPath()) == null){
                        language.set(item.getPath(), item.getDef());
                    }
                }
                language.saveFile();
            }else{
                LogUtil.log(LogLevel.ERROR, "Could not create language file. This is a fatal error, disabling...");
                return;
            }
        }
        language.loadFile();
    }

    private void registerEvent(){

    }

    private void registerEvents(){

    }

    public TimeZone getTimeZone(){
        TimeZone zone;
        try{
            zone = TimeZone.getTimeZone(getConfigFile().getString("koth.timezone"));
        }catch(NullPointerException npe){
            zone = TimeZone.getDefault();
        }
        return zone;
    }

}
