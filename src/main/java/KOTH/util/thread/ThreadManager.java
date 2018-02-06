package KOTH.util.thread;

import KOTH.Main;
import KOTH.util.chat.log.LogLevel;
import KOTH.util.chat.log.LogUtil;

import java.util.ArrayList;
import java.util.HashSet;
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
public class ThreadManager {

    private static ThreadManager instance;
    public static ThreadManager get() {
        if (instance == null)
            instance = new ThreadManager();
        return instance;
    }

    HashSet<MultiThread> mThreadPool;
    List<MultiThread> toBeAdded;
    List<MultiThread> toBeRemoved;

    public ThreadManager() {
        this.mThreadPool = new HashSet<MultiThread>();
        this.toBeAdded = new ArrayList<MultiThread>();
        this.toBeRemoved = new ArrayList<MultiThread>();
    }

    public void queueThreads(ThreadTick tTick, int amount) {
        for (int i = 0 ; i < amount ; i++) {
            MultiThread thread = new MultiThread(Main.get(), new ThreadAttributes(), tTick);
            this.addThread(thread);
            thread.runTaskTimer(Main.get(), 0L, tTick.getTick());
            LogUtil.log(LogLevel.INFO,"Adding " + (i+1) + "/" + amount + " threads to thread pool. #" + thread.hashCode());
        }
        flushAll();
    }

    public MultiThread queueThread(ThreadTick tTick) {
        MultiThread thread = new MultiThread(Main.get(), new ThreadAttributes(), tTick);
        this.addThread(thread);
        thread.runTaskTimer(Main.get(), 0L, tTick.getTick());
        LogUtil.log(LogLevel.INFO,"Adding 1/1 threads to thread pool. #" + thread.hashCode());
        return thread;
    }

    public MultiThread getFreeThread(ThreadTick tTick) {
        for (MultiThread thread : mThreadPool) {
            if (thread.getAttributes().threadPool.size() <= 4 && thread.getAttributes().toBeAdded.size() <= 4 && thread.getThreadTick() == tTick) {
                LogUtil.log(LogLevel.INFO,"Thread #" + thread.hashCode() + " is free and chosen.");
                return thread;
            }
        }
        clearStressedThreads();
        return queueThread(tTick);
    }

    public MultiThread getFreeThreadSilent(ThreadTick tTick) {
        for (MultiThread thread : mThreadPool) {
            if (thread.getAttributes().threadPool.size() <= 4 && thread.getAttributes().toBeAdded.size() <= 4 && thread.getThreadTick() == tTick) {
                LogUtil.log(LogLevel.INFO,"Thread #" + thread.hashCode() + " is free and chosen.");
                return thread;
            }
        }
        return queueThread(tTick);
    }

    public void clearStressedThread(MultiThread thread) {
        List<Threaded> newThreaded = new ArrayList<Threaded>();
        MultiThread newThread = getFreeThread(thread.getThreadTick());
        int total = 0;
        for (int i = 0 ; i < thread.getAttributes().threadPool.size()-3 ; i++) {
            if (newThread.getAttributes().threadPool.size() == 4) {
                newThread = getFreeThreadSilent(thread.getThreadTick());
            }
            Threaded threaded = thread.getAttributes().threadPool.get(i);
            if (threaded != null) {
                newThreaded.add(threaded);
                newThread.getAttributes().addNewThread(threaded);
                thread.getAttributes().removeThread(threaded);
                total++;
            }
        }
        LogUtil.log(LogLevel.INFO,"Stressed thread #" + thread.hashCode() + " was moved to thread #" + newThread.hashCode() + " [" + total + "]");
    }

    public void clearStressedThreads() {
        for (MultiThread thread : mThreadPool) {
            if (thread.getAttributes().threadPool.size() >= 5) {
                LogUtil.log(LogLevel.INFO,"Attempting to clear thread #" + thread.hashCode());
                clearStressedThread(thread);
            }
        }
        flushAll();
    }

    public void showThreads() {
        for (MultiThread thread : mThreadPool) {
            LogUtil.log("Thread #" + thread.hashCode() + " size " + thread.getAttributes().threadPool.size() + "/4");
        }
    }

    public void flushAll() {
        this.addAll(this.toBeAdded);
        this.toBeAdded.clear();
        this.removeAll(this.toBeRemoved);
        this.toBeRemoved.clear();
    }

    @Deprecated
    private void addAll(List<MultiThread> add) {
        for (MultiThread thread : add) {
            if (!mThreadPool.contains(thread))
                mThreadPool.add(thread);
        }
    }

    public void addThread(MultiThread thread) {
        this.toBeAdded.add(thread);
    }

    @Deprecated
    private void removeAll(List<MultiThread> add) {
        for (MultiThread thread : add) {
            if (mThreadPool.contains(thread))
                mThreadPool.remove(thread);
        }
    }

    public void removeThread(MultiThread thread) {
        thread.getAttributes().removeAll(thread.getAttributes().threadPool);
        this.toBeRemoved.add(thread);
    }

    public MultiThread executeThreadIn(final Threaded threaded, int seconds) {
        MultiThread thread = queueThread(ThreadTick.TICK);
        thread.getAttributes().addNewThread(new DelayedThread(thread, threaded, seconds, System.currentTimeMillis()));
        thread.getAttributes().addNewThread(threaded);
        return thread;
    }

    public void removeAll() {
        for (MultiThread multiThread : mThreadPool) {
            multiThread.cancel();
        }
        toBeRemoved.addAll(mThreadPool);
        flushAll();
    }

    class DelayedThread extends Threaded {
        MultiThread thread;
        Threaded threaded;
        int seconds;
        long time;
        public DelayedThread(MultiThread thread, Threaded threaded, int seconds, long time) {
            super(thread);
            this.thread = thread;
            this.threaded = threaded;
            this.seconds = seconds;
            this.time = time;
        }

        public boolean executeTick() {
            if (super.elapsed(time, (1000)*this.seconds)) {
                this.threaded.executeWhenDone();
                ThreadManager.get().removeThread(this.thread);
                this.thread.cancel();
                try {this.finalize();} catch (Throwable e) {}
            }
            return true;
        }
        int sec;
        public boolean executeToSeconds() {
            sec++;
            if (sec == 61) sec=0;
            return true;
        }
        int min;
        public boolean executeToMinutes() {
            min++;
            if (min == 61) min=0;
            return true;
        }
        int hour;
        public boolean executeToHours() {
            hour++;
            return true;
        }

        public String timeElapsed() {
            return sec + "S " + min + "M " + hour + "H";
        }

    }

}
