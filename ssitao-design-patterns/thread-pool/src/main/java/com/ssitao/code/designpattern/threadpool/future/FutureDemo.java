package com.ssitao.code.designpattern.threadpool.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

/**
 * Future模式示例
 *
 * Future核心思想：
 * 1. 异步执行任务，不阻塞主线程
 * 2. future.get() 获取结果
 * 3. 支持取消任务
 * 4. 支持超时设置
 */
public class FutureDemo {

    public static void main(String[] args) {
        System.out.println("=== Future模式示例 ===\n");

        try {
            // 1. 基础Future使用
            System.out.println("1. 基础Future");
            basicFutureDemo();

            // 2. 多个任务并行
            System.out.println("\n2. 多个任务并行执行");
            parallelTasksDemo();

            // 3. 任务超时处理
            System.out.println("\n3. 任务超时处理");
            timeoutDemo();

            // 4. 异常处理
            System.out.println("\n4. 任务异常处理");
            exceptionDemo();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 基础Future使用
     */
    private static void basicFutureDemo() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 提交 Callable 任务
        Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("计算任务开始...");
                Thread.sleep(2000);
                int result = 10 + 20;
                System.out.println("计算任务完成，结果: " + result);
                return result;
            }
        });

        // 主线程可以执行其他任务
        System.out.println("主线程执行其他任务...");

        // 获取结果（会阻塞等待）
        Integer result = future.get();
        System.out.println("获取到结果: " + result);

        executor.shutdown();
    }

    /**
     * 多个任务并行执行
     */
    private static void parallelTasksDemo() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 提交多个任务
        Future<String> f1 = executor.submit(() -> {
            Thread.sleep(1000);
            return "任务1结果";
        });

        Future<String> f2 = executor.submit(() -> {
            Thread.sleep(1500);
            return "任务2结果";
        });

        Future<String> f3 = executor.submit(() -> {
            Thread.sleep(500);
            return "任务3结果";
        });

        // 获取所有结果
        System.out.println(f1.get());
        System.out.println(f2.get());
        System.out.println(f3.get());

        executor.shutdown();
    }

    /**
     * 任务超时处理
     */
    private static void timeoutDemo() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<String> future = executor.submit(() -> {
            Thread.sleep(5000);
            return "任务完成";
        });

        try {
            // 设置超时时间
            String result = future.get(2, TimeUnit.SECONDS);
            System.out.println("结果: " + result);
        } catch (Exception e) {
            System.out.println("任务超时或被取消");
            future.cancel(true); // 尝试取消
        }

        executor.shutdown();
    }

    /**
     * 任务异常处理
     */
    private static void exceptionDemo() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<Integer> future = executor.submit(() -> {
            // 模拟抛出异常
            throw new RuntimeException("任务执行出错!");
        });

        try {
            Integer result = future.get();
            System.out.println("结果: " + result);
        } catch (ExecutionException e) {
            System.out.println("捕获到异常: " + e.getCause().getMessage());
        }

        executor.shutdown();
    }
}

/**
 * CompletableFuture 示例（Java8+）
 */
class CompletableFutureDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== CompletableFuture示例 ===\n");

        // 1. 异步执行
        System.out.println("1. 异步执行");
        asyncDemo();

        // 2. 链式调用
        System.out.println("\n2. 链式调用");
        chainDemo();

        // 3. 组合多个Future
        System.out.println("\n3. 组合多个Future");
        combineDemo();
    }

    private static void asyncDemo() throws Exception {
        java.util.concurrent.CompletableFuture<String> future =
            java.util.concurrent.CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "异步任务结果";
            });

        System.out.println("等待结果...");
        String result = future.get();
        System.out.println("结果: " + result);
    }

    private static void chainDemo() throws Exception {
        java.util.concurrent.CompletableFuture<Integer> future =
            java.util.concurrent.CompletableFuture.supplyAsync(() -> 10)
                .thenApply(i -> i * 2)      // 10 -> 20
                .thenApply(i -> i + 5);     // 20 -> 25

        System.out.println("链式计算结果: " + future.get());
    }

    private static void combineDemo() throws Exception {
        java.util.concurrent.CompletableFuture<String> f1 =
            java.util.concurrent.CompletableFuture.supplyAsync(() -> "Hello");
        java.util.concurrent.CompletableFuture<String> f2 =
            java.util.concurrent.CompletableFuture.supplyAsync(() -> "World");

        String result = f1.thenCombine(f2, (s1, s2) -> s1 + " " + s2).get();
        System.out.println("组合结果: " + result);
    }
}
