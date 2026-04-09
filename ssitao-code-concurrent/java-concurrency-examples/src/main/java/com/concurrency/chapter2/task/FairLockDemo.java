package com.concurrency.chapter2.task;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁与非公平锁示例
 */
public class FairLockDemo {

    public static void demo() throws InterruptedException {
        System.out.println("=== 公平锁示例 ===");
        fairLockDemo();

        Thread.sleep(1000);

        System.out.println("\n=== 非公平锁示例 ===");
        unfairLockDemo();
    }

    private static void fairLockDemo() throws InterruptedException {
        Lock fairLock = new ReentrantLock(true); // true表示公平锁

        for (int i = 1; i <= 3; i++) {
            final int num = i;
            new Thread(() -> {
                fairLock.lock();
                try {
                    System.out.println("线程" + num + " 获取锁 - 公平锁保证按顺序");
                } finally {
                    fairLock.unlock();
                }
            }, "Fair-" + i).start();
        }

        Thread.sleep(500);
    }

    private static void unfairLockDemo() throws InterruptedException {
        Lock unfairLock = new ReentrantLock(false); // false表示非公平锁

        for (int i = 1; i <= 3; i++) {
            final int num = i;
            new Thread(() -> {
                unfairLock.lock();
                try {
                    System.out.println("线程" + num + " 获取锁 - 非公平锁可能插队");
                } finally {
                    unfairLock.unlock();
                }
            }, "Unfair-" + i).start();
        }

        Thread.sleep(500);
    }
}
