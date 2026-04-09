package com.concurrency.chapter7.task;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义RejectedExecutionHandler示例
 */
public class CustomRejectedHandlerExample {

    public static void demo() throws InterruptedException {
        // 创建只有1个线程的线程池，队列容量为2
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(2),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println("!!! 任务被拒绝执行: " + r);
                        // 自定义拒绝策略：将任务保存到列表
                        System.out.println("!!! 采用自定义策略: 稍后重试或记录");
                    }
                }
        );

        System.out.println("线程池配置: 核心=1, 最大=1, 队列容量=2");
        System.out.println("提交7个任务（超出容量）...\n");

        for (int i = 1; i <= 7; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("任务" + taskId + " 开始执行");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
                System.out.println("任务" + taskId + " 执行完成");
            });
        }

        Thread.sleep(2000);
        executor.shutdown();
    }
}
