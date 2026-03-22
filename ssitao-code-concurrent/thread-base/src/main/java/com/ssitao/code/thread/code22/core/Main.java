package com.ssitao.code.thread.code22.core;


import com.ssitao.code.thread.code22.task.Job;
import com.ssitao.code.thread.code22.task.PrintQueue;

/**
 * 主程序入口类
 * 演示使用信号量(Semaphore)控制对共享资源的并发访问
 *
 * 本例展示了经典的"打印队列"场景：
 * - 12个线程同时需要使用打印机
 * - 只有3台打印机可用
 * - 通过信号量控制对打印机的访问，确保最多只有3个线程同时打印
 */
public class Main {
    public static void main(String[] args) {
        // 创建一个打印队列对象，管理3台打印机的访问
        PrintQueue printQueue = new PrintQueue();

        // 创建12个线程，每个线程都执行Job任务
        // 这些任务都会向同一个打印队列对象发出打印请求
        // 由于只有3台打印机，所以需要通过信号量来协调访问
        Thread thread[] = new Thread[12];
        for (int i = 0; i < 12; i++) {
            // 每个线程都会被分配一个Job实例，Job持有printQueue的引用
            thread[i] = new Thread(new Job(printQueue), "Thread-" + i);
        }

        // 启动所有12个线程
        // 注意：线程启动顺序不受控制，哪个线程先获得CPU时间片是不确定的
        // 信号量会自动管理访问，确保最多只有3个线程同时使用打印机
        for (int i = 0; i < 12; i++) {
            thread[i].start();
        }
    }
}
