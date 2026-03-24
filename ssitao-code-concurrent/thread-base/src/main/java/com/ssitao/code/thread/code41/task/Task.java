package com.ssitao.code.thread.code41.task;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 任务类：演示 Fork/Join 框架的任务拆分与异常处理
 *
 * 核心逻辑：
 * 1. 如果处理范围 >= 10 个元素 -> 拆分成两个子任务并行执行
 * 2. 如果处理范围 < 10 个元素 -> 直接处理（模拟耗时操作）
 * 3. 特殊条件：处理范围跨越索引3时 -> 抛出异常
 *
 * Fork/Join 异常处理特点：
 * - 子任务中的异常会被捕获并存储在任务对象中
 * - 不会立即传播到主线程
 * - 需要通过 isCompletedAbnormally() 和 getException() 来检测
 */
public class Task extends RecursiveTask<Integer> {
    private static final long serialVersionUID = 1L;

    /**
     * 待处理的数组
     */
    private int array[];

    /**
     * 处理范围的起始索引（ inclusive，包含）
     */
    private int start;

    /**
     * 处理范围的结束索引（ exclusive，不包含）
     */
    private int end;

    /**
     * 构造函数
     *
     * @param start 起始索引
     * @param end   结束索引
     */
    public Task(int array[], int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    /**
     * 核心计算方法
     *
     * 执行策略：
     * - 元素数量 >= 10: 拆分为两个子任务，使用 invokeAll() 并行执行
     * - 元素数量 < 10:
     *   - 如果范围包含索引3（3 > start 且 3 < end），抛出异常
     *   - 否则模拟耗时操作（sleep 1秒）
     *
     * @return 返回值（此例中固定返回0）
     */
    @Override
    protected Integer compute() {
        System.out.printf("Task: Start from %d to %d\n", start, end);

        if (end - start < 10) {
            // ========== 小任务：直接处理 ==========
            // 检查处理范围是否跨越索引3
            // 条件：(3 > start) && (3 < end) 表示范围包含索引3
            if ((3 > start) && (3 < end)) {
                // ========== 抛出异常 ==========
                // 这个异常会被 ForkJoinPool 捕获并存储
                throw new RuntimeException(
                    "This task throws an Exception: Task from " + start + " to " + end
                );
            }

            // 模拟耗时处理操作
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            // ========== 大任务：拆分为子任务 ==========
            // 计算中点
            int mid = (end + start) / 2;

            // 创建两个子任务
            Task task1 = new Task(array, start, mid);  // 处理前半部分 [start, mid)
            Task task2 = new Task(array, mid, end);    // 处理后半部分 [mid, end)

            System.out.printf("Task: Forking two tasks: [%d,%d] and [%d,%d]\n", start, mid, mid, end);

            // ========== invokeAll() ==========
            // 同时提交两个子任务给 ForkJoinPool 执行
            // 这是 Fork/Join 中常用的模式：
            // - fork() 异步提交
            // - join() 等待结果
            // invokeAll() 相当于先 fork() 再 join()，更简洁
            invokeAll(task1, task2);

            // ========== 等待子任务完成并获取结果 ==========
            System.out.printf("Task: Result from %d to %d: %d\n", start, mid, task1.join());
            System.out.printf("Task: Result from %d to %d: %d\n", mid, end, task2.join());
        }

        System.out.printf("Task: End from %d to %d\n", start, end);
        return 0;
    }
}
