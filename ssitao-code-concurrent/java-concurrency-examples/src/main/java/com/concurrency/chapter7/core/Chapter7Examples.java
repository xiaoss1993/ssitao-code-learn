package com.concurrency.chapter7.core;

import com.concurrency.chapter7.task.*;
import com.concurrency.chapter7.factory.*;

/**
 * 第7章：高级定制 - 示例入口
 *
 * 涵盖内容：
 * - 定制ThreadPoolExecutor类
 * - 基于优先级的Executor类
 * - 实现ThreadFactory接口生成定制线程
 * - 定制RejectedExecutionHandler
 * - 实现基于AQS的自定义锁
 */
public class Chapter7Examples {

    public static void main(String[] args) throws Exception {
        System.out.println("=== 第7章：高级定制示例 ===\n");

        // 示例1：自定义ThreadFactory
        System.out.println("--- 示例1：自定义ThreadFactory ---");
        CustomThreadFactoryExample.demo();

        // 示例2：自定义RejectedExecutionHandler
        System.out.println("\n--- 示例2：自定义RejectedExecutionHandler ---");
        CustomRejectedHandlerExample.demo();

        // 示例3：基于AQS的自定义锁
        System.out.println("\n--- 示例3：基于AQS的自定义锁 ---");
        CustomAQSLockExample.demo();

        // 示例4：优先级线程池
        System.out.println("\n--- 示例4：优先级线程池 ---");
        PriorityExecutorExample.demo();

        System.out.println("\n=== 第7章示例完成 ===");
    }
}
