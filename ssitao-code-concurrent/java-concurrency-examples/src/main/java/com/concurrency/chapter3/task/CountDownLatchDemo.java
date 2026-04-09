package com.concurrency.chapter3.task;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch倒计时门闩示例 - 等待多个并发事件完成
 */
public class CountDownLatchDemo {

    private static final int TASK_COUNT = 3;

    public static void demo() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(TASK_COUNT);

        System.out.println("主线程启动，等待" + TASK_COUNT + "个任务完成...");

        // 创建并启动3个任务
        for (int i = 1; i <= TASK_COUNT; i++) {
            final int taskId = i;
            new Thread(() -> {
                try {
                    System.out.println("任务" + taskId + " 开始执行...");
                    Thread.sleep((long) (Math.random() * 2000)); // 模拟不同耗时
                    System.out.println("任务" + taskId + " 执行完成!");
                    latch.countDown(); // 计数减1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Worker-" + taskId).start();
        }

        latch.await(); // 等待所有任务完成
        System.out.println("所有任务已完成，主线程继续执行!");
    }
}
