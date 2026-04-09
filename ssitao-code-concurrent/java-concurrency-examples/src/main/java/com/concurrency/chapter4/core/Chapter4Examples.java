package com.concurrency.chapter4.core;

import com.concurrency.chapter4.task.*;

/**
 * 第4章：线程执行器 - 示例入口
 *
 * 涵盖内容：
 * - 创建线程执行器
 * - Callable与Future
 * - invokeAny和invokeAll
 * - ScheduledExecutorService定时执行
 * - CompletionService
 * - 自定义拒绝策略
 */
public class Chapter4Examples {

    public static void main(String[] args) throws Exception {
        System.out.println("=== 第4章：线程执行器示例 ===\n");

        // 示例1：创建和使用执行器
        System.out.println("--- 示例1：基础ExecutorService ---");
        BasicExecutorDemo.demo();

        // 示例2：Callable与Future
        System.out.println("\n--- 示例2：Callable与Future ---");
        CallableFutureDemo.demo();

        // 示例3：invokeAny和invokeAll
        System.out.println("\n--- 示例3：invokeAny和invokeAll ---");
        InvokeDemo.demo();

        // 示例4：定时执行器
        System.out.println("\n--- 示例4：ScheduledExecutorService ---");
        ScheduledExecutorDemo.demo();

        // 示例5：CompletionService
        System.out.println("\n--- 示例5：CompletionService ---");
        CompletionServiceDemo.demo();

        // 示例6：自定义拒绝策略
        System.out.println("\n--- 示例6：自定义拒绝策略 ---");
        RejectedExecutionDemo.demo();

        System.out.println("\n=== 第4章示例完成 ===");
    }
}
