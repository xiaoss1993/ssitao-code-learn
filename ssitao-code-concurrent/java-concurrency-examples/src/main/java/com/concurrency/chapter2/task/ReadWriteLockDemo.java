package com.concurrency.chapter2.task;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁示例
 * 读操作可以并发执行，写操作需要独占访问
 */
public class ReadWriteLockDemo {

    public static void demo() {
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        SharedData data = new SharedData(rwLock);

        // 启动多个读线程
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    data.read();
                }
            }, "Reader-" + i).start();
        }

        // 启动多个写线程
        for (int i = 0; i < 2; i++) {
            final int value = i;
            new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    data.write(value * 100 + j);
                }
            }, "Writer-" + i).start();
        }

        // 等待所有线程完成
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class SharedData {
        private final ReadWriteLock rwLock;
        private int value = 0;

        public SharedData(ReadWriteLock rwLock) {
            this.rwLock = rwLock;
        }

        public void read() {
            rwLock.readLock().lock();
            try {
                System.out.println(Thread.currentThread().getName() + " 读取值: " + value);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rwLock.readLock().unlock();
            }
        }

        public void write(int newValue) {
            rwLock.writeLock().lock();
            try {
                System.out.println(Thread.currentThread().getName() + " 写入值: " + newValue);
                value = newValue;
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rwLock.writeLock().unlock();
            }
        }
    }
}
