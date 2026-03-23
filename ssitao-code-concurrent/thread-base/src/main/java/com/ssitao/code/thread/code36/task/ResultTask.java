package com.ssitao.code.thread.code36.task;


import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 结果任务类，继承 FutureTask<String>
 *
 * 【设计说明】
 * ResultTask 是 FutureTask 的子类，作为 ExecutableTask 的装饰器
 * - FutureTask 提供了异步执行和取消的基础功能
 * - ResultTask 在此基础上添加了任务完成时的通知机制
 *
 * 【装饰器模式】
 * ResultTask 包装了 Callable（实际是 ExecutableTask）
 * 可以在不修改原始任务的情况下添加新功能（完成通知）
 *
 * 【done() 钩子方法】
 * 这是 FutureTask 的模板方法
 * 当任务进入终态（正常完成/取消/异常）时自动调用
 * 子类可以重写此方法执行自定义逻辑
 *
 * 【线程安全】
 * done() 由线程池的Worker线程调用
 * 添加了完成日志，用于监控和调试
 */
public class ResultTask extends FutureTask<String> {
    /**
     * 任务名称，用于日志输出
     */
    private String name;

    /**
     * 构造函数
     *
     * @param callable 实际要执行的任务（必须是 ExecutableTask）
     */
    public ResultTask(Callable<String> callable) {
        super(callable);
        // 从 callable 中获取任务名称，用于后续日志输出
        this.name = ((ExecutableTask) callable).getName();
    }

    /**
     * 任务完成时的回调方法（钩子方法）
     *
     * 【调用时机】
     * 当任务进入终态时，由线程池的Worker线程调用：
     * - 任务正常完成 → done()
     * - 任务被取消 → done()
     * - 任务执行异常 → done()
     *
     * 【本例实现】
     * 根据 isCancelled() 判断是取消还是正常完成
     * 打印相应的日志信息
     */
    @Override
    protected void done() {
        // isCancelled() 检查任务是否已被取消
        if (isCancelled()) {
            System.out.printf("%s: Has been cancelled\n", name);
        } else {
            System.out.printf("%s: Has finished\n", name);
        }
    }
}