package com.ssitao.code.thread.code17.core;

import com.ssitao.code.thread.code17.task.Job;
import com.ssitao.code.thread.code17.task.PrintQueue;

/**
 * 主启动类 - 演示ReentrantLock可重入锁的使用
 *
 * 本例展示了多个线程竞争同一个锁的场景：
 * - 10个线程同时尝试使用打印队列
 * - 由于锁的保护，打印操作互斥进行，一次只有一个线程能打印
 */
public class Main {
    public static void main(String[] args) {
        // 创建一个打印队列（共享资源）
        PrintQueue printQueue = new PrintQueue();

        // 创建10个打印任务线程
        Thread thread[] = new Thread[10];
        for (int i = 0; i < 10; i++) {
            // 每个线程都持有同一个打印队列的引用，会竞争同一把锁
            thread[i] = new Thread(new Job(printQueue), "Thread-" + i);
        }

        // 启动所有线程
        for (int i = 0; i < 10; i++) {
            thread[i].start();
        }
    }
}
