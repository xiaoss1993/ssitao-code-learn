package com.ssitao.code.designpattern.threadpool.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Web应用中的线程池示例
 *
 * Web应用场景：
 * 1. 异步任务处理
 * 2. 批量数据处理
 * 3. 定时任务
 * 4. 邮件/短信发送
 * 5. 文件处理
 */
public class WebThreadPoolDemo {

    public static void main(String[] args) {
        System.out.println("=== Web线程池示例 ===\n");

        // 1. 异步任务处理
        System.out.println("1. 异步任务处理");
        asyncTaskDemo();

        // 2. 批量处理
        System.out.println("\n2. 批量数据处理");
        batchProcessDemo();

        // 3. 定时任务
        System.out.println("\n3. 定时任务");
        scheduledTaskDemo();

        // 4. 资源清理
        System.out.println("\n4. 资源清理");
        resourceCleanupDemo();
    }

    /**
     * 异步任务处理示例
     */
    private static void asyncTaskDemo() {
        // 模拟Web请求处理
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 模拟处理用户请求
        for (int i = 1; i <= 5; i++) {
            final int requestId = i;
            executor.submit(() -> {
                System.out.println("处理请求" + requestId + "，线程: " + Thread.currentThread().getName());
                // 模拟耗时操作
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("请求" + requestId + "处理完成");
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
     * 批量数据处理示例
     */
    private static void batchProcessDemo() {
        // 模拟批量导入用户
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // 模拟1000条数据
        int total = 100;
        int batchSize = 10;

        for (int i = 0; i < total; i += batchSize) {
            final int start = i;
            executor.submit(() -> {
                int end = Math.min(start + batchSize, total);
                System.out.println("处理数据 " + start + " - " + end);
                // 模拟批量处理
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定时任务示例
     */
    private static void scheduledTaskDemo() {
        // 使用ScheduledThreadPool执行定时任务
        java.util.concurrent.ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);

        // 延迟1秒后执行
        scheduler.schedule(() -> {
            System.out.println("延迟任务执行");
        }, 1, TimeUnit.SECONDS);

        // 延迟2秒后开始，每3秒执行一次
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("定时任务执行: " + System.currentTimeMillis());
        }, 2, 3, TimeUnit.SECONDS);

        // 关闭
        scheduler.shutdown();
        try {
            scheduler.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 资源清理示例
     */
    private static void resourceCleanupDemo() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 使用完资源后清理
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("JVM关闭，执行资源清理");
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }));

        executor.submit(() -> {
            System.out.println("任务执行中...");
        });

        executor.shutdown();
    }
}

/**
 * Spring中的线程池配置示例
 *
 * 1. ThreadPoolTaskExecutor
 * @Configuration
 * public class ThreadPoolConfig {
 *     @Bean
 *     public TaskExecutor taskExecutor() {
 *         ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
 *         executor.setCorePoolSize(5);
 *         executor.setMaxPoolSize(10);
 *         executor.setQueueCapacity(100);
 *         executor.setThreadNamePrefix("async-");
 *         executor.initialize();
 *         return executor;
 *     }
 * }
 *
 * 2. @Async 异步执行
 * @Service
 * public class AsyncService {
 *     @Async("taskExecutor")
 *     public void asyncMethod() {
 *         // 异步执行
 *     }
 * }
 *
 * 3. CompletableFuture异步返回
 * @Service
 * public class UserService {
 *     public CompletableFuture<User> findUser(String id) {
 *         return CompletableFuture.supplyAsync(() -> {
 *             return userDao.findById(id);
 *         });
 *     }
 * }
 *
 * 4. 线程池选择建议
 * - CPU密集型: 核心数+1
 * - IO密集型: 核心数*2
 * - 混合型: 核心数 * CPU利用率 * (1 + 等待时间/计算时间)
 */