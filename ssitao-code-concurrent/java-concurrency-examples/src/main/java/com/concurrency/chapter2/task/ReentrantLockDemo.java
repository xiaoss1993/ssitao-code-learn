package com.concurrency.chapter2.task;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock锁示例
 */
public class ReentrantLockDemo {

    public static void demo() {
        Lock lock = new ReentrantLock();
        Counter counter = new Counter(lock);

        // 创建5个线程同时增加计数器
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment();
                }
            }, "Counter-Thread-" + i);
            threads[i].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("最终计数器值: " + counter.get());
        System.out.println("期望值: 5000");
    }

    static class Counter {
        private final Lock lock;
        private int count = 0;

        public Counter(Lock lock) {
            this.lock = lock;
        }

        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock(); // 确保锁被释放
            }
        }

        public int get() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }
}
