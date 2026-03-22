package com.ssitao.code.thread.code25.core;


import com.ssitao.code.thread.code25.task.FileSearch;

import java.util.concurrent.Phaser;

/**
 * 代码25: Phaser多阶段同步示例
 *
 * 本示例演示了Java并发包中Phaser的使用方法。
 * Phaser是一种可复用的同步屏障，类似于CyclicBarrier和CountDownLatch的结合体，
 * 但支持多个阶段(phase)的同步，适用于分阶段执行的并发任务。
 *
 * 场景说明：
 * - 三个线程分别在不同的目录中搜索文件
 * - 搜索过程分为三个阶段：
 *   1. 查找所有符合条件的文件
 *   2. 过滤24小时内修改过的文件
 *   3. 打印搜索结果
 * - 每个阶段结束后，所有线程需要等待其他线程完成，才能进入下一个阶段
 */
public class Main {
    public static void main(String[] args) {
        // 创建Phaser同步器，初始注册参与者数量为3
        // 这意味着Phaser会等待3个线程都到达每个阶段的同步点后，才放行所有线程继续执行
        Phaser phaser = new Phaser(3);

        // 创建三个文件搜索任务，分别在不同的目录中搜索.log文件
        // 每个任务都关联到同一个Phaser，以便在各阶段进行同步
        FileSearch system = new FileSearch("/Users/ssitao", "log", phaser);
        FileSearch apps = new FileSearch("/Users/ssitao", "log", phaser);
        FileSearch documents = new FileSearch("/Users/ssitao", "log", phaser);

        // 创建并启动三个搜索线程
        // System线程：搜索系统相关目录
        Thread systemThread = new Thread(system, "System");
        systemThread.start();

        // Apps线程：搜索应用程序相关目录
        Thread appsThread = new Thread(apps, "Apps");
        appsThread.start();

        // Documents线程：搜索文档相关目录
        Thread documentsThread = new Thread(documents, "Documents");
        documentsThread.start();

        // 主线程等待所有搜索线程执行完毕
        // join()方法会阻塞主线程，直到对应的线程执行完毕
        try {
            systemThread.join();
            appsThread.join();
            documentsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 检查Phaser是否已终止（所有阶段都已完成）
        // 当所有注册的参与者都调用arriveAndDeregister()注销后，Phaser进入终止状态
        System.out.printf("Terminated: %s\n", phaser.isTerminated());
    }
}
