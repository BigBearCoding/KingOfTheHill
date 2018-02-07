package KOTH.storage;

import KOTH.arenas.Arena;
import KOTH.rewards.Reward;
import KOTH.util.BFile;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by BigBearCoding (c) 2018. All rights reserved.
 * Any code contained within KingOfTheHill (this document), and any associated APIs with similar branding
 * are the sole property of BigBearCoding.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 2/6/2018 at 10:21 PM.
 */
public class KPlayer {

    private Player player;
    private Arena currentArena;
    private List<Reward> rewards;
    private BFile infoFile;
    private KPlayerStatistics statistics;

    public KPlayer(Player player) {
        this.player = player;

    }

    public Player getPlayer() {
        return player;
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    public void setCurrentArena(Arena currentArena) {
        this.currentArena = currentArena;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public BFile getInfoFile() {
        return infoFile;
    }

    public KPlayerStatistics getStatistics() {
        return statistics;
    }
}
