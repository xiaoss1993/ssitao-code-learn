package com.concurrency.juc.executors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolExecutor 源码分析 (JDK8) + 实际使用示例
 */
public class ThreadPoolExecutorSourceAnalysis {

    public static void main(String[] args) throws Exception {
        System.out.println("=== ThreadPoolExecutor 源码分析 + 使用示例 ===\n");

        // 1. 基本线程池使用
        System.out.println("【1. 基本线程池使用】");
        basicThreadPool();

        // 2. 带返回值的任务
        System.out.println("\n【2. 带返回值的任务】");
        callableAndFuture();

        // 3. 批量提交任务
        System.out.println("\n【3. 批量提交任务】");
        batchSubmit();

        // 4. 定时任务
        System.out.println("\n【4. 定时任务】");
        scheduledTask();

        // 5. 拒绝策略
        System.out.println("\n【5. 拒绝策略】");
        rejectionPolicy();

        // 6. 线程池监控
        System.out.println("\n【6. 线程池监控】");
        monitorThreadPool();

        // 7. 正确关闭线程池
        System.out.println("\n【7. 正确关闭线程池】");
        shutdownThreadPool();
    }

    /**
     * 1. 基本线程池使用
     */
    private static void basicThreadPool() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,                      // corePoolSize
                4,                      // maximumPoolSize
                60L,                    // keepAliveTime
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10)
        );

        System.out.println("  线程池: core=2, max=4, queue=10");

        for (int i = 1; i <= 6; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("  任务" + taskId + " 由 " + Thread.currentThread().getName() + " 执行");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
            });
        }

        Thread.sleep(1000);
        System.out.println("  活动线程数: " + executor.getActiveCount());
        System.out.println("  队列大小: " + executor.getQueue().size());

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }

    /**
     * 2. Callable 和 Future
     */
    private static void callableAndFuture() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 提交Callable任务
        Future<Integer> future = executor.submit(() -> {
            System.out.println("  [计算任务] 开始计算...");
            Thread.sleep(1000);
            int result = 1 + 2 + 3 + 4 + 5;
            System.out.println("  [计算任务] 计算完成，结果=" + result);
            return result;
        });

        System.out.println("  主线程做其他事情...");

        // 阻塞获取结果
        Integer result = future.get();
        System.out.println("  主线程获取到结果: " + result);

        executor.shutdown();
    }

    /**
     * 3. 批量提交任务
     */
    private static void batchSubmit() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        java.util.List<Callable<String>> tasks = java.util.Arrays.asList(
                () -> { Thread.sleep(100); return "任务A"; },
                () -> { Thread.sleep(200); return "任务B"; },
                () -> { Thread.sleep(50); return "任务C"; }
        );

        System.out.println("  提交3个任务，使用invokeAll...");

        long start = System.nanoTime();
        java.util.List<Future<String>> futures = executor.invokeAll(tasks);
        long duration = System.nanoTime() - start;

        for (int i = 0; i < futures.size(); i++) {
            System.out.println("  结果" + (char)('A'+i) + ": " + futures.get(i).get());
        }
        System.out.println("  总耗时: " + duration / 1_000_000 + "ms");

        executor.shutdown();
    }

    /**
     * 4. 定时任务
     */
    private static void scheduledTask() throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        System.out.println("  提交定时任务...");

        // 延迟执行
        ScheduledFuture<?> delayedTask = scheduler.schedule(() -> {
            System.out.println("  [延迟任务] 2秒后执行");
        }, 2, TimeUnit.SECONDS);

        // 固定频率执行
        ScheduledFuture<?> fixedTask = scheduler.scheduleAtFixedRate(() -> {
            System.out.println("  [周期任务] 固定频率执行");
        }, 0, 500, TimeUnit.MILLISECONDS);

        Thread.sleep(2000);

        delayedTask.cancel(false);
        fixedTask.cancel(false);

        scheduler.shutdown();
        scheduler.awaitTermination(1, TimeUnit.SECONDS);
    }

    /**
     * 5. 拒绝策略
     */
    private static void rejectionPolicy() throws InterruptedException {
        System.out.println("  创建线程池: core=1, max=1, queue=1");
        System.out.println("  理论最大任务数: 1+1=2");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 1, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println("  [拒绝策略] 任务被拒绝: " + r);
                    }
                }
        );

        for (int i = 1; i <= 3; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("  任务" + taskId + " 开始执行");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                System.out.println("  任务" + taskId + " 执行完成");
            });
        }

        Thread.sleep(2000);
        executor.shutdown();
    }

    /**
     * 6. 线程池监控
     */
    private static void monitorThreadPool() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2, 4, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100)
        );

        System.out.println("  监控线程池状态:");
        System.out.println("  初始状态:");
        printStatus(executor);

        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            });
        }

        Thread.sleep(500);
        System.out.println("\n  提交5个任务后:");
        printStatus(executor);

        Thread.sleep(3000);
        System.out.println("\n  任务执行完成后:");
        printStatus(executor);

        executor.shutdown();
    }

    private static void printStatus(ThreadPoolExecutor executor) {
        System.out.println("    核心线程数: " + executor.getCorePoolSize());
        System.out.println("    最大线程数: " + executor.getMaximumPoolSize());
        System.out.println("    活动线程数: " + executor.getActiveCount());
        System.out.println("    完成任务数: " + executor.getCompletedTaskCount());
        System.out.println("    队列任务数: " + executor.getQueue().size());
    }

    /**
     * 7. 正确关闭线程池
     */
    private static void shutdownThreadPool() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int i = 1; i <= 3; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("  任务" + taskId + " 执行中...");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("  任务" + taskId + " 被中断");
                }
            });
        }

        System.out.println("  调用shutdown()...");
        executor.shutdown();

        if (executor.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("  线程池已关闭");
        } else {
            System.out.println("  等待超时，调用shutdownNow()...");
            executor.shutdownNow();
        }
    }
}
