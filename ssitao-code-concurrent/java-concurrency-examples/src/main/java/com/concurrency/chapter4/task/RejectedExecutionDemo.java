package com.concurrency.chapter4.task;

import java.util.concurrent.*;

/**
 * 自定义拒绝策略示例
 */
public class RejectedExecutionDemo {

    public static void demo() throws InterruptedException {
        // 创建只有1个线程的线程池
        ExecutorService executor = new ThreadPoolExecutor(
                1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(2),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println("任务被拒绝: " + r + " - 执行自定义拒绝策略");
                        // 自定义策略：打印日志或将任务保存到其他队列
                    }
                }
        );

        System.out.println("线程池配置: 核心线程=1, 最大线程=1, 队列容量=2\n");

        // 提交超过容量的任务
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            try {
                executor.submit(() -> {
                    System.out.println("任务" + taskId + " 开始执行");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("任务" + taskId + " 执行完成");
                });
            } catch (RejectedExecutionException e) {
                System.out.println("任务" + taskId + " 被拒绝执行");
            }
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
