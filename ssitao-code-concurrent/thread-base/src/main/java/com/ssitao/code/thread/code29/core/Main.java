package com.ssitao.code.thread.code29.core;


import com.ssitao.code.thread.code29.task.Server;
import com.ssitao.code.thread.code29.task.Task;

/**
 * 主程序入口 - 演示固定大小线程池的使用
 *
 * 本程序展示如何使用FixedThreadPool（固定大小线程池）来并发执行多个任务：
 *
 * 与CachedThreadPool的区别：
 * - CachedThreadPool: 线程数量无限制（0到Integer.MAX_VALUE），适合短时异步任务
 * - FixedThreadPool: 线程数量固定为5个，适合长期运行的任务，保持系统资源稳定
 *
 * 程序流程：
 * 1. 创建服务器（内含FixedThreadPool，固定5个工作线程）
 * 2. 向服务器提交100个任务
 * 3. 关闭服务器
 */
public class Main {

    /**
     * 程序入口方法
     *
     * @param args 命令行参数，本程序不使用
     */
    public static void main(String[] args) {
        // ========== 步骤1: 创建服务器实例 ==========
        // Server内部创建FixedThreadPool(5)：
        // - 始终保持5个工作线程
        // - 即使任务执行完毕，线程也不会被回收
        // - 如果5个线程都在忙碌，新任务会在队列中等待
        Server server = new Server();

        // ========== 步骤2: 循环提交100个任务 ==========
        //
        // 重要说明：
        // - 任务是快速连续提交的（几乎同时）
        // - 线程池只有5个线程，但有100个任务
        // - 同一时刻最多只有5个任务在执行
        // - 其余95个任务在阻塞队列中排队等待
        // - executeTask()方法立即返回，不等待任务完成
        //
        for (int i = 0; i < 100; i++) {
            // 创建新任务，名称为"Task 0"到"Task 99"
            Task task = new Task("Task " + i);

            // 提交任务到服务器的线程池
            // 任务会被异步执行，main线程立即继续下一次循环
            server.executeTask(task);
        }

        // ========== 步骤3: 关闭服务器 ==========
        // shutdown()行为：
        // - 不再接受新任务
        // - 已提交到队列的95个任务会继续执行
        // - 5个正在执行的任务会继续直到完成
        // - 所有100个任务最终都会完成
        server.endServer();

        // ========== 线程池执行示意 ==========
        //
        // 时间轴示意：
        // Task0 ──────▶ (线程1 执行)
        // Task1 ──────▶ (线程2 执行)
        // Task2 ──────▶ (线程3 执行)
        // Task3 ──────▶ (线程4 执行)
        // Task4 ──────▶ (线程5 执行)
        // Task5 ──等待▶ (队列中等待)
        // Task6 ──等待▶ (队列中等待)
        // ...         (队列中等待)
        // Task99 ─等待▶ (队列中等待)
        //
        // 当某个线程完成任务后：
        // 线程1: Task0完成 ──▶ Task5开始执行
        // 线程2: Task1完成 ──▶ Task6开始执行
        // ...
    }
}
