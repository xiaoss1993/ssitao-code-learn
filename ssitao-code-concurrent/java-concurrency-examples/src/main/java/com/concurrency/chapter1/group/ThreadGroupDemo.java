package com.concurrency.chapter1.group;

import java.util.concurrent.TimeUnit;

/**
 * 线程分组示例
 */
public class ThreadGroupDemo {

    public static void groupDemo() {
        // 创建两个线程组
        ThreadGroup userGroup = new ThreadGroup("UserGroup");
        ThreadGroup daemonGroup = new ThreadGroup("DaemonGroup");

        // 设置守护线程组
        daemonGroup.setDaemon(true);

        // 在用户组中创建线程
        Thread t1 = new Thread(userGroup, () -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("用户线程 [" + Thread.currentThread().getName() + "] 执行中");
                sleep(200);
            }
        }, "User-1");

        Thread t2 = new Thread(userGroup, () -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("用户线程 [" + Thread.currentThread().getName() + "] 执行中");
                sleep(200);
            }
        }, "User-2");

        // 在守护组中创建线程
        Thread t3 = new Thread(daemonGroup, () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("守护线程 [" + Thread.currentThread().getName() + "] 执行中");
                sleep(200);
            }
        }, "Daemon-1");

        System.out.println("用户组活跃线程数: " + userGroup.activeCount());
        System.out.println("守护组活跃线程数: " + daemonGroup.activeCount());

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("用户组活跃线程数(结束后): " + userGroup.activeCount());
        System.out.println("守护组活跃线程数(结束后): " + daemonGroup.activeCount());
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
