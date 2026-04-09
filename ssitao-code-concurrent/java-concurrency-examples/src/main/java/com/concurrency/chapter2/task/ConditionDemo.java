package com.concurrency.chapter2.task;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition条件变量示例 - 实现多路等待
 */
public class ConditionDemo {

    private static final Lock lock = new ReentrantLock();
    private static final Condition conditionA = lock.newCondition();
    private static final Condition conditionB = lock.newCondition();

    private static String currentThread = "NONE";

    public static void demo() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("线程A 等待条件...");
                while (!currentThread.equals("A")) {
                    conditionA.await(); // 等待
                }
                System.out.println("线程A 执行任务!");
                currentThread = "NONE";
                conditionB.signal(); // 唤醒线程B
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "Thread-A");

        Thread t2 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("线程B 等待条件...");
                while (!currentThread.equals("B")) {
                    conditionB.await(); // 等待
                }
                System.out.println("线程B 执行任务!");
                currentThread = "NONE";
                conditionA.signal(); // 唤醒线程A
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "Thread-B");

        t1.start();
        t2.start();

        Thread.sleep(500);

        // 主线程设置条件并唤醒
        lock.lock();
        try {
            currentThread = "A";
            conditionA.signal(); // 唤醒线程A
        } finally {
            lock.unlock();
        }

        t1.join();
        t2.join();

        System.out.println("Condition示例完成");
    }
}
