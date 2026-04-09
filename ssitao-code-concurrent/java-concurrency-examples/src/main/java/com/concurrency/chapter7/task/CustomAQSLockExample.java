package com.concurrency.chapter7.task;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 基于AQS的自定义锁示例
 */
public class CustomAQSLockExample {

    public static void demo() throws InterruptedException {
        CustomLock lock = new CustomLock();

        // 演示锁的获取和释放
        System.out.println("主线程获取锁...");
        lock.lock();
        System.out.println("主线程已获取锁\n");

        Thread t1 = new Thread(() -> {
            System.out.println("线程1 尝试获取锁...");
            lock.lock();
            System.out.println("线程1 获取到锁!");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            lock.unlock();
            System.out.println("线程1 释放锁");
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            System.out.println("线程2 尝试获取锁...");
            lock.lock();
            System.out.println("线程2 获取到锁!");
            lock.unlock();
            System.out.println("线程2 释放锁");
        }, "Thread-2");

        t1.start();
        Thread.sleep(100);
        t2.start();

        Thread.sleep(500);
        System.out.println("\n主线程释放锁\n");
        lock.unlock();

        t1.join();
        t2.join();

        System.out.println("\n自定义锁示例完成");
    }

    /**
     * 基于AQS的自定义互斥锁
     */
    static class CustomLock {
        private final Sync sync = new Sync();

        public void lock() {
            sync.acquire(1);
        }

        public void unlock() {
            sync.release(1);
        }

        static class Sync extends AbstractQueuedSynchronizer {
            @Override
            protected boolean tryAcquire(int arg) {
                // 尝试获取锁，如果状态为0则获取成功
                return compareAndSetState(0, 1);
            }

            @Override
            protected boolean tryRelease(int arg) {
                // 释放锁，将状态设为0
                setState(0);
                return true;
            }
        }
    }
}
