package com.gao.four;

import java.io.File;

/**
 * User: wangchen.gpx
 * Date: 13-8-13
 * Time: 下午10:36
 * 顺序的计算指定的目录下的文件大小
 */
public class TotalFileSizeSequience {

    public static long getTotalSize(File file){
        if (file.isFile())
            return file.length();

        int total = 0;
        File[] files = file.listFiles();
        if (files != null){
            for (File file1 : files) {
                return total += getTotalSize(file1);
            }
        }
        return total;
    }

    public static void main(String[] args) {
        long start = System.nanoTime();

        long totalSize = getTotalSize(new File("G:\\go_path"));

        long end = System.nanoTime();
        System.out.println((totalSize ));
        System.out.println((end - start) / 1.0e9);
    }
}
