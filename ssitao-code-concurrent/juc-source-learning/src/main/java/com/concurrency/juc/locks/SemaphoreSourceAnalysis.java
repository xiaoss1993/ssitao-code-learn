package com.concurrency.juc.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore 源码分析 + 实际使用示例
 */
public class SemaphoreSourceAnalysis {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Semaphore 源码分析 + 使用示例 ===\n");

        // 1. 连接池限流
        System.out.println("【1. 数据库连接池限流】");
        connectionPoolDemo();

        // 2. 限流器
        System.out.println("\n【2. API接口限流】");
        rateLimiterDemo();

        // 3. 资源池管理
        System.out.println("\n【3. 线程池资源管理】");
        resourcePoolDemo();

        // 4. 门控许可
        System.out.println("\n【4. 批量任务控制】");
        batchTaskControl();
    }

    /**
     * 1. 数据库连接池限流
     */
    private static void connectionPoolDemo() throws InterruptedException {
        // 模拟连接池，只有3个连接
        Semaphore connections = new Semaphore(3);
        ExecutorService executor = Executors.newFixedThreadPool(6);

        System.out.println("  数据库连接池: 最大连接数=3");

        for (int i = 1; i <= 6; i++) {
            final int requestId = i;
            executor.submit(() -> {
                try {
                    System.out.println("  请求" + requestId + " 等待获取连接...");
                    connections.acquire();
                    System.out.println("  请求" + requestId + " 获取到连接，开始查询...");

                    Thread.sleep((long) (Math.random() * 1000 + 500));

                    System.out.println("  请求" + requestId + " 查询完成，释放连接");
                    connections.release();
                } catch (InterruptedException e) {
                }
            });
        }

        Thread.sleep(3000);
        executor.shutdown();
    }

    /**
     * 2. API接口限流
     */
    private static void rateLimiterDemo() throws InterruptedException {
        // 每秒最多5个请求
        Semaphore rateLimiter = new Semaphore(5);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        System.out.println("  API限流: 每秒最多5个请求");

        for (int i = 1; i <= 10; i++) {
            final int requestId = i;
            executor.submit(() -> {
                try {
                    rateLimiter.acquire();
                    System.out.println("  请求" + requestId + " 通过限流，执行中...");
                    Thread.sleep(100);
                    rateLimiter.release();
                } catch (InterruptedException e) {
                }
            });
        }

        Thread.sleep(500);
        executor.shutdown();
    }

    /**
     * 3. 资源池管理 - 模拟线程池+任务队列
     */
    private static void resourcePoolDemo() throws InterruptedException {
        int poolSize = 2;
        Semaphore pool = new Semaphore(poolSize);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        System.out.println("  工作池: 核心线程数=2, 最大=4");

        for (int i = 1; i <= 4; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    System.out.println("  任务" + taskId + " 等待获取工作线程...");
                    pool.acquire();
                    System.out.println("  任务" + taskId + " 开始执行");

                    Thread.sleep(500);

                    System.out.println("  任务" + taskId + " 执行完成，释放线程");
                    pool.release();
                } catch (InterruptedException e) {
                }
            });
        }

        Thread.sleep(2000);
        executor.shutdown();
    }

    /**
     * 4. 批量任务控制 - 同时最多执行N个任务
     */
    private static void batchTaskControl() throws InterruptedException {
        // 最多同时执行2个任务
        Semaphore semaphore = new Semaphore(2);
        ExecutorService executor = Executors.newFixedThreadPool(5);

        System.out.println("  批量任务控制: 同时最多执行2个");

        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("  任务" + taskId + " [开始] 执行中...");
                    Thread.sleep(800);
                    System.out.println("  任务" + taskId + " [完成]");
                    semaphore.release();
                } catch (InterruptedException e) {
                }
            });
        }

        Thread.sleep(3000);
        executor.shutdown();
    }

    /**
     * 5. tryAcquire 非阻塞获取
     */
    private static void tryAcquireDemo() {
        Semaphore semaphore = new Semaphore(1);

        try {
            semaphore.acquire();
            System.out.println("  获取第一个许可");

            // 非阻塞获取
            if (semaphore.tryAcquire()) {
                System.out.println("  tryAcquire() 成功");
            } else {
                System.out.println("  tryAcquire() 失败");
            }

            // 带超时的获取
            if (semaphore.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                System.out.println("  tryAcquire(timeout) 成功");
            } else {
                System.out.println("  tryAcquire(timeout) 超时");
            }

            semaphore.release();
        } catch (InterruptedException e) {
        }
    }
}
