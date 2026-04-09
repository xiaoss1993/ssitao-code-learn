package com.concurrency.chapter1.task;

/**
 * 线程信息获取和设置示例
 */
public class ThreadInfoDemo {

    public static void infoDemo() {
        Thread mainThread = Thread.currentThread();

        System.out.println("主线程信息：");
        System.out.println("  线程名称: " + mainThread.getName());
        System.out.println("  线程ID: " + mainThread.getId());
        System.out.println("  线程优先级: " + mainThread.getPriority());
        System.out.println("  线程状态: " + mainThread.getState());
        System.out.println("  线程组: " + mainThread.getThreadGroup().getName());
        System.out.println("  是否守护线程: " + mainThread.isDaemon());

        // 创建并启动一个自定义线程
        Thread workerThread = new Thread(() -> {
            Thread t = Thread.currentThread();
            System.out.println("\n工作线程信息：");
            System.out.println("  线程名称: " + t.getName());
            System.out.println("  线程ID: " + t.getId());
            System.out.println("  线程优先级: " + t.getPriority());
        }, "Worker-Thread-1");

        workerThread.setPriority(Thread.MIN_PRIORITY);
        workerThread.start();

        try {
            workerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
