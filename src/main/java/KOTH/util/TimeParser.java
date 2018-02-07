package KOTH.util;

import org.apache.commons.lang.time.DurationFormatUtils;

import java.sql.Time;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mike (c) 2017. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 9/9/2017 at 3:13 PM.
 */
public class TimeParser {

    public static int parseToSeconds(String time) {
        int amount = 0;
        if (time.contains(":")) {
            String[] split = time.split(":");
            for (String bits : split) {
                amount = amount + parseTime(bits);
            }
        } else amount = parseTime(time);
        return amount;
    }
    public static String parseToTimeFormat(String time) {
        int millis = (parseToSeconds(time)*1000);
        return DurationFormatUtils.formatDuration(millis, "H:mm:ss", true);
    }

    public static String timeConversion(long totalSeconds) {
        final long MINUTES_IN_AN_HOUR = 60;
        final long SECONDS_IN_A_MINUTE = 60;

        long seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        long totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        long minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        long hours = totalMinutes / MINUTES_IN_AN_HOUR;
        return (hours != 0 ? hours + "h " : "") + (minutes < 10 ? "0" + minutes + "m " : minutes + "m ") + (seconds < 10 ? "0" + seconds  + "s" : seconds + "s");
    }

    private static int parseTime(String time) {
        time = time.toLowerCase();
        String[] split;
        if (time.contains("w")) {
            split = time.split("w");
            return Integer.parseInt(split[0]) * 604800;
        } else if (time.contains("d")) {
            split = time.split("d");
            return Integer.parseInt(split[0]) * 86400;
        } else if (time.contains("h")) {
            split = time.split("h");
            return Integer.parseInt(split[0]) * 3600;
        } else if (time.contains("m")) {
            split = time.split("m");
            return Integer.parseInt(split[0]) * 60;
        } else if (time.contains("s")) {
            split = time.split("s");
            return Integer.parseInt(split[0]);
        }
        return 0;
    }
}
