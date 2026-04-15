package com.learn.crazyjava.lesson16_concurrent;

import java.util.concurrent.*;

/**
 * 第16课：并发编程 - ExecutorService
 */
public class ExecutorDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // FixedThreadPool
        ExecutorService fixedPool = Executors.newFixedThreadPool(4);

        // 提交任务
        Future<?> future = fixedPool.submit(() -> {
            System.out.println("任务执行中...");
        });

        // Callable带返回值
        Future<Integer> intFuture = fixedPool.submit(() -> {
            Thread.sleep(1000);
            return 42;
        });
        System.out.println("Callable结果：" + intFuture.get());

        fixedPool.shutdown();
        if (!fixedPool.awaitTermination(10, TimeUnit.SECONDS)) {
            fixedPool.shutdownNow();
        }

        // ScheduledThreadPool
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(2);
        scheduledPool.schedule(() -> System.out.println("延迟任务执行"), 2, TimeUnit.SECONDS);
        scheduledPool.shutdown();
    }
}
