package com.concurrency.juc.locks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch 源码分析 + 实际使用示例
 */
public class CountDownLatchSourceAnalysis {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== CountDownLatch 源码分析 + 使用示例 ===\n");

        // 1. 等待多线程完成
        System.out.println("【1. 等待多线程初始化完成】");
        waitForInitialization();

        // 2. 模拟百米赛跑
        System.out.println("\n【2. 百米赛跑起跑】");
        raceStart();

        // 3. 并行计算后汇总结果
        System.out.println("\n【3. 并行计算汇总】");
        parallelComputeAndMerge();

        // 4. 超时等待
        System.out.println("\n【4. 超时等待】");
        timeoutWait();
    }

    /**
     * 1. 模拟服务启动，等待所有组件初始化
     */
    private static void waitForInitialization() throws InterruptedException {
        System.out.println("  应用启动中...");

        CountDownLatch latch = new CountDownLatch(3);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 数据库连接
        executor.submit(() -> {
            sleep(200);
            System.out.println("  [组件1] 数据库连接池初始化完成");
            latch.countDown();
        });

        // 缓存服务
        executor.submit(() -> {
            sleep(300);
            System.out.println("  [组件2] 缓存服务初始化完成");
            latch.countDown();
        });

        // 消息队列
        executor.submit(() -> {
            sleep(150);
            System.out.println("  [组件3] 消息队列初始化完成");
            latch.countDown();
        });

        latch.await(); // 等待所有组件就绪
        System.out.println("  所有组件就绪，应用启动完成!");
        executor.shutdown();
    }

    /**
     * 2. 百米赛跑 - 所有选手准备好后同时起跑
     */
    private static void raceStart() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1); // 发令枪
        int runnerCount = 5;

        ExecutorService executor = Executors.newFixedThreadPool(runnerCount);

        System.out.println("  选手准备就绪，等待发令枪...");

        for (int i = 1; i <= runnerCount; i++) {
            final int runnerId = i;
            executor.submit(() -> {
                try {
                    startLatch.await(); // 等待起跑信号
                    System.out.println("  选手" + runnerId + " 开始跑步!");
                    Thread.sleep((long) (Math.random() * 500 + 200));
                    System.out.println("  选手" + runnerId + " 冲过终点!");
                } catch (InterruptedException e) {
                }
            });
        }

        Thread.sleep(500);
        System.out.println("  [裁判] 预备...");
        Thread.sleep(500);
        System.out.println("  [裁判] 发令枪响!");
        startLatch.countDown(); // 所有选手同时开始

        Thread.sleep(1000);
        executor.shutdown();
    }

    /**
     * 3. 并行计算后汇总结果
     */
    private static void parallelComputeAndMerge() throws InterruptedException {
        int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int chunkSize = 2;
        int chunkCount = (data.length + chunkSize - 1) / chunkSize;

        CountDownLatch latch = new CountDownLatch(chunkCount);
        int[] partialResults = new int[chunkCount];

        ExecutorService executor = Executors.newFixedThreadPool(chunkCount);

        System.out.println("  数据: " + java.util.Arrays.toString(data));
        System.out.println("  分成 " + chunkCount + " 块并行计算\n");

        // 并行计算每块的累加和
        for (int i = 0; i < chunkCount; i++) {
            final int chunkId = i;
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, data.length);

            executor.submit(() -> {
                int sum = 0;
                for (int j = start; j < end; j++) {
                    sum += data[j];
                }
                partialResults[chunkId] = sum;
                System.out.println("  块" + chunkId + " [" + start + ":" + end + ") 累加和 = " + sum);
                latch.countDown();
            });
        }

        latch.await(); // 等待所有块计算完成

        // 汇总结果
        int totalSum = 0;
        for (int sum : partialResults) {
            totalSum += sum;
        }

        System.out.println("\n  汇总结果: " + totalSum);
        System.out.println("  验证: " + java.util.stream.IntStream.of(data).sum());
        executor.shutdown();
    }

    /**
     * 4. 超时等待 - 等待任务完成，超过时间则放弃
     */
    private static void timeoutWait() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        // 模拟任务
        new Thread(() -> {
            try {
                Thread.sleep(3000); // 任务需要3秒
                latch.countDown();
            } catch (InterruptedException e) {
            }
        }).start();

        System.out.println("  等待任务完成，最长2秒...");

        boolean completed = latch.await(2, TimeUnit.SECONDS);

        if (completed) {
            System.out.println("  任务完成!");
        } else {
            System.out.println("  等待超时，任务未完成");
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
