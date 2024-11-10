package com.ztiany.basic.map;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMap_Safety_Demo {

    private final static Map<String, String> map1 = Collections.synchronizedMap(new HashMap<>());

    private final static Map<String, String> map2 = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        doTest("map1", map1);
        doTest("map2", map2);
    }

    private static void doTest(String tag, Map<String, String> map) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            final int index = i;
            Thread thread = new Thread(
                    () -> map.put("key", tag + " value: " + index)
            );
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(map.get("key"));
    }

}