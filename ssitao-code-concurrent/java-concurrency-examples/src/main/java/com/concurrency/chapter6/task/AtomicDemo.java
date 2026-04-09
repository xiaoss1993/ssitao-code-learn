package com.concurrency.chapter6.task;

import java.util.concurrent.atomic.*;

/**
 * 原子变量示例
 */
public class AtomicDemo {

    public static void demo() throws InterruptedException {
        // AtomicInteger示例
        AtomicInteger atomicInt = new AtomicInteger(0);

        // AtomicLong示例
        AtomicLong atomicLong = new AtomicLong(0);

        // AtomicReference示例
        AtomicReference<String> atomicRef = new AtomicReference<>("初始值");

        // AtomicIntegerArray示例
        AtomicIntegerArray atomicArray = new AtomicIntegerArray(5);

        System.out.println("=== 原子变量操作演示 ===\n");

        // 启动多个线程同时修改原子变量
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    atomicInt.incrementAndGet();       // 自增
                    atomicLong.addAndGet(2);           // 加2
                    atomicArray.getAndIncrement(0);    // 数组第一个元素自增
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) t.join();

        System.out.println("AtomicInteger最终值: " + atomicInt.get() + " (期望: 10000)");
        System.out.println("AtomicLong最终值: " + atomicLong.get() + " (期望: 20000)");
        System.out.println("AtomicIntegerArray[0]: " + atomicArray.get(0) + " (期望: 10000)");

        // AtomicReference CAS操作
        System.out.println("\n=== AtomicReference CAS操作 ===");
        String oldValue = atomicRef.get();
        boolean success = atomicRef.compareAndSet(oldValue, "新值");
        System.out.println("CAS操作成功: " + success);
        System.out.println("当前值: " + atomicRef.get());
    }
}
