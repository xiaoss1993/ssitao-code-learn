package com.concurrency.chapter7.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义ThreadFactory示例
 */
public class CustomThreadFactoryExample implements ThreadFactory {

    private final String namePrefix;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final boolean isDaemon;

    public CustomThreadFactoryExample(String namePrefix, boolean isDaemon) {
        this.namePrefix = namePrefix;
        this.isDaemon = isDaemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, namePrefix + "-Thread-" + threadNumber.getAndIncrement());
        thread.setDaemon(isDaemon);
        thread.setPriority(Thread.NORM_PRIORITY);
        return thread;
    }

    public static void demo() {
        // 创建自定义线程工厂
        CustomThreadFactoryExample factory = new CustomThreadFactoryExample("CustomPool", false);

        // 使用工厂创建线程池
        java.util.concurrent.ExecutorService executor =
                java.util.concurrent.Executors.newFixedThreadPool(3, factory);

        System.out.println("使用自定义ThreadFactory创建线程池...\n");

        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                Thread t = Thread.currentThread();
                System.out.println("任务" + taskId + " 由 " + t.getName() +
                        " (守护线程=" + t.isDaemon() + ") 执行");
            });
        }

        executor.shutdown();
    }
}
