package com.concurrency.chapter1.task;

/**
 * 线程中断示例
 */
public class ThreadInterruptDemo {

    public static void interruptDemo() {
        Thread longTask = new Thread(() -> {
            System.out.println("长任务开始执行...");
            try {
                // 模拟长时间运行的任务
                for (int i = 0; i < 10; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("任务被中断!");
                        return;
                    }
                    System.out.println("  执行中... " + (i + 1));
                    Thread.sleep(500);
                }
                System.out.println("长任务执行完成");
            } catch (InterruptedException e) {
                System.out.println("线程睡眠时被中断");
            }
        }, "LongTask");

        longTask.start();

        // 主线程休眠1秒后中断长任务
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("主线程中断长任务...");
        longTask.interrupt();

        try {
            longTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
