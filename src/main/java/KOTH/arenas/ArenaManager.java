package KOTH.arenas;

import KOTH.Main;
import KOTH.rewards.Reward;
import KOTH.util.BFile;
import KOTH.util.LocationUtil;
import KOTH.util.chat.log.LogUtil;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sun.org.apache.bcel.internal.generic.ARETURN;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by BigBearCoding (c) 2018. All rights reserved.
 * Any code contained within KingOfTheHill (this document), and any associated APIs with similar branding
 * are the sole property of BigBearCoding.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 2/6/2018 at 10:29 PM.
 */
public class ArenaManager {

    private List<Arena> arenas = new ArrayList<Arena>();

    private Arena running = null;

    public void setupArenas(){
        File folder = new File(Main.get().getDataFolder() + File.separator + "arenas");
        if(folder.isDirectory() && folder.listFiles() != null){
            for(File file : folder.listFiles()){
                BFile info = new BFile(folder, file);
                Arena a = new Arena(info);
                arenas.add(a);
            }
        }
    }

    public Arena createArena(String name, Selection selection){
        BFile infoFile = new BFile(new File(Main.get().getDataFolder() + File.separator + "arenas"), name + "-arena");
        if(!infoFile.fileExists()){
            infoFile.createFile();
            infoFile.loadFile();

            infoFile.set("name", name);
            infoFile.set("id", arenas.size());
            infoFile.set("selection.min", LocationUtil.locationToString(selection.getMinimumPoint(), true));
            infoFile.set("selection.max", LocationUtil.locationToString(selection.getMaximumPoint(), true));
            infoFile.set("options.time", "5m");
            infoFile.set("options.useScoreboard", true);
            infoFile.set("options.particles.type", Particle.VILLAGER_HAPPY.name());
            infoFile.set("options.particles.on", false);
            infoFile.set("options.schedule.next", new SimpleDateFormat("yyyy/MM/dd@kk:mm").format(new Date(0)));
            infoFile.set("options.schedule.recurring", false);
            infoFile.set("options.schedule.often", "1w");
            infoFile.set("options.cooldown.time", "2s");
            infoFile.set("options.cooldown.use", true);
            infoFile.set("options.rewardsDistribution", 1);
            infoFile.set("options.lastRewardGiven", 0);
            infoFile.set("options.rewards", Arrays.asList(new Reward(), new Reward(), new Reward()));
            infoFile.set("scoreboard",
                    Arrays.asList("&e============",
                    "Location:",
                    "X: $x",
                    "Y: $y",
                    "Z: $z",
                    "&e============",
                    "Time Left:",
                    "$time",
                    "&e============",
                    "King:",
                    "$king",
                    "&e============",
                    "$ip"));

            infoFile.saveFile();
        }
        Arena arena = new Arena(infoFile);
        arenas.add(arena);
        return arena;
    }

    public void deleteArena(Arena arena){
        String name = arena.getName();
        try {
            arena.getScoreboard().getObjective().unregister();
        } catch (Exception e){
            LogUtil.warn("Could not unregister the scoreboard for Arena '" + name + "'.");
        }
        if(arena.getTimer() != null) arena.getTimer().getAttributes().removeThread(arena);
        arena.getInfoFile().getFile().delete();
        arenas.remove(arena);
        //arena.finish();
    }

    public Arena getArena(String name){
        for(Arena a : arenas){
            if(a.getName().equalsIgnoreCase(name)) return a;
        }
        return null;
    }

    public Arena getArena(int id){
        for(Arena a : arenas){
            if(a.getId() == id) return a;
        }
        return null;
    }

    public Arena getArena(Location loc){
        for(Arena a : arenas){
            if(a.getSelection().contains(loc)) return a;
        }
        return null;
    }

    public boolean isArena(String name){
        return getArena(name) != null;
    }

    public boolean isArena(int id){
        return getArena(id) != null;
    }

    public boolean isArena(Location loc){
        return getArena(loc) != null;
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public Arena getRunning() {
        return running;
    }

    public void setRunning(Arena running) {
        this.running = running;
    }

    public boolean isRunning(Arena arena){
        return running.getId() == arena.getId();
    }

    private static ArenaManager manager;

    public static ArenaManager getManager() {
        if(manager == null) manager = new ArenaManager();
        return manager;
    }
}
