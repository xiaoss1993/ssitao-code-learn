package com.ssitao.code.thread.code38.task;

import java.util.concurrent.TimeUnit;

/**
 * 任务类，执行一个随机时间的任务
 *
 * 实现了 Runnable 接口，代表一个可由线程执行的任务。
 * 每个任务执行时会随机睡眠 0-10 秒，模拟实际的耗时操作。
 *
 * @see Runnable
 */
public class Task implements Runnable {
    /** 任务名称，用于标识和输出 */
    private String name;

    /**
     * 构造函数
     * @param name 任务的名称
     */
    public Task(String name) {
        this.name = name;
    }

    /**
     * 任务执行的主体逻辑
     *
     * 当线程池分配一个线程来执行此任务时，会调用此方法。
     * 实现要点：
     * 1. 输出任务开始信息
     * 2. 随机生成 0-10 秒的睡眠时间
     * 3. 模拟耗时操作（睡眠）
     * 4. 输出任务结束信息
     */
    @Override
    public void run() {
        // 任务开始执行
        System.out.printf("Task %s: Starting\n", name);
        try {
            // 生成 0-10 秒的随机睡眠时间
            Long duration = (long) (Math.random() * 10);
            System.out.printf("Task %s: ReportGenerator: Generating a report during %d seconds\n", name, duration);
            // 使用 TimeUnit.SECONDS.sleep() 更直观地表示睡眠秒数
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            // 任务被中断时，打印异常信息
            // 注意：在生产环境中，这里应该更优雅地处理中断
            e.printStackTrace();
        }
        // 任务执行完成
        System.out.printf("Task %s: Ending\n", name);
    }

    /**
     * 返回任务的字符串表示
     * 当任务被拒绝时，RejectedTaskController 会调用此方法获取任务信息
     * @return 任务名称
     */
    @Override
    public String toString() {
        return name;
    }
}
