package com.ssitao.code.thread.code57.task;

import java.util.concurrent.TimeUnit;

/**
 * 任务类 - 演示自定义锁的使用
 *
 * 实现 Runnable 接口，在独立线程中运行
 * 演示获取锁、持有锁、释放锁的完整流程
 *
 * 设计目的：
 * 1. 展示 MyLock 的基本用法
 * 2. 演示锁的互斥特性（多个线程依次获取锁）
 * 3. 展示 try-finally 确保锁释放
 *
 * 锁的使用模式：
 * try {
 *     lock.lock();
 *     // 临界区代码
 * } finally {
 *     lock.unlock();
 * }
 *
 * 重要原则：
 * - 锁必须在 finally 中释放，确保异常时也能释放
 * - 锁的获取和释放必须配对
 * - 不要在持有锁时执行阻塞操作太久（影响性能）
 */
public class Task implements Runnable {

    /**
     * 共享的自定义锁
     * 所有任务线程共享同一把锁
     */
    private MyLock lock;

    /**
     * 任务名称，用于区分不同的任务
     */
    private String name;

    /**
     * 构造函数
     *
     * @param name 任务名称
     * @param lock 共享的自定义锁
     */
    public Task(String name, MyLock lock) {
        this.lock = lock;
        this.name = name;
    }

    /**
     * 任务执行方法
     *
     * 流程：
     * 1. 获取锁（阻塞等待）
     * 2. 打印获取锁信息
     * 3. 睡眠2秒（模拟工作）
     * 4. 打印释放锁信息
     * 5. 释放锁（在 finally 中执行，确保必定释放）
     */
    @Override
    public void run() {
        // 获取锁
        lock.lock();

        // 打印获取锁信息
        System.out.printf("Task: %s: Take the lock\n", name);

        try {
            // 模拟工作（睡眠2秒）
            TimeUnit.SECONDS.sleep(2);

            // 打印释放锁信息
            System.out.printf("Task: %s: Free the lock\n", name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 重要：在 finally 中释放锁
            // 确保即使发生异常，锁也会被释放
            lock.unlock();
        }
    }

}
