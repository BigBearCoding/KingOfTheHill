package KOTH.rewards;

/**
 * Created by BigBearCoding (c) 2018. All rights reserved.
 * Any code contained within KingOfTheHill (this document), and any associated APIs with similar branding
 * are the sole property of BigBearCoding.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 2/6/2018 at 11:02 PM.
 */
public enum RewardDistribution {

    IN_ORDER(1),
    RANDOM(2),
    ALL(3);

    private int id;

    RewardDistribution(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
