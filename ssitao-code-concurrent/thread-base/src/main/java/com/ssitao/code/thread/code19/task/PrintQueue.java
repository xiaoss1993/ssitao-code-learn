package com.ssitao.code.thread.code19.task;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 打印队列类，模拟一个打印队列
 * 使用ReentrantLock（可重入锁）来保护共享资源的访问
 */
public class PrintQueue {
    /**
     * 用于控制队列访问的锁
     * 构造参数false表示非公平锁，不保证线程获取锁的顺序
     * ReentrantLock支持可重入特性：同一线程可以多次获取同一个锁
     */
    private final Lock queueLock = new ReentrantLock(false);

    /**
     * 打印一个文档
     * 演示可重入锁的特性：在方法内部两次获取锁
     *
     * @param object 要打印的文档对象（此处未使用）
     */
    public void printJob(Object object) {
        // 第一次获取锁
        queueLock.lock();
        try {
            // 模拟打印操作，随机生成0-10秒的打印时间
            long duration = (long) (Math.random() * 10000);
            System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",
                    Thread.currentThread().getName(), (duration / 1000));
            // 休眠模拟实际打印消耗的时间
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 确保锁被释放
            queueLock.unlock();
        }

        // 第二次获取锁 - 演示ReentrantLock的可重入特性
        // 由于是同一线程，且已经持有锁，所以可以再次成功获取锁
        queueLock.lock();
        try {
            long duration = (long) (Math.random() * 10000);
            System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",
                    Thread.currentThread().getName(), (duration / 1000));
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 必须解锁两次以完全释放锁
            queueLock.unlock();
        }
    }
}
