package com.ssitao.code.thread.code34.task;

import java.util.Date;

/**
 * 任务类，实现Runnable接口
 *
 * 【为什么使用Runnable而不是Callable？】
 * 在 ScheduledExecutorService 中：
 * - scheduleAtFixedRate() 和 scheduleWithFixedDelay() 返回 ScheduledFuture<?>
 * - Runnable 的 run() 方法没有返回值（返回void）
 * - 不需要获取任务执行结果时，用 Runnable 更简洁
 *
 * 【Runnable vs Callable 对比】
 * - Runnable: run() 无返回值，不能抛检查异常
 * - Callable<V>: call() 有返回值<V>，可以抛检查异常
 * - ScheduledExecutorService 的 scheduleXXX() 方法接受两者
 *
 * 【任务说明】
 * 这是一个简单的周期性任务：
 * - 输出任务名称和执行时间
 * - 真实场景中可能是：发送心跳、刷新缓存、备份数据等
 */
public class Task implements Runnable {
    /**
     * 任务名称，用于标识是哪个任务在执行
     */
    private String name;

    /**
     * 构造函数，初始化任务名称
     *
     * @param name 任务名称
     */
    public Task(String name) {
        this.name = name;
    }

    /**
     * 核心方法：执行周期性任务
     *
     * 【执行时机】
     * 此方法由 ScheduledExecutorService 定期调用
     * 调用间隔由 scheduleAtFixedRate() 或 scheduleWithFixedDelay() 控制
     *
     * 【注意事项】
     * - 不要在此方法中执行耗时过长的操作
     * - 如果 run() 执行时间超过周期，下一次会立即执行（scheduleAtFixedRate）
     * - 如果需要取消任务，可将返回的 ScheduledFuture 传入 cancel()
     */
    @Override
    public void run() {
        // 输出任务名称和当前执行时间
        System.out.printf("%s: Executed at: %s\n", name, new Date());
    }
}
