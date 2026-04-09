package com.concurrency.chapter2.task;

/**
 * synchronized同步块示例 - 解决死锁问题
 */
public class SynchronizedBlockDemo {

    public static void demo() {
        final Object lock1 = new Object();
        final Object lock2 = new Object();

        System.out.println("演示synchronized同步块的使用...");

        // 线程1: 先获取lock1, 再获取lock2
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + " 获取了 lock1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 等待获取 lock2");
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + " 获取了 lock2");
                }
            }
        }, "Thread-1");

        // 线程2: 先获取lock2, 再获取lock1
        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " 获取了 lock2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 等待获取 lock1");
                synchronized (lock1) {
                    System.out.println(Thread.currentThread().getName() + " 获取了 lock1");
                }
            }
        }, "Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("同步块示例完成");
    }
}
