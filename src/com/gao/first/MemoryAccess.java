package com.gao.first;

/**
 * User: wangchen.gpx
 * Date: 13-8-12
 * Time: 下午7:36
 */
public class MemoryAccess {
    public static void main(String[] args) {
        int[] atts = new int[64 * 1024 * 512];

        long start1 = System.currentTimeMillis();
        for (int i = 0 ; i< atts.length ; i++){
            atts[i] *= 3;
        }

        System.out.println("first :" + (System.currentTimeMillis() - start1));

        long start2 = System.currentTimeMillis();
        for (int i = 0 ;i < atts.length ; i += 8){
            atts[i] *= 3;
        }
        System.out.println("second:" + (System.currentTimeMillis() - start2));
    }
}
