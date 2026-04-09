package com.concurrency.chapter6.task;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap并发映射示例
 */
public class ConcurrentHashMapDemo {

    public static void demo() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // 多个线程同时写入
        Thread[] writers = new Thread[3];
        for (int i = 0; i < 3; i++) {
            final int threadId = i;
            writers[i] = new Thread(() -> {
                for (int j = 1; j <= 100; j++) {
                    map.put("key" + j, threadId * 100 + j);
                }
            }, "Writer-" + i);
            writers[i].start();
        }

        // 多个线程同时读取
        Thread[] readers = new Thread[3];
        for (int i = 0; i < 3; i++) {
            readers[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    Integer value = map.get("key" + (j % 100 + 1));
                    if (value != null) {
                        // 读取操作
                    }
                }
            }, "Reader-" + i);
            readers[i].start();
        }

        for (Thread t : writers) t.join();
        for (Thread t : readers) t.join();

        System.out.println("ConcurrentHashMap大小: " + map.size());
        System.out.println("示例键值对: key1=" + map.get("key1") + ", key50=" + map.get("key50"));
    }
}
