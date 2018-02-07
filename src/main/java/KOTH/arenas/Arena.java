package KOTH.arenas;

import KOTH.Main;
import KOTH.storage.KPlayer;
import KOTH.util.BFile;
import KOTH.util.LocationUtil;
import KOTH.util.TimeParser;
import KOTH.util.board.Board;
import KOTH.util.chat.ChatUtil;
import KOTH.util.chat.Lang;
import KOTH.util.thread.MultiThread;
import KOTH.util.thread.ThreadManager;
import KOTH.util.thread.ThreadTick;
import KOTH.util.thread.Threaded;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by BigBearCoding (c) 2018. All rights reserved.
 * Any code contained within KingOfTheHill (this document), and any associated APIs with similar branding
 * are the sole property of BigBearCoding.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 2/6/2018 at 10:17 PM.
 */
public class Arena extends Threaded {

    private String name;
    private int id;

    private BFile infoFile;
    private MultiThread timer;
    private ArenaState state;

    private KPlayer king;
    private List<KPlayer> inside;

    private Board scoreboard;

    private Selection selection;

    private long timeToLast;
    private long currentLeft;
    private boolean boardOn;
    private Particle effect;
    private boolean particlesOn;
    private Date nextStart;
    private boolean recurring;
    private long recurEvery;
    private int cooldown;
    private boolean cooldownOn;

    public Arena(BFile infoFile) {
        this.infoFile = infoFile;
        this.name = infoFile.getFile().getName().replace("-arena.yml", "");
        this.state = ArenaState.WAITING;
        this.inside = new ArrayList<KPlayer>();
        if(infoFile.fileExists()){
            infoFile.loadFile();

            id = infoFile.getInt("id");

            Location min = LocationUtil.stringToLocation(infoFile.getString("selection.min"));
            Location max = LocationUtil.stringToLocation(infoFile.getString("selection.min"));
            assert min != null && max != null;
            selection = new CuboidSelection(min.getWorld(), min, max);

            timeToLast = TimeParser.parseToSeconds(infoFile.getString("options.time"));
            currentLeft = timeToLast;
            boardOn = infoFile.getBoolean("options.useScoreboard");
            effect = Particle.valueOf(infoFile.getString("options.particles.type"));
            particlesOn = infoFile.getBoolean("options.particles.on");
            recurring = infoFile.getBoolean("options.schedule.recurring");
            recurEvery = TimeParser.parseToSeconds(infoFile.getString("options.schedule.often"));
            cooldown = TimeParser.parseToSeconds(infoFile.getString("options.cooldown.time"));
            cooldownOn = infoFile.getBoolean("options.cooldown.use");

            infoFile.saveFile();
        }
    }

    public Arena(String name){
        this.name = name;
        this.infoFile = new BFile(new File(Main.get().getDataFolder() + File.separator + "arenas"), name + "-arena");
        this.state = ArenaState.WAITING;
        this.inside = new ArrayList<KPlayer>();
        if(infoFile.fileExists()){
            infoFile.loadFile();

            id = infoFile.getInt("id");

            Location min = LocationUtil.stringToLocation(infoFile.getString("selection.min"));
            Location max = LocationUtil.stringToLocation(infoFile.getString("selection.min"));
            assert min != null && max != null;
            selection = new CuboidSelection(min.getWorld(), min, max);

            timeToLast = TimeParser.parseToSeconds(infoFile.getString("options.time"));
            currentLeft = timeToLast;
            boardOn = infoFile.getBoolean("options.useScoreboard");
            effect = Particle.valueOf(infoFile.getString("options.particles.type"));
            particlesOn = infoFile.getBoolean("options.particles.on");
            recurring = infoFile.getBoolean("options.schedule.recurring");
            recurEvery = TimeParser.parseToSeconds(infoFile.getString("options.schedule.often"));
            cooldown = TimeParser.parseToSeconds(infoFile.getString("options.cooldown.time"));
            cooldownOn = infoFile.getBoolean("options.cooldown.use");

            infoFile.saveFile();
        }
    }

    public void update(){
        if (this.hasPassed()) {
            this.state = ArenaState.ENDED;
        }
        register();
    }

    public void refreshBoard(){
        List<String> list = infoFile.getStringList("scoreboard");
        for(String s : list){
            scoreboard.getEntry(list.indexOf(s) + "").update(parseScoreboardString(s));
        }
    }

    public void register(){
        if(this.boardOn){
            scoreboard = new Board("§5[" + name + "]");
            if(infoFile.getStringList("scoreboard") == null){
                scoreboard.add("dash1", "§e============", 13);
                scoreboard.add("loc", "Location:", 12);
                scoreboard.add("x", "X: " + selection.getMinimumPoint().getBlockX(), 11);
                scoreboard.add("y", "Y: " + selection.getMinimumPoint().getBlockY(), 10);
                scoreboard.add("z", "Z: " + selection.getMinimumPoint().getBlockZ(), 9);
                scoreboard.add("dash2", "§e============", 8);
                scoreboard.add("time_left", "Time Left:", 7);
                scoreboard.add("time", TimeParser.timeConversion(currentLeft), 6);
                scoreboard.add("dash3", "§e============", 5);
                scoreboard.add("king", "King:", 4);
                scoreboard.add("name", ((king != null) ? king.getPlayer().getName() : "N/A"), 3);
                scoreboard.add("inside", "Inside: " + inside.size(), 2);
                scoreboard.add("dash4", "§e============", 1);
            }else{
                List<String> list = infoFile.getStringList("scoreboard");
                for(String s : list){
                    scoreboard.add(list.indexOf(s) + "", parseScoreboardString(s), list.indexOf(s));
                }
            }
            if(timer == null){
                this.timer = ThreadManager.get().getFreeThread(ThreadTick.TICK);
            }
            if(!timer.getAttributes().threadPool.contains(this)){
                this.timer.getAttributes().addNewThread(this);
            }
        }
    }

    private String parseScoreboardString(String s){
        return s.replace("$x", selection.getMinimumPoint().getBlockX() + "")
                .replace("$y", selection.getMinimumPoint().getBlockY() + "")
                .replace("$z", selection.getMinimumPoint().getBlockZ() + "")
                .replace("$loc", "(" + selection.getMinimumPoint().getBlockX() + "," + selection.getMinimumPoint().getBlockY() + "," + selection.getMinimumPoint().getBlockZ() + ")")
                .replace("$world", selection.getMinimumPoint().getWorld().getName())
                .replace("$king", king.getPlayer().getName())
                .replace("$time", TimeParser.timeConversion(currentLeft))
                .replace("$startT", new SimpleDateFormat("HH:mm").format(nextStart))
                .replace("$startD", new SimpleDateFormat("MM/dd").format(nextStart))
                .replace("$start", new SimpleDateFormat("MM/dd-HH:mm").format(nextStart))
                .replace("$inside", inside.size() + "")
                .replace("$ip", Main.get().getConfigFile().getString("koth.ip") != null ? Main.get().getConfigFile().getString("koth.ip") : Main.get().getServer().getIp());
    }

    int tick = 0;

    int minutes = 10;

    @Override
    public boolean executeToMinutes() {
        if (this.state != ArenaState.RUNNING)
            return true;
        if (this.minutes == 0) {
            Bukkit.broadcastMessage(ChatColor.RED + this.getName() + " has been force ended due to inactivity!");
            this.timer.getAttributes().removeThread(this);
            this.state = ArenaState.ENDED;
            this.king = null;
            this.inside.clear();
            this.scoreboard.getObjective().unregister();
            return true;
        }
        this.minutes--;
        return true;
    }

    @Override
    public boolean executeTick() {
        if (!this.particlesOn)
            return true;
        if (this.state != ArenaState.RUNNING)
            return true;
        if (tick == 10) {
            Location bottom = this.selection.getMinimumPoint();
            Location top = this.selection.getMaximumPoint();
            if (bottom.getY() > top.getY()) {
                bottom = this.selection.getMaximumPoint();
                top = this.selection.getMaximumPoint();
            }
            this.display(bottom, top);
            if (this.king != null) {
                this.getSelection().getMinimumPoint().getWorld().playEffect(this.king.getPlayer().getEyeLocation().add(0, 0.5, 0), Effect.VILLAGER_THUNDERCLOUD, 0);
            }
            tick = 0;
        }
        tick++;
        return true;
    }

    public boolean setFutureDate(String futureDate) {
        String[] split = futureDate.split("/");
        if (split.length != 3)
            return false;
        if (!this.check("hour_of_day", split[0]))
            return false;
        if (!this.check("minute", split[1]))
            return false;
        if (!this.check("second", split[2]))
            return false;
        infoFile.saveFile();
        return true;
    }

    public boolean check(String query, String num) {
        FileConfiguration config = infoFile.getConfig();
        int i = 0;
        try {
            i = Integer.parseInt(num);
        }catch(Exception e) {
            return false;
        }
        config.set("calendar." + query, i);
        return true;
    }

    @Override
    public boolean executeToSeconds() {
        if (this.state == ArenaState.ENDED) {
            if (this.hasPassed())
                return true;
            this.state = ArenaState.WAITING;
        }

        if (cooldown != 0 && LocationUtil.isInsideLocation(king.getPlayer().getLocation(), selection)) {
            cooldown--;
        } else if (cooldown == 0) {
            ChatUtil.sendMessage(king.getPlayer(), Lang.PREFIX.toString() + Lang.LOST_CONTROL_COOLDOWN.toString()
                    .replace("$player", king.getPlayer().getName()).replace("$cd", cooldown + ""));
            this.king = null;
        }

        if (this.state == ArenaState.WAITING) {
            if (this.getNextStart() == null)
                return true;
            if (!this.hasPassed())
                return true;
            if (ArenaManager.getManager().getRunning() != null)
                return true;
            this.setCurrent(timeToLast);
            this.state = ArenaState.RUNNING;
            ArenaManager.getManager().setRunning(this);
            if (boardOn){
                for (Player player : Bukkit.getOnlinePlayers()) {
                    scoreboard.add(player);
                }
            }
            return true;
        }
        if (this.state != ArenaState.RUNNING)
            return true;
        if (boardOn) refreshBoard();
        if (this.king != null) {
            this.minutes = 15;
            if(boardOn) refreshBoard();
            if (currentLeft % 60 == 0) {
                Bukkit.broadcastMessage(Lang.PREFIX.toString() + Lang.TIMER.toString().replace("$arena", this.name).replace("$time", TimeParser.timeConversion(currentLeft)));
            }
            if (currentLeft == 30) {
                Bukkit.broadcastMessage(Lang.PREFIX.toString() + Lang.TIMER.toString().replace("$arena", this.name).replace("$time", TimeParser.timeConversion(currentLeft)));
            }
            if (currentLeft == 10) {
                Bukkit.broadcastMessage(Lang.PREFIX.toString() + Lang.TIMER.toString().replace("$arena", this.name).replace("$time", TimeParser.timeConversion(currentLeft)));
            }
            if (currentLeft <= 5 && currentLeft != 0) {
                Bukkit.broadcastMessage(Lang.PREFIX.toString() + Lang.TIMER.toString().replace("$arena", this.name).replace("$time", TimeParser.timeConversion(currentLeft)));
            } else if (currentLeft == 0) {
                Bukkit.broadcastMessage(Lang.PREFIX.toString() + Lang.ARENA_WON.toString().replace("$arena", this.name).replace("$player", this.king.getPlayer().getName()));
                //TODO REWARD SYSTEM
                this.timer.getAttributes().removeThread(this);
                this.state = ArenaState.ENDED;
                for (KPlayer kPlayers : this.inside) {
                    kPlayers.setCurrentArena(null);
                }
                this.king = null;
                this.inside.clear();
                if (boardOn){
                    this.scoreboard.getObjective().unregister();
                }
                ArenaManager.getManager().setRunning(null);
            }
            currentLeft--;
        } else {
            if(boardOn) refreshBoard();
        }
        return true;
    }

    public void display(Location bottom, Location top) {
        this.particleX(bottom, top, 0);
        this.particleZ(bottom, top, 0);
        Location newBottomX = new Location(bottom.getWorld(), bottom.getX(), bottom.getY(), top.getZ()+1);
        Location newBottomZ = new Location(bottom.getWorld(), top.getX()+1, bottom.getY(), bottom.getZ());
        this.particleX(newBottomX, top, 0);
        this.particleZ(newBottomZ, top, 0);

    }

    public void particleX(Location bottom, Location top, double count) {
        double x;
        x = bottom.getX() + count;

        Location location = new Location(bottom.getWorld(), x, bottom.getY()+1, bottom.getZ());
        if (king != null && this.isRunning())
            location.getWorld().playEffect(location, Effect.HAPPY_VILLAGER, 0);
        else if (this.isRunning())
            location.getWorld().playEffect(location, Effect.WITCH_MAGIC, 0);
        if (top.getX()+1 > x) {
            particleX(bottom, top, count+0.3);
        }
    }

    public void particleZ(Location bottom, Location top, double count) {
        double z;
        z = bottom.getZ() + count;

        Location location = new Location(bottom.getWorld(), bottom.getX(), bottom.getY()+1, z);
        if (king != null  && this.isRunning())
            location.getWorld().playEffect(location, Effect.HAPPY_VILLAGER, 0);
        else if (this.isRunning())
            location.getWorld().playEffect(location, Effect.WITCH_MAGIC, 0);
        if (top.getZ()+1 >= z) {
            particleZ(bottom, top, count+0.3);
        }
    }

    private boolean isRunning(){
        return state == ArenaState.RUNNING;
    }

    public boolean hasPassed() {
        return this.getNextStart() != null && this.getNextStart().before(Calendar.getInstance(Main.get().getTimeZone()).getTime());
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public BFile getInfoFile() {
        return infoFile;
    }

    public MultiThread getTimer() {
        return timer;
    }

    public ArenaState getState() {
        return state;
    }

    public KPlayer getKing() {
        return king;
    }

    public List<KPlayer> getInside() {
        return inside;
    }

    public Board getScoreboard() {
        return scoreboard;
    }

    public Selection getSelection() {
        return selection;
    }

    public long getTimeToLast() {
        return timeToLast;
    }

    public long getCurrentLeft() {
        return currentLeft;
    }

    public boolean isBoardOn() {
        return boardOn;
    }

    public Particle getEffect() {
        return effect;
    }

    public boolean isParticlesOn() {
        return particlesOn;
    }

    public Date getNextStart() {
        return nextStart;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public long getRecurEvery() {
        return recurEvery;
    }

    public int getCooldown() {
        return cooldown;
    }

    public boolean isCooldownOn() {
        return cooldownOn;
    }

    public void setState(ArenaState state) {
        this.state = state;
    }

    public void setKing(KPlayer king) {
        this.king = king;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    public void setTimeToLast(long timeToLast) {
        this.timeToLast = timeToLast;
    }

    public void setBoardOn(boolean boardOn) {
        this.boardOn = boardOn;
    }

    public void setEffect(Particle effect) {
        this.effect = effect;
    }

    public void setParticlesOn(boolean particlesOn) {
        this.particlesOn = particlesOn;
    }

    public void setNextStart(Date nextStart) {
        this.nextStart = nextStart;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public void setRecurEvery(long recurEvery) {
        this.recurEvery = recurEvery;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setCooldownOn(boolean cooldownOn) {
        this.cooldownOn = cooldownOn;
    }

    public void setCurrent(long currentLeft) {
        this.currentLeft = currentLeft;
    }

    public void finish() {
        try {
            this.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
