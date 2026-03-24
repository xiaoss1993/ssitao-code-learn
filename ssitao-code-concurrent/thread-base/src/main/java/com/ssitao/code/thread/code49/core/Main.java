package com.ssitao.code.thread.code49.core;


import com.ssitao.code.thread.code49.task.Decrementer;
import com.ssitao.code.thread.code49.task.Incrementer;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 主程序 - AtomicIntegerArray 原子整型数组并发示例
 *
 * 演示使用 AtomicIntegerArray 实现线程安全的数组操作
 * 100个线程对数组元素+1，100个线程对数组元素-1
 *
 * 核心概念：
 * 1. AtomicIntegerArray - 原子整型数组
 *    - 支持对数组每个元素进行原子操作
 *    - 底层使用 CAS 算法保证线程安全
 *    - 比 synchronized 的数组操作性能更高
 *
 * 2. 操作原理：
 *    - 100个 Incrementer 线程，每个对所有1000个元素执行+1操作
 *    - 100个 Decrementer 线程，每个对所有1000个元素执行-1操作
 *    - 每个元素：初始值0，经过100次+1和100次-1后，应该恢复为0
 *
 * 3. 预期结果：
 *    - 所有元素最终值应该为 0
 *    - 如果使用普通数组，非原子操作会导致最终值不确定
 *
 * 注意事项：
 * - AtomicIntegerArray 对每个元素的操作是原子的
 * - 但多个元素之间的操作不是原子的（如跨元素的范围操作）
 */
public class Main {
    public static void main(String[] args) {

        // 线程数量
        final int THREADS = 100;
        // 创建原子整型数组，包含1000个元素，初始值都为0
        AtomicIntegerArray vector = new AtomicIntegerArray(1000);

        // 创建加法器任务（所有线程共享同一个 Incrementer 实例）
        // 每个 Incrementer 会对所有元素执行 +1 操作
        Incrementer incrementer = new Incrementer(vector);

        // 创建减法器任务（所有线程共享同一个 Decrementer 实例）
        // 每个 Decrementer 会对所有元素执行 -1 操作
        Decrementer decrementer = new Decrementer(vector);

        // 创建线程数组
        Thread threadIncrementer[] = new Thread[THREADS];
        Thread threadDecrementer[] = new Thread[THREADS];

        // 启动100个加法线程和100个减法线程
        for (int i = 0; i < THREADS; i++) {
            threadIncrementer[i] = new Thread(incrementer);
            threadDecrementer[i] = new Thread(decrementer);

            threadIncrementer[i].start();
            threadDecrementer[i].start();
        }

        // 等待所有任务完成
        for (int i = 0; i < THREADS; i++) {
            try {
                threadIncrementer[i].join();
                threadDecrementer[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 验证结果：所有元素应该都为0
        // 如果有元素不为0，说明存在并发问题（理论上不应该发生）
        for (int i = 0; i < vector.length(); i++) {
            if (vector.get(i) != 0) {
                System.out.println("Vector[" + i + "] : " + vector.get(i));
            }
        }

        System.out.println("Main: End of the example");
    }
}
