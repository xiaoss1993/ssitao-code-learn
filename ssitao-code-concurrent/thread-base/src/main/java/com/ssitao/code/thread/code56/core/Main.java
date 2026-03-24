package com.ssitao.code.thread.code56.core;


import com.ssitao.code.thread.code56.task.Task;

import java.util.concurrent.ForkJoinPool;

/**
 * 主程序 - ForkJoinTask 自定义实现示例
 *
 * 演示直接继承 ForkJoinTask 实现自定义任务
 *
 * 核心概念：
 * 1. ForkJoinTask vs RecursiveTask/RecursiveAction：
 *    - RecursiveTask/RecursiveAction 是 ForkJoinTask 的便捷抽象类
 *    - 直接继承 ForkJoinTask 提供更底层的控制
 *    - 需要实现 exec() 方法代替 compute() 方法
 *
 * 2. ForkJoinTask 核心方法：
 *    - exec(): 任务执行逻辑（替代 compute()）
 *    - getRawResult(): 返回任务结果（Void 表示无返回）
 *    - setRawResult(): 设置任务结果
 *
 * 3. ForkJoinPool 配置：
 *    - 使用默认构造函数，创建默认并行级别的池
 *    - 默认并行级别 = Runtime.availableProcessors()
 *
 * 4. invoke() vs execute()：
 *    - invoke(): 同步等待任务完成
 *    - execute(): 异步执行，不等待
 *
 * 执行流程：
 * 1. 创建数组（10000个元素）
 * 2. 创建默认配置的 ForkJoinPool
 * 3. 创建 Task 并通过 invoke() 执行
 * 4. 关闭线程池
 *
 * 预期结果：
 * - Task 使用分治策略拆分数组
 * - 每个子任务完成后打印执行时间
 * - 最终所有元素值从 0 变为 1
 */
public class Main {

    public static void main(String[] args) throws Exception {

        // 创建数组并初始化（默认值为0）
        int array[] = new int[10000];

        // 创建 ForkJoinPool，使用默认配置
        // 默认并行级别 = CPU 核心数
        ForkJoinPool pool = new ForkJoinPool();

        // 创建任务
        // 参数：任务名称、数组、起始索引、结束索引
        Task task = new Task("Task", array, 0, array.length);

        // 同步执行任务
        // invoke() 会阻塞直到任务完成
        pool.invoke(task);

        // 关闭线程池
        pool.shutdown();

        System.out.printf("Main: End of the program.\n");

    }

}
