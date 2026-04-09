package com.concurrency.chapter4.task;

import java.util.concurrent.*;

/**
 * Callable与Future示例 - 获取任务执行结果
 */
public class CallableFutureDemo {

    public static void demo() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 创建Callable任务
        Callable<Integer> task1 = () -> {
            System.out.println("Task1 开始计算...");
            Thread.sleep(1000);
            return 42;
        };

        Callable<String> task2 = () -> {
            System.out.println("Task2 开始处理...");
            Thread.sleep(500);
            return "Hello from Task2!";
        };

        // 提交任务并获取Future
        Future<Integer> future1 = executor.submit(task1);
        Future<String> future2 = executor.submit(task2);

        // 获取结果（会阻塞直到结果可用）
        System.out.println("等待Task1结果...");
        Integer result1 = future1.get(); // 阻塞等待
        System.out.println("Task1结果: " + result1);

        System.out.println("等待Task2结果...");
        String result2 = future2.get(); // 阻塞等待
        System.out.println("Task2结果: " + result2);

        executor.shutdown();
    }
}
