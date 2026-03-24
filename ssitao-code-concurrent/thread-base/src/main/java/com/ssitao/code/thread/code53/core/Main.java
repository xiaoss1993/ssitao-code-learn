package com.ssitao.code.thread.code53.core;

import com.ssitao.code.thread.code53.task.MyTask;
import com.ssitao.code.thread.code53.task.MyThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 主程序 - ThreadFactory 与 ExecutorService 结合使用示例
 *
 * 演示在线程池中使用自定义 ThreadFactory
 *
 * 核心概念：
 * 1. Executors.newCachedThreadPool(threadFactory)
 *    - 创建一个缓存线程池
 *    - 根据需要创建新线程，空闲线程会被复用
 *    - 使用自定义 ThreadFactory 创建线程
 *
 * 2. ThreadFactory 在线程池中的作用：
 *    - 每当线程池需要创建新线程时，调用工厂的 newThread()
 *    - 保证线程池中所有线程都有统一的属性（名称前缀、优先级等）
 *    - 便于调试和日志追踪
 *
 * 3. CachedThreadPool 特性：
 *    - 核心线程数为0
 *    - 最大线程数为 Integer.MAX_VALUE（几乎无限制）
 *    - 空闲线程存活时间：60秒
 *    - 适合执行大量短期任务
 *
 * 与 code52 的区别：
 * - code52: 直接用 ThreadFactory 创建 Thread 对象
 * - code53: 通过 ThreadFactory 创建线程池，由线程池管理线程
 *
 * 执行流程：
 * 1. 创建自定义线程工厂 MyThreadFactory
 * 2. 使用工厂创建 CachedThreadPool
 * 3. 提交任务到线程池
 * 4. 线程池自动分配线程执行任务
 * 5. 关闭线程池并等待任务完成
 */
public class Main {

    public static void main(String[] args) throws Exception {

        // 创建自定义线程工厂
        // 所有由这个工厂创建的线程都会有统一的前缀名称
        MyThreadFactory threadFactory = new MyThreadFactory("MyThreadFactory");

        // 使用自定义线程工厂创建缓存线程池
        // 线程池会根据需要自动创建新线程
        // 所有线程都由 MyThreadFactory 创建，具有监控功能
        ExecutorService executor = Executors.newCachedThreadPool(threadFactory);

        // 创建任务
        MyTask task = new MyTask();

        // 提交任务到线程池
        // 线程池会自动分配一个线程来执行这个任务
        executor.submit(task);

        // 平缓关闭线程池
        // 不再接受新任务，但会完成已提交的任务
        executor.shutdown();

        // 等待所有任务完成
        // 参数：最大等待时间 1 天
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main: End of the program.\n");
    }
}
