package com.ssitao.code.thread.code51.core;

import com.ssitao.code.thread.code51.task.MyPriorityTask;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 主程序 - PriorityBlockingQueue 优先级阻塞队列示例
 *
 * 演示使用 PriorityBlockingQueue 实现基于任务优先级的线程池调度
 *
 * 核心概念：
 * 1. PriorityBlockingQueue - 基于优先级的无界阻塞队列
 *    - 元素必须实现 Comparable 接口
 *    - 优先级高的元素先出队列
 *    - 使用堆结构实现，插入和删除时间复杂度 O(log n)
 *    - 无界队列，不会阻塞生产者
 *
 * 2. 优先级规则（在本例中）：
 *    - 数字越小，优先级越高
 *    - Task 0 优先级最高，Task 7 优先级最低
 *    - compareTo 返回 -1 表示当前对象优先级更高
 *
 * 3. ThreadPoolExecutor 配置：
 *    - 核心线程数: 2
 *    - 最大线程数: 2
 *    - 存活时间: 1秒
 *    - 任务队列: PriorityBlockingQueue（无界）
 *
 * 执行流程：
 * 1. 提交 Task 0-3（优先级 0-3，越小越高）
 * 2. 等待1秒后，提交 Task 4-7（优先级 4-7）
 * 3. 由于只有2个线程，任务会按优先级顺序执行
 *
 * 预期执行顺序：
 * - Task 0 优先级最高，最先执行
 * - Task 1、2、3 按顺序执行
 * - Task 4、5、6、7 等前面的任务完成后，按优先级执行
 *
 * 注意：
 * - PriorityBlockingQueue 是无界的，execute() 不会阻塞
 * - 如果线程池核心线程都在忙，新任务会等待
 * - 任务执行顺序由优先级决定，而非提交顺序
 */
public class Main {

    public static void main(String[] args) {

        // 创建优先级线程池执行器
        // 参数说明：
        // - 核心线程数: 2（池中始终保持的最小线程数）
        // - 最大线程数: 2（池中允许的最大线程数）
        // - 存活时间: 1秒（空闲线程等待新任务的超时时间）
        // - 时间单位: 秒
        // - 任务队列: PriorityBlockingQueue（无界优先级队列）
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,                          // corePoolSize: 2
            2,                          // maximumPoolSize: 2
            1,                          // keepAliveTime: 1s
            TimeUnit.SECONDS,            // unit: 秒
            new PriorityBlockingQueue<>() // 无界优先级队列
        );

        // 提交第一批任务（4个任务，优先级 0-3）
        // 优先级 0 最高，优先级 3 最低
        // 这些任务会立即进入队列，并按优先级排序
        for (int i = 0; i < 4; i++) {
            MyPriorityTask task = new MyPriorityTask("Task " + i, i);
            executor.execute(task);
        }

        // 主线程睡眠1秒
        // 此时线程池正在执行 Task 0 和 Task 1
        // Task 2 和 Task 3 在队列中等待
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 提交第二批任务（4个任务，优先级 4-7）
        // 这些任务优先级低于第一批，会排在后面
        for (int i = 4; i < 8; i++) {
            MyPriorityTask task = new MyPriorityTask("Task " + i, i);
            executor.execute(task);
        }

        // 平缓关闭执行器
        // 不再接受新任务，但会完成已提交的任务
        executor.shutdown();

        // 等待所有任务执行完成
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 程序结束
        System.out.printf("Main: End of the program.\n");
    }
}