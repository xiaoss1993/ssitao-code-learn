package com.ssitao.code.thread.code42.core;

import com.ssitao.code.thread.code42.task.TaskManager;
import com.ssitao.code.thread.code42.utils.ArrayGenerator;
import com.ssitao.code.thread.code42.utils.SearchNumberTask;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Fork/Join 框架演示：并行搜索与任务取消
 *
 * 本示例展示：
 * 1. 如何使用 ForkJoinPool 并行搜索数组
 * 2. 如何在找到目标后取消其他未完成的任务
 * 3. TaskManager 如何管理并取消 Fork/Join 任务
 */
public class Main {
    public static void main(String[] args) {
        // ========== 1. 生成待搜索的数组 ==========
        // 创建一个包含1000个随机整数的数组（值范围 0-9）
        ArrayGenerator generator = new ArrayGenerator();
        int array[] = generator.generateArray(1000);

        // ========== 2. 创建任务管理器 ==========
        // TaskManager 负责跟踪所有子任务，并在找到目标后取消其他任务
        TaskManager manager = new TaskManager();

        // ========== 3. 创建 ForkJoinPool ==========
        // 使用默认构造函数创建分合池（并行级别等于 CPU 核心数）
        ForkJoinPool pool = new ForkJoinPool();

        // ========== 4. 创建搜索任务 ==========
        // 参数说明：
        // - array: 待搜索数组
        // - 0: 搜索起始索引（inclusive）
        // - 1000: 搜索结束索引（exclusive）
        // - 5: 要查找的数字
        // - manager: 任务管理器，用于取消其他任务
        SearchNumberTask task = new SearchNumberTask(array, 0, 1000, 5, manager);

        // ========== 5. 执行任务 ==========
        // 异步执行任务，不会阻塞主线程
        pool.execute(task);

        // ========== 6. 关闭线程池 ==========
        pool.shutdown();

        // ========== 7. 等待任务完成 ==========
        // 阻塞最多1天，等待所有任务完成
        try {
            pool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ========== 8. 任务完成后打印结束信息 ==========
        // 注意：即使任务被取消（因找到目标），这里也会继续执行
        System.out.printf("Main: The program has finished\n");
    }
}
