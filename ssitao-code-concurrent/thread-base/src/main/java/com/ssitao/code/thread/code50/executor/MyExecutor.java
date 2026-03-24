package com.ssitao.code.thread.code50.executor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * 自定义执行器 - 扩展 ThreadPoolExecutor 实现任务监控
 *
 * 核心设计：
 * 1. 继承 ThreadPoolExecutor，扩展监控功能
 * 2. 使用 ConcurrentHashMap 记录任务开始时间
 * 3. 重写生命周期方法（shutdown、shutdownNow）
 * 4. 重写钩子方法（beforeExecute、afterExecute）
 *
 * ThreadPoolExecutor 钩子方法：
 * - beforeExecute(): 任务执行前调用
 * - afterExecute(): 任务执行后调用（无论成功或异常）
 * - terminated(): 执行器终止后调用
 *
 * 使用 ConcurrentHashMap 的原因：
 * - 多个线程可能同时记录开始时间
 * - 需要线程安全的数据结构
 *
 * 注意：
 * - afterExecute 中的 get() 不会阻塞，因为任务已完成
 * - 但如果任务抛出未捕获的异常，afterExecute 仍会被调用
 */
public class MyExecutor extends ThreadPoolExecutor {

    /**
     * 存储任务开始时间的并发映射
     * key: 任务的 hashCode（字符串形式）
     * value: 任务开始执行的时间
     *
     * 使用 ConcurrentHashMap 保证线程安全
     */
    private ConcurrentHashMap<String, Date> startTimes;

    /**
     * 构造函数 - 创建自定义执行器
     *
     * @param corePoolSize    核心线程数
     * @param maximumPoolSize 最大线程数
     * @param keepAliveTime   空闲线程存活时间
     * @param unit            时间单位
     * @param workQueue       任务队列
     */
    public MyExecutor(int corePoolSize,
                      int maximumPoolSize,
                      long keepAliveTime,
                      TimeUnit unit,
                      BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        startTimes = new ConcurrentHashMap<>();
    }

    /**
     * 平缓关闭执行器
     *
     * 不再接受新任务，但会完成已提交的任务
     * 重写此方法以输出关闭时的统计信息
     */
    @Override
    public void shutdown() {
        // 输出关闭时的执行器状态
        System.out.printf("MyExecutor: Going to shutdown.\n");
        System.out.printf("MyExecutor: Executed tasks: %d\n", getCompletedTaskCount());
        System.out.printf("MyExecutor: Running tasks: %d\n", getActiveCount());
        System.out.printf("MyExecutor: Pending tasks: %d\n", getQueue().size());

        // 调用父类的平缓关闭
        super.shutdown();
    }

    /**
     * 立即关闭执行器
     *
     * 尝试取消所有正在执行的任务，不再启动未执行的任务
     * 返回尚未启动的任务列表
     */
    @Override
    public List<Runnable> shutdownNow() {
        // 输出关闭时的执行器状态
        System.out.printf("MyExecutor: Going to immediately shutdown.\n");
        System.out.printf("MyExecutor: Executed tasks: %d\n", getCompletedTaskCount());
        System.out.printf("MyExecutor: Running tasks: %d\n", getActiveCount());
        System.out.printf("MyExecutor: Pending tasks: %d\n", getQueue().size());

        // 调用父类的立即关闭，返回未执行的任务列表
        return super.shutdownNow();
    }

    /**
     * 任务执行前调用的钩子方法
     *
     * 在任务的 run() 或 call() 方法执行之前调用
     *
     * @param t 执行任务的线程
     * @param r 要执行的任务（Runnable）
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        // 记录任务开始时间
        String taskId = String.valueOf(r.hashCode());
        System.out.printf("MyExecutor: A task is beginning: %s : %s\n", t.getName(), taskId);
        startTimes.put(taskId, new Date());
    }

    /**
     * 任务执行后调用的钩子方法
     *
     * 在任务执行完成后调用（无论正常完成还是抛出异常）
     *
     * @param r 执行的任务
     * @param t 如果任务因异常而完成，则为该异常；否则为 null
     *
     * 注意：
     * - 如果任务正常完成，t 为 null
     * - 如果任务抛出未捕获的异常，t 包含该异常
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        Future<?> result = (Future<?>) r;
        try {
            // 输出任务完成的分隔信息
            System.out.printf("*********************************\n");
            System.out.printf("MyExecutor: A task is finishing.\n");

            // 获取任务返回值（不会阻塞，任务已完成）
            System.out.printf("MyExecutor: Result: %s\n", result.get());

            // 计算任务执行时长
            String taskId = String.valueOf(r.hashCode());
            Date startDate = startTimes.remove(taskId);
            Date finishDate = new Date();
            long diff = finishDate.getTime() - startDate.getTime();
            System.out.printf("MyExecutor: Duration: %d ms\n", diff);

            System.out.printf("*********************************\n");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
