package com.ssitao.code.thread.code12.core;


import com.ssitao.code.thread.code12.factory.MyThreadFactory;
import com.ssitao.code.thread.code12.task.Task;

/**
 * 主程序入口
 * 演示 ThreadFactory 线程工厂的使用
 *
 * ThreadFactory 作用：
 * 1. 统一管理线程的创建逻辑
 * 2. 为线程设置统一的名称前缀
 * 3. 记录线程创建的历史统计
 */
public class Main {
    public static void main(String[] args) {
        // 第1步：创建一个自定义的线程工厂，指定线程名称前缀
        MyThreadFactory factory = new MyThreadFactory("MyThreadFactory");

        // 第2步：创建一个任务（所有线程共享执行同一个任务）
        Thread thread;
        Task task = new Task();

        // 第3步：通过线程工厂创建并启动10个线程
        System.out.printf("Starting the Threads\n");
        for (int i = 0; i < 10; i++) {
            // 使用工厂创建线程，线程会自动命名为：MyThreadFactory-Thread_0, Thread_1, ...
            thread = factory.newThread(task);
            thread.start();
        }

        // 第4步：打印线程工厂的统计信息
        // 包含所有创建的线程ID、名称、创建时间
        System.out.printf("Factory stats:\n");
        System.out.printf("%s\n", factory.getStats());
    }
}
