package KOTH.util;

import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Mike (c) 2017. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 9/9/2017 at 2:46 PM.
 */
public class LocationUtil {

    public static Location stringToLocation(final String str){
        String[] split = str.split(":");
        if(split.length == 6){
            World world = Bukkit.getWorld(split[0]);

            double  x = Double.parseDouble(split[1]),
                    y = Double.parseDouble(split[2]),
                    z = Double.parseDouble(split[3]);

            float   pitch = Float.parseFloat(split[4]),
                    yaw = Float.parseFloat(split[5]);

            return new Location(world, x, y, z, pitch, yaw);
        }
        return null;
    }

    public static String locationToString(final Location loc, final boolean saveBlock){
        return loc.getWorld().getName() + ":" +
                (saveBlock ? loc.getBlockX() : loc.getX()) + ":" +
                (saveBlock ? loc.getBlockY() : loc.getY()) + ":" +
                (saveBlock ? loc.getBlockZ() : loc.getZ()) + ":" +
                loc.getPitch() + ":" +
                loc.getYaw();
    }

    public static boolean isInsideLocation(Location current, Location corner1, Location corner2){
        if(current != null && corner1 != null && corner2 != null){
            int px = current.getBlockX(),
                l1x = corner1.getBlockX(),
                l2x = corner2.getBlockX();

            int py = current.getBlockY(),
                l1y = corner1.getBlockY(),
                l2y = corner2.getBlockY();

            int pz = current.getBlockZ(),
                l1z = corner1.getBlockZ(),
                l2z = corner2.getBlockZ();

            int xMax = Math.max(l1x, l2x),
                xMin = Math.min(l1x, l2x),
                yMax = Math.max(l1y, l2y),
                yMin = Math.min(l1y, l2y),
                zMax = Math.max(l1z, l2z),
                zMin = Math.min(l1z, l2z);

            return px <= xMax && px >= xMin && py <= yMax && py >= yMin && pz <= zMax && pz >= zMin;
        }
        return false;
    }

    public static boolean isInsideLocation(Location current, Selection selection){
        return isInsideLocation(current, selection.getMaximumPoint(), selection.getMinimumPoint());
    }

}
