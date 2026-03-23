package com.ssitao.code.thread.code33.core;

import com.ssitao.code.thread.code33.task.Task;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 代码33：使用 ScheduledThreadPoolExecutor 实现定时任务
 *
 * 【核心概念】
 * ScheduledThreadPoolExecutor 是 ThreadPoolExecutor 的子类
 * 专门用于执行延迟任务或周期性任务
 *
 * 【定时任务方法】
 * 1. schedule(Runnable/Callable, delay, TimeUnit) - 延迟指定时间后执行一次
 * 2. scheduleAtFixedRate() - 固定频率执行（周期）
 * 3. scheduleWithFixedDelay() - 固定延迟执行（上次结束后等待）
 *
 * 【与普通线程池的区别】
 * - 普通线程池：任务立即执行（或等待线程空闲）
 * - 定时线程池：任务可以在指定时间后才开始执行
 *
 * 【应用场景】
 * - 定时数据备份
 * - 延迟缓存过期
 * - 定时发送通知
 * - 周期性的健康检查
 */
public class Main {
    public static void main(String[] args) {
        // ========== 1. 创建定时线程池 ==========
        // newScheduledThreadPool(1) 创建核心线程数为1的定时线程池
        // 定时线程池的核心特点是：可以安排任务在指定时间后执行
        //
        // 【参数说明】
        // - 1 表示核心线程数：线程池保持的最小线程数
        // - 即使空闲也不会回收这些线程
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

        // ========== 2. 提交5个延迟任务 ==========
        // 定时任务的执行顺序由延迟时间决定，而不是提交顺序
        System.out.printf("Main: Starting at: %s\n", new Date());

        // 场景：提交5个任务，每个任务延迟时间不同
        // Task 0: 延迟1秒后执行
        // Task 1: 延迟2秒后执行
        // Task 2: 延迟3秒后执行
        // ...
        for (int i = 0; i < 5; i++) {
            Task task = new Task("Task " + i);
            // schedule(task, 延迟时间, 时间单位)
            // 任务会在指定的延迟时间后执行，只执行一次
            executor.schedule(task, i + 1, TimeUnit.SECONDS);
        }

        // ========== 3. 关闭线程池（不取消已提交的任务） ==========
        // shutdown() 的行为：
        // - 停止接收新任务
        // - 已提交的任务会继续执行完成
        // - 不会等待所有任务完成（异步关闭）
        executor.shutdown();

        // ========== 4. 等待所有任务完成 ==========
        // awaitTermination() 会阻塞直到：
        // 1. 所有任务完成（返回true）
        // 2. 超过指定时间（返回false）
        // 3. 线程被中断（抛出InterruptedException)
        //
        // 【为什么需要这个？】
        // shutdown() 只是发出关闭请求，不保证所有任务立即结束
        // 如果需要确保所有任务完成后再继续（比如退出程序），需要调用awaitTermination()
        try {
            // 设置最大等待时间为1天
            // 如果1天后还有任务没完成，直接返回false
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ========== 5. 输出完成时间 ==========
        System.out.printf("Main: Ends at: %s\n", new Date());
    }
}
