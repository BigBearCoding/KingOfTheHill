package KOTH.util.thread;

/**
 * Created by Mike (c) 2018. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 1/29/2018 at 11:04 PM.
 */
public abstract class Threaded {

    public Threaded() {
        this(ThreadManager.get().getFreeThread(ThreadTick.TICK));
    }

    public Threaded(ThreadTick tick) {
        this(ThreadManager.get().getFreeThread(tick));
    }

    public Threaded(SThread thread) {
        thread.getAttributes().addNewThread(this);
    }

    public boolean executeTick() {
        return true;
    }

    public boolean executeToSeconds() {
        return true;
    }

    public boolean executeToMinutes() {
        return true;
    }

    public boolean executeToHours() {
        return true;
    }

    public boolean executeWhenDone() {
        return true;
    }

    String fail = "Default";

    public void setfail(String fail) {
        this.fail = fail;
    }

    public String fail() {
        return fail;
    }

    protected long elapsedSeconds = 0;
    protected long elapsedMinutes = 0;
    protected long elapsedHours = 0;

    public boolean hasElapsedSeconds() {
        if (elapsedSeconds == 0)
            elapsedSeconds = System.currentTimeMillis();
        if (elapsed(this.elapsedSeconds, 1000L)) {
            this.elapsedSeconds = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public boolean hasElapsedMinutes() {
        if (elapsedMinutes == 0)
            elapsedMinutes = System.currentTimeMillis();
        if (elapsed(this.elapsedMinutes, (1000L)*60L)) {
            this.elapsedMinutes = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public boolean hasElapsedHours() {
        if (elapsedHours == 0)
            elapsedHours = System.currentTimeMillis();
        if (elapsed(this.elapsedHours, ((1000L)*60L)*60L)) {
            this.elapsedHours = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public boolean elapsed(long from, long required) {
        return (System.currentTimeMillis() - from) > required;
    }
}
