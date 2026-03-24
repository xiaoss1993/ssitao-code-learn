package com.ssitao.code.thread.code57.task;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 自定义 AQS 同步器 - 实现独占模式互斥锁
 *
 * 核心设计：
 * 1. 继承 AbstractQueuedSynchronizer（AQS）
 * 2. 实现独占模式的 tryAcquire() 和 tryRelease()
 * 3. 使用 AtomicInteger 作为同步状态
 *
 * AQS 同步状态（state）的含义：
 * - state = 0：锁未被持有（可用）
 * - state = 1：锁已被持有
 *
 * tryAcquire() 成功条件：
 * - 当前 state = 0（锁可用）
 * - 使用 CAS 操作将 state 从 0 改为 1
 * - compareAndSet(0, 1) 原子操作保证线程安全
 *
 * tryRelease() 成功条件：
 * - 当前 state = 1（锁已被持有）
 * - 使用 CAS 操作将 state 从 1 改为 0
 * - 只有持有锁的线程才能释放锁
 *
 * 为什么使用 AtomicInteger：
 * - 保证 state 读写的原子性
 * - compareAndSet 使用 CAS 算法，无锁设计
 * - 避免 synchronized 的性能开销
 *
 * CLH 队列机制：
 * - AQS 内部维护等待线程的 CLH 队列
 * - 获取锁失败的线程会加入队列等待
 * - 锁释放时唤醒队列头部的线程
 */
public class MyAbstractQueuedSynchronizer extends AbstractQueuedSynchronizer {

    /**
     * 序列化版本ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 同步状态
     * 使用 AtomicInteger 保证原子性
     * 0 = 锁可用，1 = 锁已持有
     */
    private AtomicInteger state;

    /**
     * 构造函数
     * 初始化同步状态为 0（锁可用）
     */
    public MyAbstractQueuedSynchronizer() {
        state = new AtomicInteger(0);
    }

    /**
     * 尝试获取同步状态（独占模式）
     *
     * AQS 框架会在获取锁时调用此方法
     * 子类必须实现此方法定义获取逻辑
     *
     * @param arg 获取操作的参数（通常为1）
     * @return true 表示获取成功，false 表示获取失败
     *
     * 实现逻辑：
     * 使用 CAS 操作尝试将 state 从 0 改为 1
     * - 如果当前 state = 0，原子地设置为 1，返回 true
     * - 如果当前 state != 0，返回 false
     */
    @Override
    protected boolean tryAcquire(int arg) {
        return state.compareAndSet(0, 1);
    }

    /**
     * 尝试释放同步状态（独占模式）
     *
     * AQS 框架会在释放锁时调用此方法
     * 子类必须实现此方法定义释放逻辑
     *
     * @param arg 释放操作的参数（通常为1）
     * @return true 表示释放成功，false 表示释放失败
     *
     * 实现逻辑：
     * 使用 CAS 操作尝试将 state 从 1 改为 0
     * - 如果当前 state = 1，原子地设置为 0，返回 true
     * - 如果当前 state != 1，返回 false
     */
    @Override
    protected boolean tryRelease(int arg) {
        return state.compareAndSet(1, 0);
    }
}
