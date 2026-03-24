package com.ssitao.code.thread.code53.task;

import java.util.concurrent.ThreadFactory;

/**
 * 自定义线程工厂 - 实现 ThreadFactory 接口
 *
 * 核心设计：
 * 1. 实现 ThreadFactory 接口，自定义线程创建逻辑
 * 2. 自动为每个线程分配递增的唯一名称
 * 3. 使用自定义 MyThread 类，添加额外的监控功能
 *
 * ThreadFactory 的作用：
 * - 统一管理线程的创建过程
 * - 为线程自动设置有意义的名称（便于调试和日志分析）
 * - 可以在这里设置线程属性（优先级、守护线程等）
 * - 收集线程创建统计数据
 *
 * 使用场景：
 * - 在线程池中使用，统一管理线程池中的线程
 * - 需要追踪线程创建顺序和数量
 * - 需要为线程设置特定的属性
 *
 * 本例中的线程命名规则：
 * - 格式：prefix + "-" + counter
 * - 例如："MyThreadFactory-1", "MyThreadFactory-2", ...
 */
public class MyThreadFactory implements ThreadFactory {

    /**
     * 线程计数器，用于生成唯一的线程名称
     */
    private int counter;

    /**
     * 线程名称前缀
     */
    private String prefix;

    /**
     * 构造函数，初始化线程名称前缀和计数器
     *
     * @param prefix 线程名称前缀
     */
    public MyThreadFactory(String prefix) {
        this.prefix = prefix;
        counter = 1;
    }

    /**
     * 创建新线程
     *
     * 每次调用都会创建一个新的 MyThread 实例
     * 线程名称格式：prefix + "-" + counter
     *
     * @param r 要执行的任务（Runnable）
     * @return 配置好的 Thread 对象
     */
    @Override
    public Thread newThread(Runnable r) {
        // 创建自定义线程，使用前缀和计数器生成唯一名称
        MyThread myThread = new MyThread(r, prefix + "-" + counter);
        // 计数器递增，为下一个线程准备
        counter++;
        return myThread;
    }
}
