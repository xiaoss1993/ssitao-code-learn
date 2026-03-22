package com.ssitao.code.thread.code23.core;


import com.ssitao.code.thread.code23.task.Participant;
import com.ssitao.code.thread.code23.task.VideoConference;

/**
 * 主程序入口类
 * 演示使用 CountDownLatch（倒计时闩） 实现线程同步
 *
 * CountDownLatch 核心概念：
 * - 一种同步辅助类，允许一个或多个线程等待其他线程完成操作
 * - 初始化时设置一个计数（count）
 * - 每当一个线程完成操作，调用 countDown() 将计数减1
 * - 当计数变为0时，等待的线程被唤醒继续执行
 *
 * 本例场景：
 * - 模拟视频会议系统，所有参与者都到达后才能开始会议
 * - VideoConference 等待所有 Participant 都调用 arrive() 后才正式开始
 */
public class Main {
    public static void main(String[] args) {
        // ========== 步骤1：创建视频会议对象 ==========
        // 创建一个有10个参与者的视频会议
        // 内部会创建一个初始计数为10的 CountDownLatch
        VideoConference conference = new VideoConference(10);

        // ========== 步骤2：启动会议主持线程 ==========
        // VideoConference 是一个Runnable，它会等待所有参与者到达
        Thread threadConference = new Thread(conference);
        threadConference.start();

        // ========== 步骤3：创建并启动参与者线程 ==========
        // 创建10个参与者，每个参与者运行在一个独立的线程中
        // 每个参与者会等待一个随机时间（0-10秒）后"到达"会议
        for (int i = 0; i < 10; i++) {
            Participant p = new Participant(conference, "Participant " + i);
            Thread t = new Thread(p);
            t.start();
        }

        // 注意：主线程创建完所有参与者后就会结束
        // 实际的会议控制由 threadConference 和各个 Participant 线程完成
    }
}
