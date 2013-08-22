package com.gao.five;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: wangchen.gpx
 * Date: 13-8-22
 * Time: 下午9:39
 */
public class EnerySourceAtomic {
    private final Integer MAXLEVEL = 100;
    private AtomicInteger level = new AtomicInteger(MAXLEVEL);
    private static ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(10 , new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    return thread;
                }
            });
    private ScheduledFuture<?> scheduledFuture;


    private EnerySourceAtomic(){}

    private void init(){
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                repleash();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public boolean useEnery(int amount) {
        if (amount > 0 && amount <= level.get()) {
            level.compareAndSet(level.get(), level.get() - amount);
            return true;
        }
        return false;
    }

    public void stop() {
        scheduledFuture.cancel(false);
    }

    public EnerySourceAtomic create(){
        EnerySourceAtomic enerySourceAtomic = new EnerySourceAtomic();
        enerySourceAtomic.init();
        return enerySourceAtomic;
    }

    private void repleash() {
        if (level.get() < MAXLEVEL)
            level.incrementAndGet();
    }
}
