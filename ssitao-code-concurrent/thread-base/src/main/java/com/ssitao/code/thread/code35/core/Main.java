package com.ssitao.code.thread.code35.core;


import com.ssitao.code.thread.code35.task.Task;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 代码35：使用 Future.cancel() 取消正在执行的任务
 *
 * 【核心概念】
 * Future.cancel(boolean) 用于尝试取消正在执行的任务
 * 这是中断机制在线程池中的具体应用
 *
 * 【方法签名】
 * cancel(boolean mayInterruptIfRunning)
 * - mayInterruptIfRunning = true: 如果任务正在执行，会中断执行线程
 * - mayInterruptIfRunning = false: 如果任务正在执行，不会中断，只是不再等待结果
 *
 * 【取消行为说明】
 * 1. 如果任务尚未开始：直接取消，不会执行
 * 2. 如果任务正在执行：
 *    - mayInterruptIfRunning=true → 调用 Thread.interrupt() 中断线程
 *    - mayInterruptIfRunning=false → 任务继续执行，但结果会被丢弃
 * 3. 如果任务已完成：取消操作无效（返回false）
 *
 * 【Future 状态图】
 *         ┌─────────┐
 *         │  NEW    │ ← 任务刚创建
 *         └────┬────┘
 *              │
 *         ┌────▼────┐
 *         │ RUNNING │ ← 正在执行
 *         └────┬────┘
 *              │
 *     ┌────────┴────────┐
 *     │                 │
 * ┌───▼───┐        ┌───▼───┐
 * │DONE   │        │CANCELLED│
 * │(正常) │        │        │
 * └───────┘        └────────┘
 *
 * 【应用场景】
 * - 用户取消操作（如取消下载、取消搜索）
 * - 超时自动取消
 * - 资源清理时取消长时任务
 */
public class Main {
    public static void main(String[] args) {
        // ========== 1. 创建缓存线程池 ==========
        // newCachedThreadPool() 特点：
        // - 会重用空闲线程
        // - 可以按需创建新线程
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        // ========== 2. 创建并提交任务 ==========
        // 场景说明：提交一个无限循环的任务（每100ms打印一次）
        // 这个任务会一直运行，直到被取消
        Task task = new Task();

        System.out.printf("Main: Executing the Task\n");

        // submit() 是异步提交，立即返回 Future
        // 任务会在后台线程中执行，不阻塞主线程
        Future<String> result = executor.submit(task);

        // ========== 3. 主线程休眠2秒 ==========
        // 在这段时间内，Task 会在另一个线程中不断打印消息
        // 约打印 20 次（2秒 / 100毫秒）
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ========== 4. 取消任务 ==========
        // 场景：用户取消操作或超时自动取消
        // cancel(true) 的行为：
        // 1. 尝试中断正在执行任务的线程（调用 Thread.interrupt()）
        // 2. Task 中的 Thread.sleep(100) 会抛出 InterruptedException
        // 3. 任务结束，Future 状态变为 CANCELLED
        System.out.printf("Main: Cancelling the Task\n");
        result.cancel(true);

        // ========== 5. 验证任务状态 ==========
        // isCancelled()：是否被取消（包含 CANCELLED 状态）
        // isDone()：是否完成（包含 NORMAL、CANCELLED、EXCEPTION 任何一种）
        System.out.printf("Main: Cancelled: %s\n", result.isCancelled());
        System.out.printf("Main: Done: %s\n", result.isDone());

        // ========== 6. 关闭线程池 ==========
        executor.shutdown();
        System.out.printf("Main: The executor has finished\n");
    }
}
