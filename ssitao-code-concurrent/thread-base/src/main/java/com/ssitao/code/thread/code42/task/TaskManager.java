package com.ssitao.code.thread.code42.task;


import com.ssitao.code.thread.code42.utils.SearchNumberTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

/**
 * 任务管理器：管理 Fork/Join 任务的生命周期
 *
 * 核心功能：
 * 1. 跟踪所有活跃的搜索任务
 * 2. 在找到目标后，取消所有其他未完成的任务
 *
 * 工作原理：
 * - SearchNumberTask 在拆分任务时，会将子任务注册到 TaskManager
 * - 当某个任务找到目标数字后，调用 cancelTasks() 取消其他任务
 * - cancelTasks() 遍历所有任务，对除了当前任务之外的任务调用 cancel(true)
 *
 * 任务取消机制：
 * - ForkJoinTask.cancel(true) 的参数 true 表示即使任务正在执行，也尝试中断它
 * - 但需要注意的是，cancel() 并不保证一定能取消任务，特别是当任务正在 sleep 时
 * - 取消操作只是设置任务的取消状态，但任务是否立即响应取决于任务的实现
 */
public class TaskManager {

    /**
     * 存储所有活跃的 ForkJoinTask
     *
     * 使用 ForkJoinTask<Integer> 作为类型，因为它：
     * 1. 是 SearchNumberTask 的父类
     * 2. 提供了 cancel() 方法用于取消任务
     */
    private List<ForkJoinTask<Integer>> tasks;

    /**
     * 构造函数：初始化任务列表
     */
    public TaskManager() {
        tasks = new ArrayList<>();
    }

    /**
     * 添加任务到管理器
     *
     * 当 SearchNumberTask 拆分出新任务时，调用此方法注册到管理器
     * 这样管理器就能跟踪所有活跃的任务
     *
     * @param task 要添加的任务
     */
    public void addTask(ForkJoinTask<Integer> task) {
        tasks.add(task);
    }

    /**
     * 取消所有任务（除了指定的任务）
     *
     * 这是实现"找到即取消"策略的关键方法
     *
     * 取消逻辑：
     * 1. 遍历所有任务
     * 2. 对于每个不是当前任务的任务，调用 cancel(true)
     * 3. cancel(true) 会尝试取消任务：
     *    - 如果任务还未开始，会被标记为已取消，不会执行
     *    - 如果任务正在执行，会尝试中断执行线程
     *    - 如果任务已经完成，取消操作无效
     *
     * @param cancelTask 要保留的任务（找到目标的那个任务），其他任务都会被取消
     */
    public void cancelTasks(ForkJoinTask<Integer> cancelTask) {
        // 遍历所有任务
        for (ForkJoinTask<Integer> task : tasks) {
            // 跳过当前任务（找到目标的那个任务）
            if (task != cancelTask) {
                // ========== 取消任务 ==========
                // 参数 true 表示即使任务正在执行，也尝试中断
                // 注意：这只是设置取消标志，任务是否响应取决于其实现
                task.cancel(true);

                // 调用任务的 writeCancelMessage() 输出取消信息
                // 需要强制转换为 SearchNumberTask 才能调用此方法
                ((SearchNumberTask) task).writeCancelMessage();
            }
        }
    }
}
