package KOTH.util;

import KOTH.util.chat.log.LogLevel;
import KOTH.util.chat.log.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mike (c) 2017. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 9/9/2017 at 2:44 PM.
 */
public class BFile {

    private File folder;
    private File file;
    private YamlConfiguration config;

    public BFile(File folder, File file){
        this.folder = folder;
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public BFile(File folder, String fileName){
        this.folder = folder;
        this.file = new File(folder, fileName + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public BFile(File folder, String fileName, String extension){
        this.folder = folder;
        this.file = new File(folder, fileName + "." + extension);
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public boolean createFile(){
        if(!folder.exists()){
            folder.mkdirs();
        }

        if(!file.exists()){
            try{
                file.createNewFile();
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean fileExists(){
        return file.exists();
    }

    public boolean loadFile(){
        try {
            config.load(file);
            return true;
        }catch (FileNotFoundException e){
            createFile();
            return false;
        } catch (IOException e) {
            createFile();
            return false;
        } catch (InvalidConfigurationException e) {
            createFile();
            return false;
        }
    }

    public boolean saveFile(){
        try{
            config.save(file);
            return true;
        }catch (IOException e){
            LogUtil.log(LogLevel.WARN, "Failed to save the file '" + file.getName() + "'");
            return false;
        }
    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getConfig(){
        return this.config;
    }

    public Object get(String path) {return this.config.get(path);}

    public Object get(String path, Object def) {return this.config.get(path, def);}

    public String getString(String path) {return this.config.getString(path);}

    public String getString(String path, String def) {return this.config.getString(path, def);}

    public int getInt(String path) {return this.config.getInt(path);}

    public int getInt(String path, int def) {return this.config.getInt(path, def);}

    public boolean getBoolean(String path) {return this.config.getBoolean(path);}

    public boolean getBoolean(String path, boolean def) {return this.config.getBoolean(path, def);}

    public void createSection(String path) {this.config.createSection(path);}

    public ConfigurationSection getConfigurationSection(String path) {return this.config.getConfigurationSection(path);}

    public double getDouble(String path) {return this.config.getDouble(path);}

    public double getDouble(String path, double def) {return this.config.getDouble(path, def);}

    public List<?> getList(String path) {return this.config.getList(path);}

    public List<?> getList(String path, List<?> def) {return this.config.getList(path, def);}

    public boolean contains(String path) {return this.config.contains(path);}

    public void removeKey(String path) {this.config.set(path, null);}

    public Set<String> getKeys(Boolean deep) {return this.config.getKeys(deep);}

    public List<String> getStringList(String path) {return this.config.getStringList(path);}

    public List<Integer> getIntList(String path) { return this.config.getIntegerList(path); }

    public void set(String path, Object value) {this.config.set(path, value);}

    public Set<String> getKeys(boolean deep){return this.config.getKeys(deep);}

    public List<Map<?, ?>> getMapList(String path){ return this.config.getMapList(path); }

}