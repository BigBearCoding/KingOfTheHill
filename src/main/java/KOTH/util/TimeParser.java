package KOTH.util;

import org.apache.commons.lang.time.DurationFormatUtils;

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
    public static String parseToTimeFormat(String time)
    {
        int millis = (parseToSeconds(time)*1000);
        return DurationFormatUtils.formatDuration(millis, "H:mm:ss", true);
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
