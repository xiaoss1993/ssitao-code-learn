package com.ssitao.code.thread.code19.core;


import com.ssitao.code.thread.code19.task.Job;
import com.ssitao.code.thread.code19.task.PrintQueue;

/**
 * 主启动类
 * 演示ReentrantLock（可重入锁）的使用
 */
public class Main {
    public static void main(String[] args) {
        // 创建一个打印队列实例，作为共享资源
        PrintQueue printQueue = new PrintQueue();

        // 创建10个打印任务线程
        Thread thread[] = new Thread[10];
        for (int i = 0; i < 10; i++) {
            // 每个线程都绑定一个Job任务，Job会调用printQueue进行打印
            thread[i] = new Thread(new Job(printQueue), "Thread " + i);
        }

        // 依次启动每个线程，每隔0.1秒启动一个
        // 由于ReentrantLock是非公平锁（构造参数为false），
        // 线程获取锁的顺序不确定，不一定按启动顺序获得锁
        for (int i = 0; i < 10; i++) {
            thread[i].start();
            try {
                // 主线程休眠100毫秒后再启动下一个线程
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
