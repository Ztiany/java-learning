package com.ztiany.basic.map;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {

    private final static Map<String, String> map1 = Collections.synchronizedMap(new HashMap<>());
    private final static Map<String, String> map2 = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            final int index = i;
            Thread thread = new Thread(
                    () -> map1.put("key", "value" + index)
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
        System.out.println(map1.get("key"));
    }

}
