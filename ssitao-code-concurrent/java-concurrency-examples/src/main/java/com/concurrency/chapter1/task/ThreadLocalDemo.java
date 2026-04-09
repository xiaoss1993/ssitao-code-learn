package com.concurrency.chapter1.task;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ThreadLocal 线程局部变量示例
 */
public class ThreadLocalDemo {

    // ThreadLocal 保证每个线程都有自己的独立副本
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    private static final ThreadLocal<Integer> THREAD_COUNTER =
            ThreadLocal.withInitial(() -> 0);

    public static void threadLocalDemo() {
        // 主线程
        System.out.println("主线程日期格式: " + DATE_FORMAT.get().format(new Date()));
        System.out.println("主线程计数器: " + THREAD_COUNTER.get());

        // 线程1
        Thread thread1 = new Thread(() -> {
            DATE_FORMAT.set(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
            for (int i = 0; i < 3; i++) {
                THREAD_COUNTER.set(THREAD_COUNTER.get() + 1);
                System.out.println("线程[" + Thread.currentThread().getName() + "] - " +
                        "计数器: " + THREAD_COUNTER.get() + " - " +
                        "日期: " + DATE_FORMAT.get().format(new Date()));
                sleep(100);
            }
        }, "Thread-1");

        // 线程2
        Thread thread2 = new Thread(() -> {
            // 使用默认格式
            for (int i = 0; i < 3; i++) {
                THREAD_COUNTER.set(THREAD_COUNTER.get() + 10);
                System.out.println("线程[" + Thread.currentThread().getName() + "] - " +
                        "计数器: " + THREAD_COUNTER.get() + " - " +
                        "日期: " + DATE_FORMAT.get().format(new Date()));
                sleep(100);
            }
        }, "Thread-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 主线程的值不受其他线程影响
        System.out.println("主线程最终计数器: " + THREAD_COUNTER.get());

        // 清理ThreadLocal
        DATE_FORMAT.remove();
        THREAD_COUNTER.remove();
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
