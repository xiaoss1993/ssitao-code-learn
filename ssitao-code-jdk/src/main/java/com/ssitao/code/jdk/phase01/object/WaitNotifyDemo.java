package com.ssitao.code.jdk.phase01.object;

/**
 * 第一阶段步骤1: Object类 - 演示wait/notify线程通信
 *
 * wait/notify是Object类的方法,用于线程间通信
 * - wait(): 当前线程释放锁并进入等待状态
 * - notify(): 唤醒一个等待该对象锁的线程
 * - notifyAll(): 唤醒所有等待该对象锁的线程
 */
public class WaitNotifyDemo {
    private final Object lock = new Object();
    private boolean ready = false;

    /**
     * 等待线程 - 直到ready为true才继续执行
     */
    public void waitForReady() throws InterruptedException {
        synchronized (lock) {
            while (!ready) {
                System.out.println("线程 [" + Thread.currentThread().getName() + "] 等待中...");
                lock.wait();  // 释放锁并等待
            }
            System.out.println("线程 [" + Thread.currentThread().getName() + "] 继续执行!");
        }
    }

    /**
     * 唤醒线程 - 设置ready为true并唤醒所有等待线程
     */
    public void setReady() {
        synchronized (lock) {
            System.out.println("设置 ready = true");
            ready = true;
            lock.notifyAll();  // 唤醒所有等待的线程
        }
    }

    public static void main(String[] args) {
        System.out.println("=== wait/notify 线程通信演示 ===\n");

        WaitNotifyDemo demo = new WaitNotifyDemo();

        // 创建3个等待线程
        for (int i = 1; i <= 3; i++) {
            final int threadNum = i;
            new Thread(() -> {
                try {
                    Thread.sleep(100 * threadNum);  // 依次启动
                    demo.waitForReady();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "等待线程-" + threadNum).start();
        }

        // 主线程2秒后唤醒
        new Thread(() -> {
            try {
                Thread.sleep(500);
                System.out.println("\n--- 2秒后唤醒所有等待线程 ---\n");
                demo.setReady();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "唤醒线程").start();

        // 等待所有线程执行完毕
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n=== 所有线程执行完毕 ===");
    }
}
