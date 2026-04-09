package com.concurrency.chapter1.core;

import com.concurrency.chapter1.factory.CustomThreadFactoryDemo;
import com.concurrency.chapter1.task.*;
import com.concurrency.chapter1.group.ExceptionHandler;
import com.concurrency.chapter1.group.ThreadGroupDemo;
import com.concurrency.chapter1.factory.CustomThreadFactory;

/**
 * 第1章：线程基础 - 示例入口
 *
 * 涵盖内容：
 * - 线程的创建和运行
 * - 线程信息的获取和设置
 * - 线程的中断
 * - 线程的休眠和恢复
 * - 等待线程的终止
 * - 守护线程的创建和运行
 * - 线程局部变量的使用
 * - 线程分组
 * - 使用工厂类创建线程
 */
public class Chapter1Examples {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 第1章：线程基础示例 ===\n");

        // 示例1：线程的创建和运行
        System.out.println("--- 示例1：线程创建和运行 ---");
        Thread thread = new Thread(new RunnableTask("RunnableTask"));
        thread.start();
        thread.join();

        // 示例2：线程信息获取和设置
        System.out.println("\n--- 示例2：线程信息获取和设置 ---");
        ThreadInfoDemo.infoDemo();

        // 示例3：线程中断
        System.out.println("\n--- 示例3：线程中断 ---");
        ThreadInterruptDemo.interruptDemo();

        // 示例4：线程休眠
        System.out.println("\n--- 示例4：线程休眠 ---");
        ThreadSleepDemo.sleepDemo();

        // 示例5：等待线程终止 (join)
        System.out.println("\n--- 示例5：等待线程终止 ---");
        ThreadJoinDemo.joinDemo();

        // 示例6：守护线程
        System.out.println("\n--- 示例6：守护线程 ---");
        DaemonThreadDemo.daemonDemo();

        // 示例7：ThreadLocal线程局部变量
        System.out.println("\n--- 示例7：ThreadLocal ---");
        ThreadLocalDemo.threadLocalDemo();

        // 示例8：线程分组
        System.out.println("\n--- 示例8：线程分组 ---");
        ThreadGroupDemo.groupDemo();

        // 示例9：自定义线程工厂
        System.out.println("\n--- 示例9：自定义线程工厂 ---");
        CustomThreadFactoryDemo.factoryDemo();

        System.out.println("\n=== 第1章示例完成 ===");
    }
}
