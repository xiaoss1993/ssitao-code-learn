package com.ssitao.code.thread.code54.task;

import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义定时线程池 - 扩展 ScheduledThreadPoolExecutor
 *
 * 核心设计：
 * 1. 继承 ScheduledThreadPoolExecutor，添加自定义任务装饰
 * 2. 重写 decorateTask() 方法，将 RunnableScheduledFuture 包装为 MyScheduledTask
 * 3. 重写 scheduleAtFixedRate() 方法，设置周期执行的时间间隔
 *
 * 关键方法：
 * - decorateTask(): 装饰任务，将任务包装为自定义的 MyScheduledTask
 * - scheduleAtFixedRate(): 设置周期任务的执行间隔
 *
 * MyScheduledTask 的作用：
 * - 继承 FutureTask，实现 RunnableScheduledFuture
 * - 在 run() 方法中实现周期性执行的逻辑
 * - 计算下一次执行的时间延迟
 *
 * 使用场景：
 * - 需要在任务执行前后进行监控或日志记录
 * - 需要自定义任务的延迟计算逻辑
 * - 需要追踪周期性任务的执行时间
 */
public class MyScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    /**
     * 构造函数
     *
     * @param corePoolSize 核心线程数
     */
    public MyScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    /**
     * 装饰任务方法
     *
     * 当向线程池提交任务时，线程池会调用此方法将任务装饰为自定义类型
     * 将标准的 RunnableScheduledFuture 包装为 MyScheduledTask
     *
     * @param runnable 要执行的任务
     * @param task     原始的 RunnableScheduledFuture
     * @param <V>      任务结果的类型
     * @return 包装后的 MyScheduledTask
     */
    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable,
                                                          RunnableScheduledFuture<V> task) {
        // 将原始任务包装为 MyScheduledTask
        MyScheduledTask<V> myTask = new MyScheduledTask<V>(runnable, null, task, this);
        return myTask;
    }

    /**
     * 周期性固定速率调度
     *
     * 重写此方法以在周期任务执行时设置正确的时间间隔
     *
     * @param command      要执行的任务
     * @param initialDelay 初始延迟时间
     * @param period       周期时间
     * @param unit         时间单位
     * @return 周期任务的 Future
     */
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                  long initialDelay,
                                                  long period,
                                                  TimeUnit unit) {
        // 调用父类方法创建周期任务
        ScheduledFuture<?> task = super.scheduleAtFixedRate(command, initialDelay, period, unit);
        // 将任务转换为 MyScheduledTask
        MyScheduledTask<?> myTask = (MyScheduledTask<?>) task;
        // 设置周期时间（转换为毫秒）
        myTask.setPeriod(TimeUnit.MILLISECONDS.convert(period, unit));
        return task;
    }

}
