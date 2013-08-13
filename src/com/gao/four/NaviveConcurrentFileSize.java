package com.gao.four;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * User: wangchen.gpx
 * Date: 13-8-13
 * Time: 下午11:01
 */
public class NaviveConcurrentFileSize {

    public static void main(String[] args) {
        long start = System.nanoTime();

        long totalfilesize = totalfilesize("G:\\go_path");

        long end = System.nanoTime();
        System.out.println("total:" + totalfilesize);
        System.out.println((end - start) / 1.0e9);
    }

    public static long totalfilesize(String fileName) {
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        try {
            return computeFileSize(executorService, new File(fileName));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        return 0;
    }

    public static long computeFileSize(final ExecutorService executorService, final File file) throws InterruptedException, ExecutionException, TimeoutException {
        if (file.isFile())
            return file.length();

        File[] files = file.listFiles();
        long count = 0;
        ArrayList<Future<Long>> arrayList = new ArrayList<Future<Long>>();
        if (files != null) {

            for (final File file1 : files) {
                arrayList.add(executorService.submit(new Callable<Long>() {
                    @Override
                    public Long call() throws Exception {
                        return computeFileSize(executorService, file1);
                    }
                }));
            }
            for (Future<Long> future : arrayList) {
                count += future.get(100L , TimeUnit.SECONDS);
            }
        }
        return count;
    }
}
