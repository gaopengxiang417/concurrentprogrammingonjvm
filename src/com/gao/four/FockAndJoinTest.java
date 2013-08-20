package com.gao.four;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * User: wangchen.gpx
 * Date: 13-8-20
 * Time: 下午9:18
 */
public class FockAndJoinTest {
    public static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public static class MyFileFinder extends RecursiveTask<Long>{
        private final File file;

        public MyFileFinder(File file) {
            this.file = file;
        }

        @Override
        protected Long compute() {
            long totalSize = 0;
            if (file.isFile()){
                totalSize = file.length();
            }else{
                File[] files = file.listFiles();
                if(files != null){
                    ArrayList<ForkJoinTask<Long>> forkJoinTasks = new ArrayList<>();
                    for (File file1 : files) {
                        if(file1.isFile()){
                            totalSize += file1.length();
                        }else{
                            forkJoinTasks.add(new MyFileFinder(file1));
                        }
                    }
                    Collection<ForkJoinTask<Long>> forkJoinTasks1 = invokeAll(forkJoinTasks);
                    for (ForkJoinTask<Long> longForkJoinTask : forkJoinTasks1) {
                        try {
                            totalSize += longForkJoinTask.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return totalSize;
        }
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        Long total = forkJoinPool.invoke(new MyFileFinder(new File("G:\\go_path")));
        System.out.println((System.nanoTime() - start) / 1e9);
        System.out.println(total);
    }
}
