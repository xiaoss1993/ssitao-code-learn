package com.concurrency.chapter5.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * 基础ForkJoinPool示例
 */
public class ForkJoinPoolDemo {

    public static void demo() throws InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();

        // 创建一个简单的递归动作
        RecursiveAction task = new RecursiveAction() {
            @Override
            protected void compute() {
                System.out.println("任务由 " + Thread.currentThread().getName() + " 执行");
            }
        };

        pool.execute(task);
        pool.awaitQuiescence(1, TimeUnit.SECONDS);
        pool.shutdown();
    }
}
