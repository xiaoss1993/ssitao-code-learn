package com.ssitao.code.thread.code49.task;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 减法器 - 对数组每个元素执行 -1 操作
 *
 * 实现 Runnable 接口，在独立线程中运行
 * 负责将共享数组中的每个元素减少 1
 *
 * 设计特点：
 * 1. 所有 Decrementer 线程共享同一个 vector 和同一个 Decrementer 实例
 * 2. 每个线程遍历整个数组，对每个元素执行 getAndDecrement()
 * 3. getAndDecrement() 是原子操作，保证线程安全
 *
 * 原子性保证：
 * - getAndDecrement(i) 等价于 get(i) - 1，但整个过程是原子的
 * - 多个线程同时对同一元素执行 -1，不会丢失更新
 *
 * 操作计数：
 * - 100个线程 × 1000个元素 × 1次 = 100,000 次 -1 操作
 *
 * 与 Incrementer 的配对：
 * - 每个元素的最终值 = 0 + 100×(+1) + 100×(-1) = 0
 * - 理论上所有元素最终都应该为 0
 */
public class Decrementer implements Runnable {

    /**
     * 共享的原子整型数组
     * 所有 Incrementer 和 Decrementer 线程共享此数组
     */
    private AtomicIntegerArray vector;

    /**
     * 构造函数，初始化共享数组
     *
     * @param vector 共享的原子整型数组
     */
    public Decrementer(AtomicIntegerArray vector) {
        this.vector = vector;
    }

    /**
     * 任务执行入口
     *
     * 核心逻辑：
     * 遍历数组，对每个元素执行原子 -1 操作
     * getAndDecrement(i) 等价于：
     *   1. 读取 vector[i] 的当前值
     *   2. 将值 -1
     *   3. 写回 vector[i]
     *   4. 返回旧值
     * 整个过程是原子的，不会被其他线程中断
     */
    @Override
    public void run() {
        // 遍历数组的每个元素
        for (int i = 0; i < vector.length(); i++) {
            // 对第 i 个元素执行原子 -1 操作
            vector.getAndDecrement(i);
        }
    }

}
