package com.concurrency.chapter1.task;

/**
 * 等待线程终止 (join) 示例
 */
public class ThreadJoinDemo {

    public static void joinDemo() {
        Thread task1 = new Thread(() -> {
            System.out.println("任务1开始");
            sleep(500);
            System.out.println("任务1完成");
        }, "Task-1");

        Thread task2 = new Thread(() -> {
            System.out.println("任务2开始");
            sleep(300);
            System.out.println("任务2完成");
        }, "Task-2");

        Thread task3 = new Thread(() -> {
            System.out.println("任务3开始");
            sleep(200);
            System.out.println("任务3完成");
        }, "Task-3");

        System.out.println("启动所有任务...");
        task1.start();
        task2.start();
        task3.start();

        try {
            // 等待所有任务完成
            System.out.println("主线程等待所有任务完成...");
            task1.join();
            task2.join();
            task3.join();
            System.out.println("所有任务已完成!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
