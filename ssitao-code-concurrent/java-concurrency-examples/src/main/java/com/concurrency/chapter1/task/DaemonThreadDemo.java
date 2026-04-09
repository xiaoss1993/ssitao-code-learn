package com.concurrency.chapter1.task;

/**
 * 守护线程示例
 */
public class DaemonThreadDemo {

    public static void daemonDemo() {
        System.out.println("主线程开始");

        // 创建一个用户线程
        Thread userThread = new Thread(() -> {
            System.out.println("用户线程开始");
            for (int i = 0; i < 5; i++) {
                System.out.println("  用户线程执行 " + i);
                sleep(200);
            }
            System.out.println("用户线程结束");
        }, "User-Thread");

        // 创建一个守护线程
        Thread daemonThread = new Thread(() -> {
            System.out.println("守护线程开始");
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                if (i % 100 == 0) {
                    System.out.println("  守护线程执行 " + i);
                }
                sleep(100);
            }
            System.out.println("守护线程结束"); // 这行可能不会打印
        }, "Daemon-Thread");

        daemonThread.setDaemon(true);

        userThread.start();
        daemonThread.start();

        try {
            userThread.join();
            // 主线程结束，守护线程也会随之结束
            System.out.println("主线程结束，JVM会终止所有守护线程");
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
