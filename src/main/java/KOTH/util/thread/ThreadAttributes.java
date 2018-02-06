package KOTH.util.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike (c) 2018. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 1/29/2018 at 11:04 PM.
 */
public class ThreadAttributes {

    public List<Threaded> threadPool;
    public List<Threaded> toBeAdded;
    public List<Threaded> toBeRemoved;

    public ThreadAttributes() {
        this.threadPool = new ArrayList<Threaded>();
        this.toBeAdded = new ArrayList<Threaded>();
        this.toBeRemoved = new ArrayList<Threaded>();
    }

    public ThreadAttributes addAll(List<Threaded> threads) {
        this.toBeAdded.addAll(threads);
        return this;
    }

    public ThreadAttributes addNewThread(Threaded thread) {
        this.toBeAdded.add(thread);
        return this;
    }

    public ThreadAttributes removeAll(List<Threaded> threads) {
        this.toBeRemoved.removeAll(threads);
        return this;
    }

    public ThreadAttributes removeThread(Threaded thread) {
        this.toBeRemoved.add(thread);
        return this;
    }

    public ThreadAttributes flushThreads() {
        this.threadPool.addAll(this.toBeAdded);
        this.toBeAdded.clear();
        this.threadPool.removeAll(this.toBeRemoved);
        this.toBeRemoved.clear();
        return this;
    }

}
