package KOTH.util.board;

import KOTH.util.chat.log.LogLevel;
import KOTH.util.chat.log.LogUtil;
import com.google.common.base.Splitter;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.Iterator;

/**
 * Created by Mike (c) 2018. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 1/29/2018 at 10:00 PM.
 */
public class Entry {

    private String key;
    private Board board;
    private String name;
    private Team team;
    private Score score;
    private int value;

    private String origName;
    private int count;

    public Entry(String key, Board board, int value) {
        this.key = key;
        this.board = board;
        this.value = value;
    }

    public Entry(String key, Board board, int value, String origName, int count) {
        this.key = key;
        this.board = board;
        this.value = value;
        this.origName = origName;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public Board getBoard() {
        return board;
    }

    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    public Score getScore() {
        return score;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (!score.isScoreSet()) score.setScore(-1);
        score.setScore(value);
    }

    public void update(String newName){
        int value = getValue();
        if(origName != null && newName.equalsIgnoreCase(origName)){
            for(int i = 0; i < count; i++){
                newName = ChatColor.RESET + newName;
            }
        }
        create(newName);
        setValue(value);
    }

    public void remove(){
        if(score != null){
            try {
                score.getScoreboard().resetScores(score.getEntry());
            } catch (NullPointerException e){
                LogUtil.log(LogLevel.ERROR, "Null Pointer Exception caught when trying to remove score from '" + score.getEntry() + "'.");
            }
        }
        if(team != null){
            try {
                team.unregister();
            }catch (NullPointerException e){
                LogUtil.log(LogLevel.ERROR, "Null Pointer Exception caught when trying to unregister team '" + team.getName() + "'.");
            }
        }
    }

    public void create(String name){
        this.name = name;
        remove();

        if(name.length() <= 16){
            int value = getValue();
            score = board.getObjective().getScore(name);
            score.setScore(value);
            return;
        }

        team = board.getScoreboard().registerNewTeam("board-" + board.getTeamId());
        Iterator<String> iterator = Splitter.fixedLength(16).split(name).iterator();
        if (name.length() > 16) team.setPrefix(iterator.next());
        String entry = iterator.next();
        score = board.getObjective().getScore(entry);
        if (name.length() > 32) team.setSuffix(iterator.next());

        team.addEntry(entry);
    }

}
