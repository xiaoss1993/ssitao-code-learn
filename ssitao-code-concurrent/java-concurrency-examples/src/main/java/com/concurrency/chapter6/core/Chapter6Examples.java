package com.concurrency.chapter6.core;

import com.concurrency.chapter6.task.*;

/**
 * 第6章：并发集合 - 示例入口
 *
 * 涵盖内容：
 * - ConcurrentLinkedDeque非阻塞列表
 * - LinkedBlockingDeque阻塞列表
 * - PriorityBlockingQueue优先级队列
 * - DelayQueue延迟队列
 * - ConcurrentHashMap并发映射
 * - ThreadLocalRandom并发随机数
 * - AtomicInteger原子变量
 * - AtomicIntegerArray原子数组
 */
public class Chapter6Examples {

    public static void main(String[] args) throws Exception {
        System.out.println("=== 第6章：并发集合示例 ===\n");

        // 示例1：ConcurrentLinkedDeque
        System.out.println("--- 示例1：ConcurrentLinkedDeque ---");
        ConcurrentLinkedDequeDemo.demo();

        // 示例2：LinkedBlockingDeque
        System.out.println("\n--- 示例2：LinkedBlockingDeque ---");
        LinkedBlockingDequeDemo.demo();

        // 示例3：PriorityBlockingQueue
        System.out.println("\n--- 示例3：PriorityBlockingQueue ---");
        PriorityBlockingQueueDemo.demo();

        // 示例4：DelayQueue
        System.out.println("\n--- 示例4：DelayQueue ---");
        DelayQueueDemo.demo();

        // 示例5：ConcurrentHashMap
        System.out.println("\n--- 示例5：ConcurrentHashMap ---");
        ConcurrentHashMapDemo.demo();

        // 示例6：原子变量
        System.out.println("\n--- 示例6：原子变量 ---");
        AtomicDemo.demo();

        System.out.println("\n=== 第6章示例完成 ===");
    }
}
