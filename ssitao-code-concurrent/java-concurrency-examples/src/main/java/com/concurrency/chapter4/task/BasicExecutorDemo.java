package com.concurrency.chapter4.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基础ExecutorService示例
 */
public class BasicExecutorDemo {

    public static void demo() throws InterruptedException {
        // 创建固定大小的线程池
        ExecutorService executor = Executors.newFixedThreadPool(4);

        System.out.println("提交5个任务到线程池...");

        // 提交Runnable任务
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("任务" + taskId + " 由 " + Thread.currentThread().getName() + " 执行");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 关闭执行器
        executor.shutdown();

        // 等待所有任务完成
        if (executor.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS)) {
            System.out.println("所有任务已完成!");
        }
    }
}
