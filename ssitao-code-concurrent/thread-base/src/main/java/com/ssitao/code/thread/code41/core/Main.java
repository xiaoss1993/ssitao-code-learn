package com.ssitao.code.thread.code41.core;


import com.ssitao.code.thread.code41.task.Task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Fork/Join 框架演示：任务拆分与异常处理
 *
 * 本示例展示：
 * 1. 如何使用 ForkJoinPool 执行任务
 * 2. 如何处理 Fork/Join 任务中抛出的异常
 * 3. ForkJoinPool 特有的异常处理机制
 */
public class Main {
    public static void main(String[] args) {
        // ========== 1. 创建待处理的数组 ==========
        int array[] = new int[100];

        // ========== 2. 创建任务 ==========
        // 任务将处理数组 array[0] 到 array[99]
        Task task = new Task(array, 0, 100);

        // ========== 3. 创建 ForkJoinPool 并执行任务 ==========
        ForkJoinPool pool = new ForkJoinPool();
        pool.execute(task);  // 异步执行，不阻塞

        // ========== 4. 关闭线程池 ==========
        pool.shutdown();

        // ========== 5. 等待任务完成 ==========
        // awaitTermination() 阻塞直到：
        // - 任务完成，或
        // - 超过指定的等待时间（1天）
        try {
            pool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ========== 6. 异常处理 ==========
        // ForkJoinPool 不会直接抛出异常，需要通过任务对象的方法检测
        if (task.isCompletedAbnormally()) {
            // isCompletedAbnormally(): 判断任务是否因异常而完成
            System.out.printf("Main: An exception has occurred\n");
            // getException(): 获取任务抛出的异常
            System.out.printf("Main: %s\n", task.getException());
        }

        // ========== 7. 获取任务结果 ==========
        System.out.printf("Main: Result: %d", task.join());
    }
}
