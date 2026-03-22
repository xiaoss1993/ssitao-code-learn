package com.ssitao.code.thread.code26.task;

import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 学生类 - 模拟考试场景
 *
 * 每个学生线程执行以下流程：
 * 1. 到达考场，等待所有学生到齐（phase 0同步）
 * 2. 做第1题，完成后等待所有人（phase 1同步）
 * 3. 做第2题，完成后等待所有人（phase 2同步）
 * 4. 做第3题，完成后等待所有人（phase 3同步后Phaser终止）
 *
 * 每个学生完成每道题的时间是随机的（0-10秒），
 * 但由于Phaser的同步作用，所有学生必须等到最慢的那个完成当前题后，
 * 才能一起进入下一题。
 */
public class Student implements Runnable {
    /**
     * Phaser同步器，用于控制学生线程在各阶段的同步
     */
    private Phaser phaser;

    /**
     * 构造函数
     *
     * @param phaser 阶段同步器，所有学生共享同一个Phaser实例
     */
    public Student(Phaser phaser) {
        this.phaser = phaser;
    }

    /**
     * 线程执行入口 - 模拟考试流程
     *
     * 执行流程说明：
     * - arriveAndAwaitAdvance()：到达同步点，阻塞等待所有学生都到达后一起进入下一阶段
     * - 每道题的完成时间是随机的，体现了Phaser的等待同步能力
     */
    @Override
    public void run() {
        System.out.printf("%s: Has arrived to do the exam. %s\n", Thread.currentThread().getName(), new Date());
        // 第一次同步：等待所有学生都到达考场
        phaser.arriveAndAwaitAdvance();

        // ========== 第一题 ==========
        System.out.printf("%s: Is going to do the first exercise. %s\n", Thread.currentThread().getName(), new Date());
        doExercise1();
        System.out.printf("%s: Has done the first exercise. %s\n", Thread.currentThread().getName(), new Date());
        // 第一题完成，到达同步点，等待其他学生
        phaser.arriveAndAwaitAdvance();

        // ========== 第二题 ==========
        System.out.printf("%s: Is going to do the second exercise. %s\n", Thread.currentThread().getName(), new Date());
        doExercise2();
        System.out.printf("%s: Has done the second exercise. %s\n", Thread.currentThread().getName(), new Date());
        // 第二题完成，到达同步点，等待其他学生
        phaser.arriveAndAwaitAdvance();

        // ========== 第三题 ==========
        System.out.printf("%s: Is going to do the third exercise. %s\n", Thread.currentThread().getName(), new Date());
        doExercise3();
        System.out.printf("%s: Has finished the exam. %s\n", Thread.currentThread().getName(), new Date());
        // 第三题完成，到达同步点（此时会触发onAdvance返回true，Phaser终止）
        phaser.arriveAndAwaitAdvance();
    }

    /**
     * 模拟做第1题
     *
     * 学生需要花费0-10秒的随机时间来完成题目
     */
    private void doExercise1() {
        try {
            Long duration = (long) (Math.random() * 10);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模拟做第2题
     */
    private void doExercise2() {
        try {
            Long duration = (long) (Math.random() * 10);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模拟做第3题
     */
    private void doExercise3() {
        try {
            Long duration = (long) (Math.random() * 10);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}