package com.ssitao.code.thread.code28.task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 任务类 - 实现Runnable接口，表示一个可并发执行的任务
 *
 * 本类是线程池要执行的工作单元，每个Task代表一个独立的计算任务
 * 通过实现Runnable接口，任务可以被ThreadPoolExecutor线程池执行
 */
public class Task implements Runnable {

    /**
     * 任务创建时间，记录任务实例化的时间点
     */
    private Date initDate;

    /**
     * 任务名称，用于在日志和输出中区分不同的任务
     */
    private String name;

    /**
     * 构造函数，初始化任务名称和创建时间
     *
     * @param name 任务的名称，用于标识不同的任务实例
     */
    public Task(String name) {
        this.name = name;
        this.initDate = new Date();  // 记录任务创建时刻
    }


    /**
     * 核心执行方法 - 线程池调用此方法来执行任务
     *
     * 该方法定义了任务的具体行为：
     * 1. 打印任务创建信息
     * 2. 打印任务开始执行信息
     * 3. 模拟执行任务（随机睡眠0-10秒）
     * 4. 打印任务完成信息
     */
    @Override
    public void run() {
        // ========== 步骤1: 输出任务创建信息 ==========
        // 显示任务是由哪个线程创建的，以及创建时间
        System.out.printf("%s: Task %s: Created on: %s\n",
                Thread.currentThread().getName(),  // 获取执行当前任务的线程名称
                name,                                // 任务名称
                initDate);                          // 任务创建时间

        // ========== 步骤2: 输出任务开始执行信息 ==========
        // 显示任务正式开始执行的时间和执行线程
        System.out.printf("%s: Task %s: Started on: %s\n",
                Thread.currentThread().getName(),
                name,
                new Date());  // 当前时间（任务开始时间）

        // ========== 步骤3: 模拟任务执行 ==========
        // 使用随机数生成0-10秒的睡眠时间，模拟不同耗时的任务
        try {
            // 生成0.0到10.0之间的随机数，转换为秒
            Long duration = (long) (Math.random() * 10);
            System.out.printf("%s: Task %s: Doing a task during %d seconds\n",
                    Thread.currentThread().getName(),
                    name,
                    duration);

            // 使用TimeUnit.SECONDS.sleep()使当前线程睡眠指定秒数
            // 这是模拟任务执行的核心耗时操作
            // 注意：sleep期间线程处于BLOCKED状态，不占用CPU
            TimeUnit.SECONDS.sleep(duration);

        } catch (InterruptedException e) {
            // 当线程在sleep期间被中断时，会抛出此异常
            // 正常情况下不会发生，这里简单打印堆栈信息
            e.printStackTrace();
        }

        // ========== 步骤4: 输出任务完成信息 ==========
        // 显示任务执行完成的时间和完成任务的线程
        System.out.printf("%s: Task %s: Finished on: %s\n",
                Thread.currentThread().getName(),
                name,
                new Date());  // 当前时间（任务完成时间）
    }
}
