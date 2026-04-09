package com.concurrency.chapter1.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用自定义线程工厂的示例
 */
public class CustomThreadFactoryDemo {

    public static void factoryDemo() {
        // 创建自定义线程工厂
        CustomThreadFactory factory = new CustomThreadFactory("CustomPool");

        // 使用工厂创建固定大小的线程池
        ExecutorService executor = Executors.newFixedThreadPool(3, factory);

        System.out.println("使用自定义线程工厂创建线程池...");

        // 提交任务
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("任务" + taskId + " 由 " + Thread.currentThread().getName() + " 执行");
            });
        }

        executor.shutdown();

        while (!executor.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("所有任务完成");
    }
}
