package com.learn.crazyjava.lesson11_thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * 第11课：多线程 - 同步演示
 */
public class SyncDemo {
    private int count = 0;
    private final AtomicInteger atomicCount = new AtomicInteger(0);

    // synchronized方法
    public synchronized void incrementSync() {
        count++;
    }

    // 使用原子变量
    public void incrementAtomic() {
        atomicCount.incrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {
        SyncDemo demo = new SyncDemo();

        // 测试synchronized
        CountDownLatch latch1 = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                demo.incrementSync();
                latch1.countDown();
            }).start();
        }
        latch1.await();
        System.out.println("synchronized count = " + demo.count);

        // 测试AtomicInteger
        CountDownLatch latch2 = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                demo.incrementAtomic();
                latch2.countDown();
            }).start();
        }
        latch2.await();
        System.out.println("atomic count = " + demo.atomicCount.get());
    }
}
