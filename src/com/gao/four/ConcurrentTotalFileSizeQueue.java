package com.gao.four;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: wangchen.gpx
 * Date: 13-8-20
 * Time: 下午8:52
 */
public class ConcurrentTotalFileSizeQueue {
    private ExecutorService executorService;
    private BlockingQueue<Long> fileSizes = new ArrayBlockingQueue<Long>(500);
    private AtomicLong visitTimes = new AtomicLong();

    public void startExploreDir(final File file){
        visitTimes.incrementAndGet();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                exploreDir(file);
            }
        });
    }

    private void exploreDir(File file) {
        long fileSize = 0;
        if (file.isFile()){
            fileSize = file.length();
        }else{
            File[] files = file.listFiles();
            if(files != null){
                for (File file1 : files) {
                    if (file1.isFile()){
                       fileSize += file1.length();
                    }else{
                        startExploreDir(file1);
                    }
                }
            }
        }
        try {
            fileSizes.put(fileSize);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        visitTimes.decrementAndGet();
    }

    public long getTotalFileSize(final String filename) throws InterruptedException {
        try {
            executorService = Executors.newFixedThreadPool(100);

            long totalSize = 0;
            startExploreDir(new File(filename));

            while (visitTimes.get() > 0 || fileSizes.size() > 0) {
                Long poll = fileSizes.poll(10, TimeUnit.SECONDS);
                totalSize += poll;
            }
            return totalSize;
        } finally {
            executorService.shutdown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.nanoTime();

        ConcurrentTotalFileSizeQueue concurrentTotalFileSizeQueue = new ConcurrentTotalFileSizeQueue();
        long totalFileSize = concurrentTotalFileSizeQueue.getTotalFileSize("G:\\go_path");
        System.out.println((System.nanoTime() - start)/1.0e9);
        System.out.println("total :" + totalFileSize);
    }
}
