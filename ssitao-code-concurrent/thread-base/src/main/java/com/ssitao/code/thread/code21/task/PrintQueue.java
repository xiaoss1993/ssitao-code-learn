package com.ssitao.code.thread.code21.task;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 打印队列类
 * 使用信号量(Semaphore)来控制对打印作业的并发访问
 *
 * Semaphore是Java并发包java.util.concurrent中的一个同步工具类
 * 它维护了一组许可证(permits)，线程可以通过acquire()获取许可证，
 * 通过release()释放许可证。当许可证数量为1时，Semaphore就相当于
 * 一个互斥锁(Mutex)，确保同一时刻只有一个线程能够访问共享资源
 */
public class PrintQueue {
    /**
     * 信号量，控制打印队列的访问
     * 初始化为1，表示同时只允许1个线程访问打印资源
     * 这实现了对打印机的互斥访问
     */
    private final Semaphore semaphore;

    /**
     * 构造函数，初始化信号量
     * 创建了一个只有1个许可证的信号量
     */
    public PrintQueue() {
        // 初始许可证数量为1，确保互斥访问
        semaphore = new Semaphore(1);
    }

    /**
     * 模拟文档打印的方法
     * 使用信号量控制对打印机的访问
     *
     * @param document 需要打印的对象
     */
    public void printJob(Object document) {
        try {
            // acquire()方法尝试获取一个许可证
            // 如果许可证已被其他线程占用（还没释放），
            // 当前线程会被阻塞（休眠），直到获得这个许可证
            semaphore.acquire();

            // 成功获取许可证后，执行打印操作
            // 模拟打印耗时，随机生成0-10秒的打印时间
            long duration = (long) (Math.random() * 10);
            System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",
                    Thread.currentThread().getName(), duration);

            // 使用Thread.sleep模拟打印耗时
            Thread.sleep(duration);
            // 使用TimeUnit.SECONDS.sleep也是休眠指定秒数，效果与上面相同
            // （这里似乎有重复，两种方式都在用）
            TimeUnit.SECONDS.sleep(duration);

        } catch (InterruptedException e) {
            // 如果线程被中断，打印异常信息
            e.printStackTrace();
        } finally {
            // 关键：在finally块中释放信号量
            // 确保无论是否发生异常，信号量都会被释放
            // release()方法释放一个许可证，让其他等待的线程可以获取
            // JVM会选择其中一个等待线程获取信号量并继续执行
            semaphore.release();
        }
    }
}
