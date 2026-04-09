package com.concurrency.chapter1.task;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 线程休眠示例
 */
public class ThreadSleepDemo {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static void sleepDemo() {
        System.out.println("当前时间: " + LocalTime.now().format(formatter));

        Thread sleeper = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                System.out.println("第" + i + "次休眠 - 开始: " + LocalTime.now().format(formatter));
                try {
                    Thread.sleep(1000); // 休眠1秒
                } catch (InterruptedException e) {
                    System.out.println("休眠被中断!");
                    return;
                }
                System.out.println("第" + i + "次休眠 - 结束: " + LocalTime.now().format(formatter));
            }
        }, "Sleeper");

        sleeper.start();

        try {
            sleeper.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
