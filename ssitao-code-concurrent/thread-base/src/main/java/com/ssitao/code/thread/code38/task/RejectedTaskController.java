package com.ssitao.code.thread.code38.task;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 拒绝任务处理器类
 *
 * 实现了 RejectedExecutionHandler 接口，该接口是 Java 并发包提供的
 * 用于处理线程池无法接受新任务时的回调机制。
 *
 * 触发场景：
 * 1. 线程池已调用 shutdown() 后再提交任务
 * 2. 线程池达到最大容量且队列已满
 * 3. 线程池已终止
 *
 * @see RejectedExecutionHandler
 * @see ThreadPoolExecutor
 */
public class RejectedTaskController implements RejectedExecutionHandler {

    /**
     * 当任务被拒绝执行时调用此方法
     *
     * @param r       被拒绝的可运行任务（Runnable）
     * @param executor 拒绝该任务的线程池执行器
     *
     * 这个方法会在以下情况被调用：
     * - executor.submit(task) 时线程池已关闭
     * - 线程池无法再接受更多任务
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // 输出被拒绝的任务信息
        System.out.printf("RejectedTaskController: The task %s has been rejected\n", r.toString());
        // 输出线程池的当前状态信息
        System.out.printf("RejectedTaskController: %s\n", executor.toString());
        // isTerminating() 返回 true 表示线程池正在关闭但尚未完全终止
        System.out.printf("RejectedTaskController: Terminating: %s\n", executor.isTerminating());
        // isTerminated() 返回 true 表示线程池已完全终止
        System.out.printf("RejectedTasksController: Terminated: %s\n", executor.isTerminated());
    }
}
