package com.ssitao.code.thread.code57.task;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自定义锁 - 实现 Lock 接口
 *
 * 核心设计：
 * 1. 实现 Lock 接口，提供标准锁操作
 * 2. 内部使用自定义 AQS（MyAbstractQueuedSynchronizer）
 * 3. 将 Lock 接口方法委托给 AQS 实现
 *
 * Lock 接口方法：
 * - lock(): 获取锁，不响应中断
 * - lockInterruptibly(): 获取锁，可响应中断
 * - tryLock(): 尝试获取锁，立即返回
 * - tryLock(timeout): 尝试获取锁，等待超时
 * - unlock(): 释放锁
 * - newCondition(): 创建条件变量
 *
 * AQS 相关方法：
 * - acquire(): 独占模式获取，不响应中断
 * - acquireInterruptibly(): 独占模式获取，响应中断
 * - tryAcquireNanos(): 独占模式限时获取
 * - release(): 独占模式释放
 *
 * 与 synchronized 的区别：
 * - synchronized: 自动获取/释放锁，不可中断
 * - Lock: 手动获取/释放，支持超时、中断、条件变量
 *
 * 使用场景：
 * - 需要尝试获取锁而不阻塞
 * - 需要响应中断
 * - 需要超时控制
 * - 需要条件变量（等待/通知）
 */
public class MyLock implements Lock {

    /**
     * 内部使用的 AQS 同步器
     * 所有 Lock 操作都委托给 sync 处理
     */
    private AbstractQueuedSynchronizer sync;

    /**
     * 构造函数
     * 创建自定义 AQS 实例
     */
    public MyLock() {
        sync = new MyAbstractQueuedSynchronizer();
    }

    /**
     * 获取锁
     *
     * 不响应中断，如果锁不可用则一直等待
     */
    @Override
    public void lock() {
        sync.acquire(1);
    }

    /**
     * 获取锁（可中断）
     *
     * 如果锁不可用，则阻塞等待
     * 等待过程中可响应中断
     *
     * @throws InterruptedException 如果等待过程中被中断
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    /**
     * 尝试获取锁
     *
     * 非阻塞，立即返回
     * 尝试获取锁，等待最多1纳秒
     *
     * @return true 表示获取成功，false 表示获取失败
     */
    @Override
    public boolean tryLock() {
        try {
            // 尝试获取，超时1纳秒
            return sync.tryAcquireNanos(1, 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 尝试获取锁（限时）
     *
     * 在指定时间内等待获取锁
     *
     * @param time 最大等待时间
     * @param unit 时间单位
     * @return true 表示获取成功，false 表示超时
     * @throws InterruptedException 如果等待过程中被中断
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        // 转换时间为纳秒后调用 AQS
        return sync.tryAcquireNanos(1, TimeUnit.NANOSECONDS.convert(time, unit));
    }

    /**
     * 释放锁
     *
     * 唤醒等待队列中的一个线程（如果有）
     */
    @Override
    public void unlock() {
        sync.release(1);
    }

    /**
     * 创建条件变量
     *
     * 用于线程间的等待/通知机制
     * 类似于 Object.wait()/notify()
     *
     * @return 条件对象
     */
    @Override
    public Condition newCondition() {
        return sync.new ConditionObject();
    }

}
