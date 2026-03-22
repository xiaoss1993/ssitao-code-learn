package com.ssitao.code.thread.code23.task;


import java.util.concurrent.CountDownLatch;

/**
 * 视频会议类
 * 使用 CountDownLatch（倒计时闩）来控制所有参与者都到达后才开始会议
 *
 * CountDownLatch 工作原理：
 * - 初始化时设置一个计数初始值
 * - 其他线程调用 countDown() 将计数减1
 * - 调用 await() 的线程会阻塞，直到计数变为0
 * - 计数变为0后，所有等待的线程被唤醒
 *
 * 本类的作用：
 * - 作为会议主持人，等待所有参与者到达
 * - 当所有参与者都到达后，打印开始会议的消息
 */
public class VideoConference implements Runnable {
    /**
     * 倒计时闩控制器
     * 初始计数设置为参与者总数
     * 每当一个参与者到达，就调用 countDown() 减1
     */
    private final CountDownLatch controller;

    /**
     * 构造函数，初始化倒计时闩
     *
     * @param number 参与者人数，用于初始化 CountDownLatch 的计数
     */
    public VideoConference(int number) {
        controller = new CountDownLatch(number);
    }

    /**
     * 参与者到达时调用此方法
     *
     * 当参与者到达会议时：
     * 1. 打印参与者到达的消息
     * 2. 调用 countDown() 将计数器减1（表示又少一个参与者需要等待）
     * 3. 打印当前还剩余多少参与者未到达
     *
     * @param name 参与者名称
     */
    public void arrive(String name) {
        // 打印参与者到达的消息
        System.out.printf("%s has arrived.\n", name);

        // 将计数器减1，表示这个参与者已经到达
        controller.countDown();

        // 打印当前还剩余多少参与者未到达
        System.out.printf("VideoConference: Waiting for %d participants.\n", controller.getCount());
    }

    /**
     * 核心方法：等待所有参与者到达，然后开始会议
     *
     * 这是 Runnable 的 run() 方法，由专门的线程执行。
     * 该线程会一直等待，直到 CountDownLatch 的计数变为0。
     */
    @Override
    public void run() {
        // 打印初始化的参与者数量
        System.out.printf("VideoConference: Initialization: %d participants.\n", controller.getCount());

        try {
            // ========== 等待所有参与者到达 ==========
            // await() 方法会阻塞当前线程
            // 直到 CountDownLatch 的计数变为0（即所有参与者都调用了 countDown()）
            controller.await();

            // ========== 所有参与者已到达，开始会议 ==========
            System.out.printf("VideoConference: All the participants have come\n");
            System.out.printf("VideoConference: Let's start...\n");

        } catch (InterruptedException e) {
            // 如果线程被中断，打印异常信息
            e.printStackTrace();
        }
    }
}
