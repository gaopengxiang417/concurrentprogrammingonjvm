package com.gao.five;

import java.util.concurrent.*;

/**
 * User: wangchen.gpx
 * Date: 13-8-22
 * Time: 下午9:24
 */
public class EnerySourceTimer {

    private final Integer MAXLEVEL = 100;
    private Integer level = MAXLEVEL;
    private static final ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(10 , new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    return thread;
                }
            });
    private ScheduledFuture<?> scheduledFuture;

    private EnerySourceTimer(){}

    private void init() {
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                repleash();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public boolean useEnery(int amount){
        if (amount > 0 && amount <= level){
            level -= amount;
            return true;
        }
        return false;
    }

    public static EnerySourceTimer createSource(){
        EnerySourceTimer enerySourceTimer = new EnerySourceTimer();
        enerySourceTimer.init();
        return enerySourceTimer;
    }

    public void stopEnery(){
        scheduledFuture.cancel(false);
    }

    public int getAvaiableLevel(){
        return level;
    }

    private void repleash() {
        if (level < MAXLEVEL)
            level++;
    }

}
