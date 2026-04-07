package com.ssitao.code.effectivejava.ch09.item80;

import java.util.concurrent.*;

/**
 * 条目80：优先使用并发工具而非wait和notify
 *
 * java.util.concurrent中的高级并发工具：
 * 1. ExecutorService - 线程池管理
 * 2. ConcurrentHashMap - 高效并发Map
 * 3. BlockingQueue - 阻塞队列
 * 4. CountDownLatch - 倒计时门栓
 * 5. Semaphore - 信号量
 * 6. CyclicBarrier - 循环屏障
 *
 * 原则：
 * - 优先使用高层工具
 * - wait和notify容易出错，难以调试
 * - 现代JVM对同步块优化很好，wait/notify不更快
 */
public class ConcurrentUtilities {

    // ==================== CountDownLatch示例 ====================
    /**
     * 并行启动多个线程，等待所有线程完成后汇总结果
     */
    static class ParallelProcessor {
        public static long timeParallel(int nThreads, Runnable... tasks)
                throws InterruptedException {
            CountDownLatch startSignal = new CountDownLatch(1);  // 控制器开始
            CountDownLatch doneSignal = new CountDownLatch(nTasks(tasks));  // 计数器

            for (Runnable task : tasks) {
                new Thread(() -> {
                    try {
                        startSignal.await();  // 等待开始信号
                        task.run();
                        doneSignal.countDown();  // 完成
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }

            long start = System.nanoTime();
            startSignal.countDown();  // 发出开始信号
            doneSignal.await();  // 等待所有完成
            return System.nanoTime() - start;
        }

        private static int nTasks(Runnable... tasks) {
            return tasks.length;
        }
    }

    // ==================== Semaphore示例 ====================
    /**
     * Semaphore实现限流器
     */
    static class RateLimiter {
        private final Semaphore permits;

        public RateLimiter(int permits) {
            this.permits = new Semaphore(permits);
        }

        public void acquire() throws InterruptedException {
            permits.acquire();  // 获取许可，没有则阻塞
        }

        public void release() {
            permits.release();  // 释放许可
        }

        public void doWork(String task) throws InterruptedException {
            acquire();
            try {
                System.out.println("执行任务: " + task);
                Thread.sleep(100);
            } finally {
                release();
            }
        }
    }

    // ==================== CyclicBarrier示例 ====================
    /**
     * CyclicBarrier用于多线程协调
     */
    static class PhaseTask {
        public static void runPhases(int nWorkers) throws InterruptedException {
            CyclicBarrier barrier = new CyclicBarrier(nWorkers, () ->
                System.out.println("所有工作线程到达屏障点，进入下一阶段"));

            ExecutorService executor = Executors.newFixedThreadPool(nWorkers);

            for (int i = 0; i < nWorkers; i++) {
                final int phase = i;
                executor.submit(() -> {
                    try {
                        for (int p = 0; p < 3; p++) {
                            System.out.println("线程" + phase + " 执行阶段" + p);
                            barrier.await();  // 等待其他线程
                        }
                    } catch (BrokenBarrierException | InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        }
    }

    // ==================== ScheduledFuture示例 ====================
    /**
     * 延迟执行和周期性执行
     */
    static class ScheduledTask {
        public static void scheduleTask() throws InterruptedException {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            Runnable task = () -> System.out.println("定时任务执行: " + System.currentTimeMillis());

            // 延迟1秒后执行
            ScheduledFuture<?> scheduledFuture = scheduler.schedule(task, 1, TimeUnit.SECONDS);

            // 等待任务完成
            try {
                scheduledFuture.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            scheduler.shutdown();
        }

        public static void periodicTask() throws InterruptedException {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            Runnable task = () -> System.out.println("周期性任务: " + System.currentTimeMillis());

            // 延迟1秒后开始，每2秒执行一次
            ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(
                task, 1, 2, TimeUnit.SECONDS);

            Thread.sleep(10000);  // 执行5次
            scheduledFuture.cancel(true);
            scheduler.shutdown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== CountDownLatch示例 ===\n");

        // 模拟并行计算
        long time = ParallelProcessor.timeParallel(4,
            () -> { sleep(100); },
            () -> { sleep(200); },
            () -> { sleep(150); },
            () -> { sleep(80); }
        );
        System.out.println("并行执行时间: " + time / 1_000_000 + "ms");

        System.out.println("\n=== Semaphore限流示例 ===\n");

        RateLimiter limiter = new RateLimiter(3);  // 最多3个并发

        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            new Thread(() -> {
                try {
                    limiter.doWork("任务" + taskId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        Thread.sleep(2000);  // 等待完成

        System.out.println("\n=== CyclicBarrier示例 ===\n");

        PhaseTask.runPhases(3);

        System.out.println("\n=== 最佳实践 ===\n");
        System.out.println("1. CountDownLatch: 一次性事件");
        System.out.println("2. CyclicBarrier: 可重用的线程协调");
        System.out.println("3. Semaphore: 限流");
        System.out.println("4. ScheduledExecutorService: 定时任务");
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
