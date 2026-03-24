package com.ssitao.code.thread.code56.task;

import java.util.Date;
import java.util.concurrent.ForkJoinTask;

/**
 * 自定义工作任务抽象类 - 直接继承 ForkJoinTask
 *
 * 核心设计：
 * 1. 直接继承 ForkJoinTask<Void>，比 RecursiveTask 更底层
 * 2. 实现 exec() 方法作为任务执行入口
 * 3. 定义抽象 compute() 方法让子类实现具体逻辑
 * 4. 在 exec() 中添加计时逻辑，追踪任务执行时长
 *
 * ForkJoinTask 需要实现的方法：
 * - exec(): 任务执行逻辑，返回是否正常完成
 * - getRawResult(): 获取任务结果（本例返回 null，因为是 Void）
 * - setRawResult(): 设置任务结果（本例为空）
 *
 * exec() vs compute()：
 * - exec() 是 ForkJoinTask 的最终执行方法
 * - compute() 是我们定义的抽象方法，子类实现具体逻辑
 * - exec() 会调用 compute() 并添加计时等额外逻辑
 *
 * 使用场景：
 * - 需要更细粒度地控制任务执行流程
 * - 需要在任务执行前后添加自定义逻辑（如监控、日志）
 * - 需要追踪任务执行时间
 */
public abstract class MyWorkerTask extends ForkJoinTask<Void> {

    /**
     * 序列化版本ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 任务名称，用于日志和调试
     */
    private String name;

    /**
     * 构造函数
     *
     * @param name 任务名称
     */
    public MyWorkerTask(String name) {
        this.name = name;
    }

    /**
     * 获取任务结果
     *
     * ForkJoinTask 需要实现的方法
     * 本类返回 null，因为是无返回值的任务
     *
     * @return null（无返回值）
     */
    @Override
    public Void getRawResult() {
        return null;
    }

    /**
     * 设置任务结果
     *
     * ForkJoinTask 需要实现的方法
     * 本类为空，因为是无返回值的任务
     *
     * @param value 结果值（本例忽略）
     */
    @Override
    protected void setRawResult(Void value) {
        // 无操作，因为任务无返回值
    }

    /**
     * 任务执行方法
     *
     * ForkJoinTask 的核心执行方法
     * 在此方法中调用子类的 compute() 并记录执行时间
     *
     * @return true 表示正常完成
     */
    @Override
    protected boolean exec() {
        // 记录开始时间
        Date startDate = new Date();

        // 调用子类的 compute() 方法执行实际任务
        compute();

        // 记录结束时间
        Date finishDate = new Date();
        long diff = finishDate.getTime() - startDate.getTime();

        // 打印任务执行信息
        System.out.printf("MyWorkerTask: %s : %d Milliseconds to complete.\n", name, diff);

        return true;
    }

    /**
     * 获取任务名称
     *
     * @return 任务名称
     */
    public String getName() {
        return name;
    }

    /**
     * 抽象方法，子类实现具体的计算逻辑
     *
     * 类似于 RecursiveTask 的 compute() 方法
     * 但这里是由 exec() 调用，而不是直接被线程池调用
     */
    protected abstract void compute();
}
