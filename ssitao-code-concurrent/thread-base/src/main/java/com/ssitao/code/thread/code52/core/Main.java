package com.ssitao.code.thread.code52.core;

import com.ssitao.code.thread.code52.task.MyTask;
import com.ssitao.code.thread.code52.task.MyThreadFactory;

/**
 * 主程序 - ThreadFactory 线程工厂模式示例
 *
 * 演示使用 ThreadFactory 自定义线程创建过程
 *
 * 核心概念：
 * 1. ThreadFactory - 线程工厂接口
 *    - 统一管理线程的创建过程
 *    - 可以自定义线程的创建逻辑
 *    - 常用于线程池中创建线程
 *
 * 2. ThreadFactory 接口：
 *    - 只有一个方法：newThread(Runnable r)
 *    - 返回一个 Thread 对象
 *    - 可以在这里设置线程属性（名称、优先级、守护线程等）
 *
 * 3. 自定义 ThreadFactory 的优势：
 *    - 统一设置线程名称前缀（便于调试）
 *    - 设置线程优先级
 *    - 设置线程为守护线程
 *    - 收集线程创建统计信息
 *    - 记录线程创建日志
 *
 * 执行流程：
 * 1. 创建自定义线程工厂 MyThreadFactory
 * 2. 创建任务 MyTask
 * 3. 通过工厂创建线程（自动带有自定义属性）
 * 4. 启动线程并等待完成
 * 5. 打印线程信息（包含创建时间、执行时长等）
 */
public class Main {
    public static void main(String[] args) throws Exception {

        // 创建自定义线程工厂，指定线程名称前缀
        MyThreadFactory myFactory = new MyThreadFactory("MyThreadFactory");

        // 创建任务
        MyTask task = new MyTask();

        // 通过工厂创建线程
        // 线程会自动获得名称 "MyThreadFactory-1"
        // 工厂会记录线程创建信息
        Thread thread = myFactory.newThread(task);

        // 启动线程
        thread.start();

        // 等待线程执行完成
        thread.join();

        // 打印线程信息
        // 输出包括：线程名称、创建日期、执行时长
        System.out.printf("Main: Thread information.\n");
        System.out.printf("%s\n", thread);
        System.out.printf("Main: End of the example.\n");
    }
}
