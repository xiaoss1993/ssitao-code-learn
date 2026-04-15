package com.learn.crazyjava.lesson16_concurrent;

import java.util.concurrent.*;

/**
 * 第16课：并发编程 - CountDownLatch
 */
public class ConcurrentDemo {
    public static void main(String[] args) throws InterruptedException {
        int workerCount = 3;
        CountDownLatch latch = new CountDownLatch(workerCount);

        for (int i = 0; i < workerCount; i++) {
            final int workerId = i;
            new Thread(() -> {
                try {
                    System.out.println("工人" + workerId + "开始工作");
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println("工人" + workerId + "完成工作");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        System.out.println("所有工人都完成了，监工开始检查");
    }
}
