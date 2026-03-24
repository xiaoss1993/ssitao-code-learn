package com.ssitao.code.thread.code42.utils;


import com.ssitao.code.thread.code42.task.TaskManager;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 并行搜索任务类：在数组中查找指定数字
 *
 * Fork/Join 任务拆分策略：
 * - 搜索范围 > 10 个元素：拆分成两个子任务并行搜索
 * - 搜索范围 <= 10 个元素：顺序搜索
 *
 * 任务取消机制：
 * - 当某个子任务找到目标数字后，立即调用 manager.cancelTasks()
 * - cancelTasks() 会取消所有其他未完成的任务
 * - 这样可以避免不必要的计算，提高效率
 *
 * @param <Integer> 返回值类型：找到的位置（未找到返回 -1）
 */
public class SearchNumberTask extends RecursiveTask<Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * 未找到目标时返回的特殊值
     */
    private final static int NOT_FOUND = -1;

    /**
     * 待搜索的数组
     */
    private int numbers[];

    /**
     * 搜索范围的起始索引（inclusive，包含）
     */
    private int start;

    /**
     * 搜索范围的结束索引（exclusive，不包含）
     */
    private int end;

    /**
     * 要查找的目标数字
     */
    private int number;

    /**
     * 任务管理器引用
     * 用于在找到目标后取消所有其他未完成的任务
     */
    private TaskManager manager;

    /**
     * 构造函数
     *
     * @param numbers 待搜索的数组
     * @param start   搜索起始索引（inclusive）
     * @param end     搜索结束索引（exclusive）
     * @param number  要查找的数字
     * @param manager 任务管理器，用于取消其他任务
     */
    public SearchNumberTask(int numbers[], int start, int end, int number, TaskManager manager) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
        this.number = number;
        this.manager = manager;
    }

    /**
     * 核心计算方法：根据范围大小决定拆分或直接搜索
     *
     * 决策逻辑：
     * - 如果范围 > 10：调用 launchTasks() 拆分为两个子任务
     * - 如果范围 <= 10：调用 lookForNumber() 顺序搜索
     *
     * @return 目标数字在数组中的位置（未找到返回 -1）
     */
    @Override
    protected Integer compute() {
        System.out.println("Task: " + start + ":" + end);
        int ret;
        if (end - start > 10) {
            // ========== 大范围：拆分任务 ==========
            ret = launchTasks();
        } else {
            // ========== 小范围：顺序搜索 ==========
            ret = lookForNumber();
        }
        return ret;
    }

    /**
     * 顺序搜索方法
     *
     * 搜索 [start, end) 范围内的元素，查找目标数字
     * 每次检查之间模拟 1 秒延时（模拟耗时操作）
     * 找到目标后立即取消所有其他任务
     *
     * @return 目标数字的位置（未找到返回 NOT_FOUND = -1）
     */
    private int lookForNumber() {
        for (int i = start; i < end; i++) {
            // 检查当前元素是否为目标数字
            if (numbers[i] == number) {
                System.out.printf("Task: Number %d found in position %d\n", number, i);

                // ========== 找到目标：取消所有其他任务 ==========
                // 这是关键的一步：一旦找到目标，立即取消其他未完成的任务
                // 这样可以避免不必要的计算
                manager.cancelTasks(this);

                // 返回找到的位置
                return i;
            }

            // 模拟耗时操作（搜索每个元素需要1秒）
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 未找到目标
        return NOT_FOUND;
    }

    /**
     * 任务拆分方法：将搜索范围分成两部分并行执行
     *
     * 拆分策略：
     * 1. 计算中点 mid = (start + end) / 2
     * 2. 创建两个子任务：[start, mid) 和 [mid, end)
     * 3. 将子任务注册到 TaskManager 进行管理
     * 4. fork() 异步执行两个子任务
     * 5. join() 等待并获取结果，先返回的结果有效
     *
     * @return 目标数字的位置（未找到返回 -1）
     */
    private int launchTasks() {
        // ========== 1. 计算中点，拆分范围 ==========
        int mid = (start + end) / 2;

        // ========== 2. 创建两个子任务 ==========
        SearchNumberTask task1 = new SearchNumberTask(numbers, start, mid, number, manager);
        SearchNumberTask task2 = new SearchNumberTask(numbers, mid, end, number, manager);

        // ========== 3. 将子任务注册到管理器 ==========
        // TaskManager 需要跟踪所有任务，以便在找到目标后取消它们
        manager.addTask(task1);
        manager.addTask(task2);

        // ========== 4. 异步执行子任务 ==========
        // fork() 将任务提交到线程池，立即返回，不阻塞
        task1.fork();
        task2.fork();

        int returnValue;

        // ========== 5. 等待子任务完成 ==========
        // 先等待 task1 的结果
        returnValue = task1.join();

        // 如果 task1 找到了目标（返回值 != -1），直接返回
        // 注意：task2 不会被取消，而是会继续运行完成
        if (returnValue != -1) {
            return returnValue;
        }

        // task1 没找到，等待 task2 的结果
        returnValue = task2.join();
        return returnValue;
    }

    /**
     * 取消消息输出方法
     *
     * 当任务被取消时调用，输出友好的取消提示信息
     */
    public void writeCancelMessage() {
        System.out.printf("Task: Cancelled task from %d to %d\n", start, end);
    }
}
