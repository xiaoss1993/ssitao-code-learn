package com.concurrency.chapter3.task;

import java.util.concurrent.Semaphore;

/**
 * Semaphore信号量示例 - 控制资源并发访问
 */
public class SemaphoreDemo {

    private static final Semaphore semaphore = new Semaphore(3); // 最多3个并发

    public static void demo() throws InterruptedException {
        System.out.println("模拟3个打印队列，但只有2台打印机...");

        // 模拟5个打印任务
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            new Thread(() -> {
                try {
                    System.out.println("任务" + taskId + " 等待获取打印机...");
                    semaphore.acquire(); // 获取许可
                    System.out.println("任务" + taskId + " 开始打印...");
                    Thread.sleep(1000); // 模拟打印
                    System.out.println("任务" + taskId + " 打印完成!");
                    semaphore.release(); // 释放许可
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Task-" + i).start();
        }

        Thread.sleep(6000);
    }
}
