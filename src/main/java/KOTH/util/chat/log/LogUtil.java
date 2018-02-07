package KOTH.util.chat.log;

import KOTH.util.chat.Lang;
import KOTH.util.chat.SystemColor;
import org.bukkit.Bukkit;

/**
 * Created by Mike (c) 2017. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 9/9/2017 at 8:36 PM.
 */
public class LogUtil {

    public static void log(String msg){
        log(LogLevel.DEFAULT, msg);
    }

    public static void log(int l, String msg){
        LogLevel level = LogLevel.getByID(l);
        assert level != null;
        log(level, msg);
    }

    public static void log(LogLevel level, String msg){
        if(level == LogLevel.ERROR){
            System.out.println(Lang.CONSOLE_PREFIX.toString() + level.getPrintColor() + msg + SystemColor.RESET);
            Bukkit.getServer().shutdown();
        }else{
            System.out.println(Lang.CONSOLE_PREFIX.toString() + level.getPrintColor() + msg + SystemColor.RESET);
        }
    }

    public static void inform(String msg){
        log(LogLevel.INFO, msg);
    }

    public static void debug(String msg){
        log(LogLevel.DEBUG, msg);
    }

    public static void warn(String msg){
        log(LogLevel.WARN, msg);
    }

    public static void throwError(String msg){
        log(LogLevel.ERROR, msg);
    }

    public static void trace(String msg){
        log(LogLevel.TRACE, msg);
    }

}
