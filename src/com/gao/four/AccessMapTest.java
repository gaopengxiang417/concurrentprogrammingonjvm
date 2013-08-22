package com.gao.four;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: wangchen.gpx
 * Date: 13-8-22
 * Time: 下午8:32
 * 验证普通的map，synchronizedmap，concurrentmap在迭代的时候修改元素的不同行为
 */
public class AccessMapTest {
    public static void main(String[] args) {
        System.out.println("plain map........");
        accessMap(new HashMap<String, Object>());

        System.out.println("synchronizedmap.....");
        accessMap(Collections.synchronizedMap(new HashMap<String, Object>()));

        System.out.println("concurrent map...");
        accessMap(new ConcurrentHashMap<String, Object>());
    }

    public static void accessMap(final Map<String,Object> map){
        map.put("fould" , 44);
        map.put("chans" , 11);

        try {
            for (final String s : map.keySet()) {
                System.out.println("value : " + s + ":" + map.get(s));
                map.put("third" , 90);
            }
        } catch (Exception e) {
            System.out.println("throw exception......");
        }

        System.out.println("total size is : " + map.size());
    }
}
