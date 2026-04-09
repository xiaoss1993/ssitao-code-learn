package com.concurrency.juc.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * AtomicInteger 源码分析 + 实际使用示例
 */
public class AtomicIntegerSourceAnalysis {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== AtomicInteger 源码分析 + 使用示例 ===\n");

        // 1. 基本操作示例
        System.out.println("【1. 基本操作示例】");
        basicOperations();

        // 2. 并发计数器示例
        System.out.println("\n【2. 并发计数器示例】");
        concurrentCounter();

        // 3. 模拟抢票系统
        System.out.println("\n【3. 模拟抢票系统】");
        ticketSelling();

        // 4. 统计任务完成数
        System.out.println("\n【4. 任务完成统计】");
        taskCompletionStats();
    }

    /**
     * 1. AtomicInteger 基本操作
     */
    private static void basicOperations() {
        AtomicInteger ai = new AtomicInteger(0);

        // getAndIncrement - 先返回后自增
        int v1 = ai.getAndIncrement();
        System.out.println("getAndIncrement(): 返回=" + v1 + ", 当前=" + ai.get());

        // incrementAndGet - 先自增后返回
        int v2 = ai.incrementAndGet();
        System.out.println("incrementAndGet(): 返回=" + v2 + ", 当前=" + ai.get());

        // getAndDecrement - 先返回后自减
        int v3 = ai.getAndDecrement();
        System.out.println("getAndDecrement(): 返回=" + v3 + ", 当前=" + ai.get());

        // decrementAndGet - 先自减后返回
        int v4 = ai.decrementAndGet();
        System.out.println("decrementAndGet(): 返回=" + v4 + ", 当前=" + ai.get());

        // getAndAdd
        int v5 = ai.getAndAdd(5);
        System.out.println("getAndAdd(5): 返回=" + v5 + ", 当前=" + ai.get());

        // addAndGet
        int v6 = ai.addAndGet(3);
        System.out.println("addAndGet(3): 返回=" + v6 + ", 当前=" + ai.get());

        // compareAndSet
        boolean success1 = ai.compareAndSet(8, 100);
        System.out.println("compareAndSet(8, 100): " + success1 + ", 当前=" + ai.get());

        boolean success2 = ai.compareAndSet(8, 200); // 会失败，因为当前是100
        System.out.println("compareAndSet(8, 200): " + success2 + ", 当前=" + ai.get());

        // getAndSet
        int oldValue = ai.getAndSet(500);
        System.out.println("getAndSet(500): 返回旧值=" + oldValue + ", 当前=" + ai.get());
    }

    /**
     * 2. 并发计数器 - 多线程同时递增
     */
    private static void concurrentCounter() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        int threadCount = 10;
        int incrementPerThread = 10000;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        long start = System.nanoTime();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for (int j = 0; j < incrementPerThread; j++) {
                    counter.incrementAndGet();
                }
                latch.countDown();
            });
        }

        latch.await();
        long duration = System.nanoTime() - start;
        executor.shutdown();

        System.out.println("  线程数: " + threadCount);
        System.out.println("  每线程递增: " + incrementPerThread);
        System.out.println("  期望结果: " + (threadCount * incrementPerThread));
        System.out.println("  实际结果: " + counter.get());
        System.out.println("  耗时: " + duration / 1_000_000 + " ms");
    }

    /**
     * 3. 模拟抢票系统
     */
    private static void ticketSelling() throws InterruptedException {
        // 100张票，10个窗口同时卖
        AtomicInteger tickets = new AtomicInteger(100);
        int windowCount = 10;
        int ticketsPerWindow = 20;

        ExecutorService executor = Executors.newFixedThreadPool(windowCount);
        CountDownLatch latch = new CountDownLatch(windowCount);

        for (int i = 1; i <= windowCount; i++) {
            final int windowId = i;
            executor.submit(() -> {
                int sold = 0;
                while (sold < ticketsPerWindow) {
                    int ticket = tickets.getAndDecrement();
                    if (ticket > 0) {
                        sold++;
                        if (sold <= 3 || sold > ticketsPerWindow - 3) {
                            System.out.println("  窗口" + windowId + " 售出第" + ticket + "张票");
                        }
                    } else {
                        break;
                    }
                }
                System.out.println("  窗口" + windowId + " 共售出: " + sold + " 张");
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        System.out.println("  最终余票: " + tickets.get());
    }

    /**
     * 4. 任务完成统计
     */
    private static void taskCompletionStats() throws InterruptedException {
        AtomicLong completedTasks = new AtomicLong(0);
        int producerCount = 5;
        int tasksPerProducer = 1000;

        ExecutorService executor = Executors.newFixedThreadPool(producerCount);
        CountDownLatch produceLatch = new CountDownLatch(producerCount);

        // 生产者
        for (int i = 0; i < producerCount; i++) {
            final int producerId = i;
            executor.submit(() -> {
                for (int j = 0; j < tasksPerProducer; j++) {
                    completedTasks.incrementAndGet();
                }
                produceLatch.countDown();
            });
        }

        produceLatch.await();
        System.out.println("  [最终] 共完成任务: " + completedTasks.get());
        executor.shutdown();
    }
}
