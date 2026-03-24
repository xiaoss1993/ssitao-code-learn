package com.ssitao.code.thread.code55.core;


import com.ssitao.code.thread.code55.task.MyRecursiveTask;
import com.ssitao.code.thread.code55.task.MyWorkerThreadFactory;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * 主程序 - Fork/Join 框架示例
 *
 * 演示使用 ForkJoinPool 执行分治任务
 *
 * 核心概念：
 * 1. Fork/Join 框架 - Java 7 引入的并行计算框架
 *    - 基于工作窃取（Work-Stealing）算法
 *    - 适合将大任务分解为小任务并行执行
 *    - 工作线程从自己的双端队列取任务，空闲时从其他线程队列尾部偷任务
 *
 * 2. ForkJoinPool 参数：
 *    - parallelism: 并行级别（工作线程数）
 *    - factory: 工作线程工厂
 *    - handler: 未捕获异常的处理器
 *    - asyncMode: 异步模式（false表示FIFO队列）
 *
 * 3. RecursiveTask vs RecursiveAction：
 *    - RecursiveTask: 有返回值的任务
 *    - RecursiveAction: 无返回值的任务
 *
 * 4. 工作窃取算法：
 *    - 每个工作线程有自己的任务队列（双端队列）
 *    - 任务从队列头部获取（自己执行）
 *    - 空闲时从其他线程队列尾部偷任务（窃取）
 *    - 减少线程竞争，提高CPU利用率
 *
 * 执行流程：
 * 1. 创建自定义工作线程工厂
 * 2. 创建 ForkJoinPool（4个工作线程）
 * 3. 准备100000个元素的数组，每个元素值为1
 * 4. 创建 MyRecursiveTask 计算数组总和
 * 5. 执行任务并等待结果
 *
 * 预期结果：
 * - 数组总和 = 100000（100000个1相加）
 * - 每个工作线程处理的任务数量会被打印
 */
public class Main {

    public static void main(String[] args) throws Exception {

        // 创建自定义工作线程工厂
        // 用于创建自定义的 MyWorkerThread
        MyWorkerThreadFactory factory = new MyWorkerThreadFactory();

        // 创建 ForkJoinPool
        // 参数：并行级别4、工作线程工厂、自定义异常处理器、异步模式false
        ForkJoinPool pool = new ForkJoinPool(4, factory, null, false);

        // 创建数组并初始化
        int array[] = new int[100000];

        // 初始化数组，每个元素值为1
        for (int i = 0; i < array.length; i++) {
            array[i] = 1;
        }

        // 创建递归任务，计算数组总和
        // 初始范围：[0, 100000)
        MyRecursiveTask task = new MyRecursiveTask(array, 0, array.length);

        // 异步执行任务
        pool.execute(task);

        // 等待任务完成（阻塞直到结果可用）
        task.join();

        // 平缓关闭线程池
        pool.shutdown();

        // 等待线程池终止
        pool.awaitTermination(1, TimeUnit.DAYS);

        // 打印最终结果
        System.out.printf("Main: Result: %d\n", task.get());

        System.out.printf("Main: End of the program\n");
    }

}
