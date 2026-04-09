package com.concurrency.chapter2.core;

import com.concurrency.chapter2.task.*;

/**
 * 第2章：同步机制 - 示例入口
 *
 * 涵盖内容：
 * - synchronized实现同步方法/块
 * - 使用Lock实现同步
 * - 使用读写锁实现同步数据访问
 * - 在锁中使用多条件 (Condition)
 */
public class Chapter2Examples {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 第2章：同步机制示例 ===\n");

        // 示例1：synchronized同步方法
        System.out.println("--- 示例1：synchronized同步方法 ---");
        SynchronizedMethodDemo.demo();

        // 示例2：synchronized同步块
        System.out.println("\n--- 示例2：synchronized同步块 ---");
        SynchronizedBlockDemo.demo();

        // 示例3：ReentrantLock锁
        System.out.println("\n--- 示例3：ReentrantLock锁 ---");
        ReentrantLockDemo.demo();

        // 示例4：读写锁
        System.out.println("\n--- 示例4：读写锁 ---");
        ReadWriteLockDemo.demo();

        // 示例5：公平锁与非公平锁
        System.out.println("\n--- 示例5：公平锁与非公平锁 ---");
        FairLockDemo.demo();

        // 示例6：Condition条件变量
        System.out.println("\n--- 示例6：Condition条件变量 ---");
        ConditionDemo.demo();

        System.out.println("\n=== 第2章示例完成 ===");
    }
}
