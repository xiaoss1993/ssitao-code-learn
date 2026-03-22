package com.ssitao.code.thread.code26.core;


import com.ssitao.code.thread.code26.task.MyPhaser;
import com.ssitao.code.thread.code26.task.Student;

/**
 * 代码26: Phaser动态注册与阶段切换演示
 *
 * 本示例演示了Phaser的以下特性：
 * 1. 动态注册：通过phaser.register()动态增加参与者数量
 * 2. onAdvance回调：自定义阶段切换时的行为
 * 3. 多阶段同步：5个学生线程同步完成3道试题
 *
 * 场景说明：
 * - 5个学生参加考试，需要完成3道试题
 * - 每道试题完成后，所有学生必须等待其他同学完成才能进入下一题
 * - MyPhaser在每个阶段切换时打印提示信息
 */
public class Main {
    public static void main(String[] args) {
        // 创建自定义的Phaser子类，用于在阶段切换时打印提示信息
        MyPhaser phaser = new MyPhaser();

        // 创建5个学生对象，并将它们注册到phaser中
        // 注意：register()只是增加一个未到达的参与者计数，不会阻塞
        Student[] students = new Student[5];
        for (int i = 0; i < students.length; i++) {
            students[i] = new Student(phaser);
            phaser.register(); // 动态注册一个新参与者
        }

        // 创建并启动5个学生线程
        Thread[] threads = new Thread[5];
        for (int i = 0; i < students.length; i++) {
            threads[i] = new Thread(students[i]);
            threads[i].start();
        }

        // 等待所有学生线程执行完毕
        // join()会阻塞主线程，直到对应线程完成执行
        try {
            for (int i = 0; i < threads.length; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 检查Phaser是否已进入终止状态
        // 当onAdvance()返回true时，Phaser终止
        System.out.printf("Main: The phaser has finished: %s.\n", phaser.isTerminated());
    }
}