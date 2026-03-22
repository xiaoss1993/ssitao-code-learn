package com.ssitao.code.thread.code21.core;

import com.ssitao.code.thread.code21.task.Job;
import com.ssitao.code.thread.code21.task.PrintQueue;

/**
 * 主启动类
 * 演示使用信号量(Semaphore)控制多线程对共享资源的访问
 */
public class Main {
    public static void main(String[] args) {
        // 创建一个打印队列对象（共享资源）
        PrintQueue printQueue = new PrintQueue();

        // 创建10个线程来模拟10个用户同时打印
        Thread thread[] = new Thread[10];
        for (int i = 0; i < 10; i++) {
            // 每个线程都创建一个Job任务，共享同一个PrintQueue
            thread[i] = new Thread(new Job(printQueue), "Thread-" + i);
        }

        // 启动10个线程，由于Semaphore只允许1个线程同时访问打印队列
        // 所以10个线程会竞争获取信号量，获取到的才能执行打印
        for (int i = 0; i < 10; i++) {
            thread[i].start();
        }
    }
}
