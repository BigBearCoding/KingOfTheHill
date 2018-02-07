package KOTH.storage;

import KOTH.arenas.Arena;

/**
 * Created by BigBearCoding (c) 2018. All rights reserved.
 * Any code contained within KingOfTheHill (this document), and any associated APIs with similar branding
 * are the sole property of BigBearCoding.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 2/6/2018 at 10:22 PM.
 */
public class KPlayerStatistics {

    private KPlayer player;

    private int kills;
    private int deaths;
    private long timeAsKing;
    private int wins;
    private Arena bestArena;
    private int rewardsClaimed;

    public KPlayerStatistics(KPlayer player) {
        this.player = player;
    }
}
