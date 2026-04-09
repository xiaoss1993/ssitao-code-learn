package com.concurrency.chapter5.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * Fork/Join异常处理示例
 */
public class ForkJoinExceptionDemo {

    public static void demo() throws InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();

        RecursiveTask<Integer> task = new RecursiveTask<Integer>() {
            @Override
            protected Integer compute() {
                int value = (int) (Math.random() * 10);
                if (value < 3) {
                    throw new RuntimeException("任务生成随机数 " + value + " 小于3，模拟异常");
                }
                System.out.println("正常计算: " + value);
                return value;
            }
        };

        try {
            pool.execute(task);
            Integer result = task.get();
            System.out.println("结果: " + result);
        } catch (Exception e) {
            System.out.println("捕获异常: " + e.getCause().getMessage());
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.SECONDS);
    }
}
