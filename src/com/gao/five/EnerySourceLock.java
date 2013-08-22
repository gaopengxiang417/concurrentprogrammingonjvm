package com.gao.five;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: wangchen.gpx
 * Date: 13-8-22
 * Time: 下午9:49
 */
public class EnerySourceLock {
    private static final Integer MAXLEVEL = 100;
    private int level = MAXLEVEL;
    private int useage = 0;
    private ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(10);
    private final ReentrantReadWriteLock reentrantLock = new ReentrantReadWriteLock();
    private ScheduledFuture<?> scheduledFuture;


    private EnerySourceLock(){}

    private void init() {
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                repleash();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }


    public void stop() {
        scheduledFuture.cancel(false);
    }

    public int getUseage(){
        reentrantLock.readLock().lock();
        try{
            return useage;
        }finally {
            reentrantLock.readLock().unlock();
        }
    }

    public boolean userEnergy(int amount){
        reentrantLock.writeLock().lock();
        try{
            if (amount > 0 && amount <= level){
                level -= amount;
                useage++;
                return true;
            }
            return false;
        }finally {
            reentrantLock.writeLock().unlock();
        }
    }

    public EnerySourceLock createEnery(){
        EnerySourceLock enerySourceLock = new EnerySourceLock();
        enerySourceLock.init();
        return enerySourceLock;
    }

    private void repleash() {
        reentrantLock.writeLock().lock();
        try{
            if (level < MAXLEVEL)
                level++;
        }finally {
            reentrantLock.writeLock().unlock();
        }
    }
}
