package com.concurrency.chapter5.core;

import com.concurrency.chapter5.task.*;

/**
 * 第5章：Fork/Join框架 - 示例入口
 *
 * 涵盖内容：
 * - 创建Fork-Join线程池
 * - 合并任务的结果 (RecursiveTask)
 * - 异步运行任务
 * - 在任务中抛出异常
 * - 取消任务
 */
public class Chapter5Examples {

    public static void main(String[] args) throws Exception {
        System.out.println("=== 第5章：Fork/Join框架示例 ===\n");

        // 示例1：基础ForkJoinPool
        System.out.println("--- 示例1：基础ForkJoinPool ---");
        ForkJoinPoolDemo.demo();

        // 示例2：RecursiveTask求和
        System.out.println("\n--- 示例2：RecursiveTask求和 ---");
        SumTaskDemo.demo();

        // 示例3：RecursiveAction无返回值
        System.out.println("\n--- 示例3：RecursiveAction ---");
        PrintTaskDemo.demo();

        // 示例4：Fork-Join异常处理
        System.out.println("\n--- 示例4：Fork-Join异常处理 ---");
        ForkJoinExceptionDemo.demo();

        System.out.println("\n=== 第5章示例完成 ===");
    }
}
