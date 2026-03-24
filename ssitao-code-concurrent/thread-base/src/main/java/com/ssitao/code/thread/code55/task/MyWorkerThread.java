package com.ssitao.code.thread.code55.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * 自定义工作线程 - 扩展 ForkJoinWorkerThread 添加监控功能
 *
 * 核心设计：
 * 1. 继承 ForkJoinWorkerThread，用于 ForkJoinPool 中的工作线程
 * 2. 使用 ThreadLocal 统计每个工作线程处理的任务数量
 * 3. 重写 onStart() 和 onTermination() 钩子方法，记录线程生命周期
 *
 * ForkJoinWorkerThread 钩子方法：
 * - onStart(): 线程启动时调用，可用于初始化
 * - onTermination(exception): 线程终止时调用，可用于统计
 *
 * ThreadLocal 的作用：
 * - 每个工作线程独立的计数器
 * - 线程间隔离，互不干扰
 * - 用于监控工作负载分布
 *
 * 使用场景：
 * - 监控 Fork/Join 任务分配是否均衡
 * - 分析工作窃取效果
 * - 性能调优
 */
public class MyWorkerThread extends ForkJoinWorkerThread {

    /**
     * 线程本地的任务计数器
     * 每个工作线程独立计数，互不干扰
     */
    private static ThreadLocal<Integer> taskCounter = new ThreadLocal<>();

    /**
     * 构造函数
     *
     * @param pool 工作线程所属的 ForkJoinPool
     */
    protected MyWorkerThread(ForkJoinPool pool) {
        super(pool);
    }

    /**
     * 线程启动时的钩子方法
     *
     * 在线程开始执行任务前调用
     * 用于初始化线程本地数据
     */
    @Override
    protected void onStart() {
        // 调用父类方法
        super.onStart();
        // 初始化任务计数器为0
        System.out.printf("MyWorkerThread %d: Initializing task counter.\n", getId());
        taskCounter.set(0);
    }

    /**
     * 线程终止时的钩子方法
     *
     * 在线程执行完所有任务后调用
     * 用于输出统计信息
     *
     * @param exception 如果线程因异常终止，则为该异常；否则为 null
     */
    @Override
    protected void onTermination(Throwable exception) {
        // 打印线程处理的任务数量
        System.out.printf("MyWorkerThread %d: %d tasks processed.\n", getId(), taskCounter.get());
        // 调用父类方法
        super.onTermination(exception);
    }

    /**
     * 添加任务计数
     *
     * 每当工作线程处理一个任务时调用
     * 用于追踪每个线程处理的任务数量
     */
    public void addTask() {
        // 获取当前计数并加1
        int counter = taskCounter.get().intValue();
        counter++;
        taskCounter.set(counter);
    }
}
