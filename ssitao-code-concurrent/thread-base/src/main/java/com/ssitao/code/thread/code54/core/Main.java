package com.ssitao.code.thread.code54.core;


import com.ssitao.code.thread.code54.task.MyScheduledThreadPoolExecutor;
import com.ssitao.code.thread.code54.task.Task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 主程序 - ScheduledThreadPoolExecutor 定时线程池示例
 *
 * 演示使用 ScheduledThreadPoolExecutor 执行定时任务
 *
 * 核心概念：
 * 1. ScheduledThreadPoolExecutor - 定时线程池
 *    - 支持延迟执行任务
 *    - 支持周期性执行任务
 *    - 继承自 ThreadPoolExecutor
 *
 * 2. 定时任务方法：
 *    - schedule(Runnable, delay, unit): 延迟一次执行
 *    - scheduleAtFixedRate(Runnable, initialDelay, period, unit): 固定周期执行
 *    - scheduleWithFixedDelay(Runnable, initialDelay, delay, unit): 固定延迟执行
 *
 * 3. scheduleAtFixedRate vs scheduleWithFixedDelay:
 *    - scheduleAtFixedRate: 固定周期（任务开始时间间隔固定）
 *    - scheduleWithFixedDelay: 固定延迟（任务结束到开始间隔固定）
 *
 * 执行流程：
 * 1. 创建 ScheduledThreadPoolExecutor（核心线程数2）
 * 2. 使用 schedule() 延迟1秒执行一次性任务
 * 3. 睡眠3秒后
 * 4. 使用 scheduleAtFixedRate() 延迟1秒后开始，每3秒执行一次
 * 5. 睡眠10秒后关闭线程池
 *
 * 预期输出：
 * - T+1s: Task: Begin./End. (一次性任务)
 * - T+4s: 开始周期任务
 * - T+7s, T+10s, T+13s: 周期任务执行
 */
public class Main {

    public static void main(String[] args) throws Exception {

        // 创建定时线程池，核心线程数为2
        MyScheduledThreadPoolExecutor executor = new MyScheduledThreadPoolExecutor(2);

        // 创建一次性任务
        Task task = new Task();

        // 打印当前时间
        System.out.printf("Main: %s\n", new Date());

        // 延迟1秒后执行一次性任务
        // 参数：任务、延迟时间、时间单位
        executor.schedule(task, 1, TimeUnit.SECONDS);

        // 主线程睡眠3秒
        // 等待一次性任务执行完成
        TimeUnit.SECONDS.sleep(3);

        // 创建新的周期任务
        task = new Task();

        // 打印当前时间
        System.out.printf("Main: %s\n", new Date());

        // 延迟1秒后开始周期执行，每3秒执行一次
        // 参数：任务、初始延迟、周期、时间单位
        // 注意：如果任务执行时间超过周期，scheduleAtFixedRate会等待任务完成
        executor.scheduleAtFixedRate(task, 1, 3, TimeUnit.SECONDS);

        // 主线程睡眠10秒
        // 让周期任务执行几次
        TimeUnit.SECONDS.sleep(10);

        // 平缓关闭线程池
        executor.shutdown();

        // 等待所有任务完成
        executor.awaitTermination(1, TimeUnit.DAYS);

        System.out.printf("Main: End of the program.\n");
    }

}
