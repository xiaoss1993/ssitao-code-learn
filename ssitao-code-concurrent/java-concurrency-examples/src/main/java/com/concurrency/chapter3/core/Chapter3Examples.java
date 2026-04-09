package com.concurrency.chapter3.core;

import com.concurrency.chapter3.task.*;

/**
 * 第3章：并发控制 - 示例入口
 *
 * 涵盖内容：
 * - Semaphore信号量
 * - CountDownLatch倒计时门闩
 * - CyclicBarrier循环屏障
 * - Phaser阶段同步器
 * - Exchanger数据交换
 */
public class Chapter3Examples {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 第3章：并发控制示例 ===\n");

        // 示例1：Semaphore信号量
        System.out.println("--- 示例1：Semaphore信号量 ---");
        SemaphoreDemo.demo();

        // 示例2：CountDownLatch
        System.out.println("\n--- 示例2：CountDownLatch ---");
        CountDownLatchDemo.demo();

        // 示例3：CyclicBarrier
        System.out.println("\n--- 示例3：CyclicBarrier ---");
        CyclicBarrierDemo.demo();

        // 示例4：Phaser
        System.out.println("\n--- 示例4：Phaser阶段同步器 ---");
        PhaserDemo.demo();

        // 示例5：Exchanger
        System.out.println("\n--- 示例5：Exchanger数据交换 ---");
        ExchangerDemo.demo();

        System.out.println("\n=== 第3章示例完成 ===");
    }
}
