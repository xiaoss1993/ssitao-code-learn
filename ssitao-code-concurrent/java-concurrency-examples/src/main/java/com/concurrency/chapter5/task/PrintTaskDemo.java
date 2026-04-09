package com.concurrency.chapter5.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * PrintTask示例 - RecursiveAction无返回值
 */
public class PrintTaskDemo {

    public static void demo() throws InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();

        System.out.println("Fork/Join树形任务执行：\n");

        PrintTask rootTask = new PrintTask("Root", 32);
        pool.invoke(rootTask);

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.SECONDS);
    }
}
