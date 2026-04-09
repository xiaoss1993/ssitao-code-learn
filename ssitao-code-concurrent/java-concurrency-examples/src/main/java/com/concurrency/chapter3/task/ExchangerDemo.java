package com.concurrency.chapter3.task;

import java.util.concurrent.Exchanger;

/**
 * Exchanger数据交换示例 - 并发任务间的数据交换
 */
public class ExchangerDemo {

    private static final Exchanger<String> exchanger = new Exchanger<>();

    public static void demo() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                String data = "数据包A";
                System.out.println("线程A 发送: " + data);
                String received = exchanger.exchange(data);
                System.out.println("线程A 收到: " + received);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Exchanger-Thread-A");

        Thread t2 = new Thread(() -> {
            try {
                String data = "数据包B";
                System.out.println("线程B 发送: " + data);
                String received = exchanger.exchange(data);
                System.out.println("线程B 收到: " + received);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Exchanger-Thread-B");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("数据交换完成!");
    }
}
