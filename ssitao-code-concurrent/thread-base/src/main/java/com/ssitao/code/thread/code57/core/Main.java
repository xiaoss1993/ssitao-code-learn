package com.ssitao.code.thread.code57.core;


import com.ssitao.code.thread.code57.task.MyLock;
import com.ssitao.code.thread.code57.task.Task;

import java.util.concurrent.TimeUnit;

/**
 * 主程序 - AbstractQueuedSynchronizer 自定义锁示例
 *
 * 演示使用 AQS 实现自定义互斥锁
 *
 * 核心概念：
 * 1. AbstractQueuedSynchronizer（AQS）- Java 并发基础框架
 *    - Java 5 引入的同步器框架
 *    - 是 ReentrantLock、CountDownLatch、Semaphore 等的基础
 *    - 基于 CLH 队列（Craig, Landin, Hagersten 锁队列）
 *
 * 2. AQS 核心方法：
 *    - tryAcquire(): 尝试获取同步状态（被子类实现）
 *    - tryRelease(): 尝试释放同步状态（被子类实现）
 *    - acquire(): 获取同步状态（框架提供，可重写）
 *    - release(): 释放同步状态（框架提供，可重写）
 *
 * 3. AQS 两种模式：
 *    - 独占模式（Exclusive）：如 ReentrantLock，一次只能一个线程获取锁
 *    - 共享模式（Shared）：如 Semaphore、CountDownLatch，多个线程可同时获取
 *
 * 4. 同步状态（state）：
 *    - AQS 使用 volatile int state 表示同步状态
 *    - 子类可以自定义 state 的含义
 *    - 本例中：state=0 表示未锁，state=1 表示已锁定
 *
 * 执行流程：
 * 1. 创建自定义锁 MyLock
 * 2. 启动10个任务线程，每个都会尝试获取锁
 * 3. 主线程循环尝试获取锁（每1秒尝试一次）
 * 4. 获取锁后打印信息，然后释放锁
 *
 * 预期输出：
 * - 10个任务线程会依次获取锁（互斥）
 * - 每个任务持有锁2秒
 * - 主线程最终会获取到锁
 */
public class Main {

    public static void main(String[] args) {

        // 创建自定义锁
        MyLock lock = new MyLock();

        // 启动10个任务线程
        for (int i = 0; i < 10; i++) {
            // 创建任务，传入锁
            Task task = new Task("Task-" + i, lock);
            Thread thread = new Thread(task);
            thread.start();
        }

        // 主线程尝试获取锁
        // 使用 tryLock(timeout) 尝试获取，如果失败会等待
        boolean value;
        do {
            try {
                // 尝试获取锁，等待1秒超时
                value = lock.tryLock(1, TimeUnit.SECONDS);
                if (!value) {
                    // 获取失败，打印信息
                    System.out.printf("Main: Trying to get the Lock\n");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                value = false;
            }
        } while (!value);

        // 获取到锁
        System.out.printf("Main: Got the lock\n");

        // 释放锁
        lock.unlock();

        System.out.printf("Main: End of the program\n");
    }

}
