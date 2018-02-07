package KOTH.arenas;

/**
 * Created by BigBearCoding (c) 2018. All rights reserved.
 * Any code contained within KingOfTheHill (this document), and any associated APIs with similar branding
 * are the sole property of BigBearCoding.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 2/6/2018 at 10:18 PM.
 */
public enum ArenaState {

    WAITING("Waiting..."),
    RUNNING("In Progress."),
    ENDED("Ended.");

    private String msgValue;

    ArenaState(String msgValue) {
        this.msgValue = msgValue;
    }

    public String getMsgValue() {
        return msgValue;
    }
}
