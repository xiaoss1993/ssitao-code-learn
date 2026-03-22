package com.ssitao.code.thread.code17.task;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 打印队列类 - 模拟文档打印服务
 *
 * 使用ReentrantLock（可重入锁）来控制对打印资源的访问：
 * - 保证同一时刻只有一个线程能执行打印操作
 * - ReentrantLock是synchronized的替代方案，提供更灵活的锁控制
 * - 支持公平/非公平锁模式（默认非公平）
 *
 * ReentrantLock特点：
 * 1. 可重入：同一线程可以多次获取同一把锁
 * 2. 支持tryLock()：非阻塞获取锁
 * 3. 支持公平锁：按照等待时间顺序获取锁
 */
public class PrintQueue {
    /**
     * 使用ReentrantLock实现互斥访问
     * final修饰保证锁对象引用不可变
     */
    private final Lock queueLock = new ReentrantLock();

    /**
     * 执行打印任务
     *
     * 使用try-finally确保锁始终被释放：
     * - lock()获取锁
     * - try中执行打印逻辑（模拟0-10秒的随机打印时间）
     * - finally中unlock()释放锁（即使发生异常也会释放）
     *
     * @param object 要打印的文档对象（此处未实际使用）
     */
    public void printJob(Object object) {
        // 获取锁，如果锁被其他线程持有则等待
        queueLock.lock();
        try {
            // 模拟随机打印时长（0-10秒）
            long duration = (long) (Math.random() * 10000);
            System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",
                    Thread.currentThread().getName(), (duration / 1000));
            // 模拟打印过程
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // 打印异常信息
            e.printStackTrace();
        } finally {
            // 关键：必须释放锁，否则其他线程将永远阻塞
            queueLock.unlock();
        }
    }
}
