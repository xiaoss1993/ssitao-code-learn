package com.ssitao.code.thread.code38.core;

import com.ssitao.code.thread.code38.task.RejectedTaskController;
import com.ssitao.code.thread.code38.task.Task;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ThreadPoolExecutor 拒绝执行处理器示例
 *
 * 本示例演示当线程池关闭后，再向其提交任务时，
 * RejectedExecutionHandler 如何处理被拒绝的任务。
 *
 * 核心概念：
 * 1. ThreadPoolExecutor - Java线程池核心类，用于管理线程执行
 * 2. RejectedExecutionHandler - 拒绝执行处理器接口，当任务无法被接受时调用
 * 3. shutdown() - 优雅关闭线程池，不再接受新任务，但会执行已提交的任务
 */
public class Main {
    public static void main(String[] args) {
        // 创建拒绝任务控制器对象
        // 该控制器实现了 RejectedExecutionHandler 接口，用于处理被拒绝的任务
        RejectedTaskController controller = new RejectedTaskController();

        // 创建缓存线程池执行器
        // newCachedThreadPool() 创建一个可缓存的线程池，线程数量按需增长
        // 强转为 ThreadPoolExecutor 以便访问其特有方法（如 setRejectedExecutionHandler）
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        // 为执行器设置自定义的拒绝执行处理器
        // 当线程池无法接受新任务时，会调用 controller 的 rejectedExecution 方法
        executor.setRejectedExecutionHandler(controller);

        // 循环提交三个任务到线程池
        System.out.printf("Main: Starting.\n");
        for (int i = 0; i < 3; i++) {
            Task task = new Task("Task" + i);
            executor.submit(task);  // 提交任务到线程池执行
        }

        // 优雅关闭线程池
        // shutdown() 后：
        // 1. 线程池不再接受新任务（submit会触发拒绝）
        // 2. 已提交的任务仍会继续执行完成
        System.out.printf("Main: Shuting down the Executor.\n");
        executor.shutdown();

        // 线程池关闭后，尝试再提交一个任务
        // 这个任务会被拒绝执行，因为线程池已经关闭
        System.out.printf("Main: Sending another Task.\n");
        Task task = new Task("RejectedTask");
        executor.submit(task);  // 触发 RejectedTaskController.rejectedExecution()

        // 程序结束
        System.out.printf("Main: End.\n");
    }
}
