package KOTH.util.chat.log;

import KOTH.util.chat.SystemColor;

/**
 * Created by Mike (c) 2017. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 9/9/2017 at 8:38 PM.
 */
public enum LogLevel {

    DEFAULT(0, SystemColor.SILVER, "default"), INFO(1, SystemColor.GREEN, "info"), DEBUG(2, SystemColor.AQUAMARINE, "debug"),
    WARN(3, SystemColor.GOLD, "warn"), ERROR(4, SystemColor.RED, "error"), TRACE(5, SystemColor.MAGENTA, "trace");

    private int id;
    private SystemColor printColor;
    private String name;

    LogLevel(int id, SystemColor printColor, String name) {
        this.id = id;
        this.printColor = printColor;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public SystemColor getPrintColor() {
        return printColor;
    }

    public static LogLevel getByID(int id){
        for(LogLevel l : LogLevel.values()){
            if(id == l.getId()) return l;
        }
        return null;
    }

}
