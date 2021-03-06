package KOTH.util.thread;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Mike (c) 2018. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 1/29/2018 at 11:04 PM.
 */
public abstract class SThread extends BukkitRunnable implements Runnable{

    public abstract void run();
    public abstract ThreadType getType();
    public abstract ThreadAttributes getAttributes();

}
