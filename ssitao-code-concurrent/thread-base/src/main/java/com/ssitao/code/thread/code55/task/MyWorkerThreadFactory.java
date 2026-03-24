package com.ssitao.code.thread.code55.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * 自定义 Fork/Join 工作线程工厂
 *
 * 核心设计：
 * 1. 实现 ForkJoinWorkerThreadFactory 接口
 * 2. 创建自定义的 MyWorkerThread 实例
 * 3. 用于 ForkJoinPool 创建工作线程
 *
 * ForkJoinWorkerThreadFactory 接口：
 * - 只有一个方法 newThread(ForkJoinPool pool)
 * - 返回 ForkJoinWorkerThread 实例
 *
 * 使用场景：
 * - 在创建 ForkJoinPool 时指定自定义工作线程类型
 * - 用于监控、统计工作线程行为
 * - 自定义工作线程的初始化和清理逻辑
 *
 * 使用示例：
 * ForkJoinPool pool = new ForkJoinPool(
 *     4,                      // 并行级别
 *     new MyWorkerThreadFactory(), // 自定义工厂
 *     null,                   // 异常处理器
 *     false                   // 异步模式
 * );
 */
public class MyWorkerThreadFactory implements ForkJoinWorkerThreadFactory {


    /**
     * 创建新的工作线程
     *
     * @param pool 工作线程所属的 ForkJoinPool
     * @return 新创建的 MyWorkerThread 实例
     */
    @Override
    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        // 创建自定义工作线程
        return new MyWorkerThread(pool);
    }

}
