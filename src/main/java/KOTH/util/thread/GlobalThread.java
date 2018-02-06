package KOTH.util.thread;

import KOTH.util.chat.log.LogLevel;
import KOTH.util.chat.log.LogUtil;
import org.bukkit.plugin.Plugin;

/**
 * Created by Mike (c) 2018. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 1/29/2018 at 11:03 PM.
 */
public class GlobalThread extends SThread {

    Plugin plugin;
    private ThreadAttributes attributes;

    public GlobalThread(Plugin plugin, ThreadAttributes attributes) {
        this.plugin = plugin;
        this.attributes = attributes;
    }

    public void run() {
        for (Threaded thread : this.attributes.threadPool) {
            if (!thread.executeTick()) {
                LogUtil.warn("Imbeded thread #" + thread.hashCode() + " failed to execute:");
                LogUtil.warn("- " + thread.fail());
            }
            if (thread.hasElapsedSeconds()) {
                if (!thread.executeToSeconds()) {
                    LogUtil.warn("Imbeded thread #" + thread.hashCode() + " failed to execute:");
                    LogUtil.warn("- " + thread.fail());
                }
            }
            if (thread.hasElapsedMinutes()) {
                if (!thread.executeToMinutes()) {
                    LogUtil.warn("Imbeded thread #" + thread.hashCode() + " failed to execute:");
                    LogUtil.warn("- " + thread.fail());
                }
            }
            if (thread.hasElapsedHours()) {
                if (!thread.executeToHours()) {
                    LogUtil.warn("Imbeded thread #" + thread.hashCode() + " failed to execute:");
                    LogUtil.warn("- " + thread.fail());
                }
            }
        }
        attributes.flushThreads();
    }

    public ThreadAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(ThreadAttributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public ThreadType getType() {
        return ThreadType.GLOBAL;
    }

}
