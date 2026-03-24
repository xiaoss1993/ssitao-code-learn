package com.ssitao.code.thread.code43.core;


import com.ssitao.code.thread.code43.task.AddTask;
import com.ssitao.code.thread.code43.task.PollTask;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * ConcurrentLinkedDeque 并发队列演示
 *
 * 本示例展示 ConcurrentLinkedDeque 在多线程环境下的安全操作：
 * 1. 100个线程同时向队列添加元素（每个线程添加10000个）
 * 2. 100个线程同时从队列删除元素（每个线程删除5000对，即10000个）
 *
 * ConcurrentLinkedDeque 特点：
 * - 无界、线程安全的双向队列
 * - 基于 CAS（Compare-And-Swap）实现，无需锁
 * - 适合高并发场景
 * - 迭代器弱一致性，不会抛出 ConcurrentModificationException
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // ========== 1. 创建并发双向队列 ==========
        // ConcurrentLinkedDeque 是线程安全的双向队列
        // 无界队列，可以无限添加元素（直到内存耗尽）
        ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<>();

        // ========== 2. 创建线程数组 ==========
        // 用于存储100个工作线程
        Thread threads[] = new Thread[100];

        // ========== 3. 启动100个添加任务 ==========
        // 每个 AddTask 会向队列添加10000个元素
        // 100个线程 x 10000个元素 = 100万个元素
        for (int i = 0; i < threads.length; i++) {
            AddTask task = new AddTask(list);
            threads[i] = new Thread(task);
            threads[i].start();
        }
        System.out.printf("Main: %d AddTask threads have been launched\n", threads.length);

        // ========== 4. 等待所有添加任务完成 ==========
        // join() 会阻塞主线程，直到每个线程执行完毕
        for (Thread thread : threads) {
            thread.join();
        }

        // ========== 5. 打印添加后的队列大小 ==========
        // 预期结果：100,000 个元素
        System.out.printf("Main: Size of the List: %d\n", list.size());

        // ========== 6. 启动100个删除任务 ==========
        // 每个 PollTask 会从队列两端删除5000对元素（共10000个）
        // 100个线程 x 10000个元素 = 100万个元素
        for (int i = 0; i < threads.length; i++) {
            PollTask task = new PollTask(list);
            threads[i] = new Thread(task);
            threads[i].start();
        }
        System.out.printf("Main: %d PollTask threads have been launched\n", threads.length);

        // ========== 7. 等待所有删除任务完成 ==========
        for (Thread thread : threads) {
            thread.join();
        }

        // ========== 8. 打印删除后的队列大小 ==========
        // 理论上应该是 0（100万添加 - 100万删除）
        // 但由于并发的不确定性，可能略少于0（负数不可能，只是删除多于添加的情况）
        System.out.printf("Main: Size of the List: %d\n", list.size());
    }
}
