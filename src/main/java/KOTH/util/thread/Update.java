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
public class Update extends Threaded {

    int time = 0;

    public Update(SThread thread) {
        super(thread);
    }

    @Override
    public boolean executeToSeconds() {
        if (time == 10) {
            ThreadManager.get().clearStressedThreads();
            time = 0;
        }
        time++;
        return true;
    }
}
