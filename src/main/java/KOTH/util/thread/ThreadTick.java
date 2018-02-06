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
public enum ThreadTick {

    TICK(1), TICK_HALF(11), TICK_FULL(21);

    private long tick;

    ThreadTick(long tick) {
        this.tick = tick;
    }

    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }



}
