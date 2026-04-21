package com.ssitao.code.designpattern.threadpool.basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 线程池基础示例
 *
 * 线程池核心概念：
 * 1. 减少线程创建/销毁开销
 * 2. 控制并发数量
 * 3. 复用线程执行任务
 *
 * JDK线程池类型：
 * - FixedThreadPool: 固定线程数
 * - CachedThreadPool: 可扩展线程池
 * - SingleThreadExecutor: 单线程池
 * - ScheduledThreadPool: 定时任务线程池
 */
public class ThreadPoolBasicDemo {

    public static void main(String[] args) {
        System.out.println("=== 线程池基础示例 ===\n");

        // 1. 固定线程池
        System.out.println("1. 固定线程池 FixedThreadPool");
        fixedThreadPoolDemo();

        // 2. 可缓存线程池
        System.out.println("\n2. 可缓存线程池 CachedThreadPool");
        cachedThreadPoolDemo();

        // 3. 单线程线程池
        System.out.println("\n3. 单线程线程池 SingleThreadExecutor");
        singleThreadPoolDemo();
    }

    /**
     * 固定线程池示例
     */
    private static void fixedThreadPoolDemo() {
        // 创建3个固定线程的线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 提交10个任务
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("任务" + taskId + " 开始，线程: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500); // 模拟任务执行
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务" + taskId + " 完成");
            });
        }

        // 关闭线程池
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 可缓存线程池示例
     */
    private static void cachedThreadPoolDemo() {
        // 线程数可扩展，60秒空闲回收
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("任务" + taskId + " 执行，线程: " + Thread.currentThread().getName());
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单线程线程池示例
     */
    private static void singleThreadPoolDemo() {
        // 只有一个线程，保证任务顺序执行
        ExecutorService executor = Executors.newSingleThreadExecutor();

        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("任务" + taskId + " 执行，线程: " + Thread.currentThread().getName());
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
