package com.gao.four;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * User: wangchen.gpx
 * Date: 13-8-13
 * Time: 下午11:17
 */
public class ConcurrentTotalFileSize {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        long start = System.nanoTime();

        ConcurrentTotalFileSize totalFileSize = new ConcurrentTotalFileSize();

        long totalFileSize1 = totalFileSize.getTotalFileSize("G:\\go_path");

        long end = System.nanoTime();
        System.out.println(totalFileSize1);
        System.out.println((end -start) / 1.0e9);
    }

    public  long getTotalFileSize(String file) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        File file1 = new File(file);

        long total = 0;
        ArrayList<File> files = new ArrayList<File>();
        files.add(file1);
        while(!files.isEmpty()){
            ArrayList<Future<SubDirectoryDO>> futureArrayList = new ArrayList<Future<SubDirectoryDO>>();
            for (final File file2 : files) {
                futureArrayList.add(executorService.submit(new Callable<SubDirectoryDO>() {
                    @Override
                    public SubDirectoryDO call() throws Exception {
                        return getSubDirc(file2);
                    }
                }));
            }

            files.clear();

            for (Future<SubDirectoryDO> future : futureArrayList) {
                SubDirectoryDO subDirectoryDO = future.get(100, TimeUnit.MILLISECONDS);
                files.addAll(subDirectoryDO.subDirectories);
                total += subDirectoryDO.size;
            }
        }
        return total;
    }

    public  SubDirectoryDO getSubDirc(final File file){
        long total = 0;
        List<File> doArrayList = new ArrayList<File>();
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files != null){
                for (File file1 : files) {
                    if (file1.isFile()){
                        total += file1.length();
                    }else{
                        doArrayList.add(file1);
                    }
                }
            }
        }
        return new SubDirectoryDO(total, doArrayList);
    }

    class SubDirectoryDO{
        public long size;
        public List<File> subDirectories;

        SubDirectoryDO(long size, List<File> subDirectories) {
            this.size = size;
            this.subDirectories = subDirectories;
        }
    }
}
