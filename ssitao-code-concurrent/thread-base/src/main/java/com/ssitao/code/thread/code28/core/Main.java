package com.ssitao.code.thread.code28.core;


import com.ssitao.code.thread.code28.task.Server;
import com.ssitao.code.thread.code28.task.Task;

/**
 * 主程序入口 - 演示线程池的使用
 *
 * 本程序展示了如何使用ThreadPoolExecutor线程池来并发执行多个任务：
 * 1. 创建服务器（内含线程池）
 * 2. 向服务器提交100个独立任务
 * 3. 关闭服务器
 *
 * 核心概念：
 * - 线程池复用：避免频繁创建销毁线程带来的开销
 * - 并发执行：多个任务同时执行，提高系统吞吐量
 * - 异步处理：任务提交后立即返回，不阻塞主线程
 */
public class Main {

    /**
     * 程序入口方法
     *
     * @param args 命令行参数，本程序不使用
     */
    public static void main(String[] args) {
        // ========== 步骤1: 创建服务器实例 ==========
        // Server内部会创建一个CachedThreadPool线程池
        // 此时线程池为空，没有活跃线程
        Server server = new Server();

        // ========== 步骤2: 循环提交100个任务 ==========
        //
        // 关键点：
        // - 任务是快速连续提交的（几乎同时）
        // - 每个任务执行时间随机（0-10秒）
        // - 线程池会根据负载动态调整线程数量
        // - submitTask()调用会立即返回，不等待任务完成
        //
        for (int i = 0; i < 100; i++) {
            // 创建新任务，任务名称为"Task 0", "Task 1", ... "Task 99"
            Task task = new Task("Task " + i);

            // 将任务提交给服务器（线程池）
            // 任务会被异步执行，main线程立即继续下一次循环
            server.executeTask(task);
        }

        // ========== 步骤3: 关闭服务器 ==========
        // 调用shutdown()后：
        // - 不再接受新任务
        // - 已提交的任务会继续执行完成
        // - 线程池中的工作线程会被回收
        server.endServer();

        // ========== 程序结束说明 ==========
        // main方法执行完毕后，进程可能不会立即退出
        // 因为线程池中的非守护线程可能还在执行任务
        // shutdown()会确保所有任务完成后程序可以正常退出
    }
}
