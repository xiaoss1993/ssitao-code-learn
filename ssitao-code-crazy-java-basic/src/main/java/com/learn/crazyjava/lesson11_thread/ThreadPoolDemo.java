package com.learn.crazyjava.lesson11_thread;

import java.util.concurrent.*;

/**
 * 第11课：多线程 - 线程池
 */
public class ThreadPoolDemo {
    public static void main(String[] args) {
        // 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(4);

        // 提交多个任务
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            pool.execute(() -> {
                System.out.println("任务" + taskId + "由" +
                    Thread.currentThread().getName() + "执行");
            });
        }

        // 关闭线程池
        pool.shutdown();

        // 使用ThreadPoolExecutor
        ThreadPoolExecutor customPool = new ThreadPoolExecutor(
            2,                      // corePoolSize
            4,                      // maximumPoolSize
            60,                     // keepAliveTime
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        customPool.execute(() -> System.out.println("自定义线程池任务"));
        customPool.shutdown();
    }
}
