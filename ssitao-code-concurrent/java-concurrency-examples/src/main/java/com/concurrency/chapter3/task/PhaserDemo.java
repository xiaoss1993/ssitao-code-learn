package com.concurrency.chapter3.task;

import java.util.concurrent.Phaser;

/**
 * Phaser阶段同步器示例 - 并发阶段任务的运行
 */
public class PhaserDemo {

    public static void demo() throws InterruptedException {
        Phaser phaser = new Phaser(3); // 3个参与线程

        System.out.println("阶段任务开始...\n");

        for (int i = 1; i <= 3; i++) {
            final int workerId = i;
            new Thread(() -> {
                try {
                    // 阶段1
                    System.out.println("工人" + workerId + " 完成阶段1");
                    phaser.arriveAndAwaitAdvance();

                    // 阶段2
                    System.out.println("工人" + workerId + " 完成阶段2");
                    phaser.arriveAndAwaitAdvance();

                    // 阶段3
                    System.out.println("工人" + workerId + " 完成阶段3");
                    phaser.arriveAndAwaitAdvance();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Worker-" + workerId).start();
        }

        Thread.sleep(3000);
    }
}
