package com.ssitao.code.thread.code54.task;

import java.util.Date;
import java.util.concurrent.*;

/**
 * 自定义定时任务 - 扩展 FutureTask 并实现 RunnableScheduledFuture
 *
 * 核心设计：
 * 1. 继承 FutureTask（支持异步结果）
 * 2. 实现 RunnableScheduledFuture（支持定时调度）
 * 3. 重写 run() 方法，实现周期性执行的再入队逻辑
 * 4. 重写 getDelay() 方法，计算下一次执行的延迟
 *
 * 关键机制：
 * - 周期性任务执行完成后，通过 getQueue().add(this) 重新加入队列
 * - 下一次执行时间 = 当前执行开始时间 + period
 * - 使用 startDate 追踪下一次执行的时间点
 *
 * 与普通 ScheduledFutureTask 的区别：
 * - 添加了执行前后的日志输出
 * - 自定义延迟计算逻辑
 *
 * 注意：
 * - runAndReset() 是 FutureTask 的受保护方法
 * - 执行完任务后重置状态，以便重复执行
 */
public class MyScheduledTask<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {


    /**
     * 原始的 RunnableScheduledFuture
     * 用于委托一些方法实现
     */
    private RunnableScheduledFuture<V> task;

    /**
     * 所属的定时线程池
     * 用于访问任务队列
     */
    private ScheduledThreadPoolExecutor executor;

    /**
     * 周期任务的执行周期（毫秒）
     */
    private long period;

    /**
     * 下一次执行的开始时间戳（毫秒）
     * 为0时表示尚未设置
     */
    private long startDate;

    /**
     * 构造函数
     *
     * @param runnable  要执行的任务
     * @param result    任务结果的默认值
     * @param task      原始的 RunnableScheduledFuture
     * @param executor  所属的定时线程池
     */
    public MyScheduledTask(Runnable runnable, V result, RunnableScheduledFuture<V> task, ScheduledThreadPoolExecutor executor) {
        super(runnable, result);
        this.task = task;
        this.executor = executor;
    }

    /**
     * 任务执行方法
     *
     * 重写此方法以实现：
     * 1. 周期性任务执行后将自身重新加入队列
     * 2. 打印执行前后的日志
     * 3. 调用 runAndReset() 执行任务并重置状态
     */
    @Override
    public void run() {
        // 如果是周期性任务，且线程池未关闭
        if (isPeriodic() && (!executor.isShutdown())) {
            // 计算下一次执行的开始时间 = 当前时间 + 周期
            Date now = new Date();
            startDate = now.getTime() + period;
            // 将任务重新加入队列，等待下一次执行
            executor.getQueue().add(this);
        }

        // 打印执行前的日志
        System.out.printf("Pre-MyScheduledTask: %s\n", new Date());
        System.out.printf("MyScheduledTask: Is Periodic: %s\n", isPeriodic());

        // 执行任务并重置状态（用于周期性任务）
        super.runAndReset();

        // 打印执行后的日志
        System.out.printf("Post-MyScheduledTask: %s\n", new Date());
    }

    /**
     * 设置周期任务的执行周期
     *
     * @param period 周期时间（毫秒）
     */
    public void setPeriod(long period) {
        this.period = period;
    }

    /**
     * 判断是否是周期性任务
     *
     * @return true 表示周期性任务，false 表示一次性任务
     */
    @Override
    public boolean isPeriodic() {
        return task.isPeriodic();
    }

    /**
     * 获取距离下一次执行还剩多少时间
     *
     * @param unit 时间单位
     * @return 延迟时间
     *
     * 逻辑：
     * - 非周期性任务：使用原始任务的延迟
     * - 周期性任务且 startDate 未设置：使用原始任务的延迟
     * - 周期性任务且 startDate 已设置：计算 startDate - now
     */
    @Override
    public long getDelay(TimeUnit unit) {
        if (!isPeriodic()) {
            // 非周期性任务，使用原始任务的延迟
            return task.getDelay(unit);
        } else {
            if (startDate == 0) {
                // 第一次执行，使用原始任务的延迟
                return task.getDelay(unit);
            } else {
                // 计算下一次执行的延迟
                Date now = new Date();
                long delay = startDate - now.getTime();
                return unit.convert(delay, TimeUnit.MILLISECONDS);
            }
        }
    }

    /**
     * 比较两个定时任务的优先级
     *
     * @param o 要比较的 Delayed 对象
     * @return 比较结果
     */
    @Override
    public int compareTo(Delayed o) {
        return task.compareTo(o);
    }

}
