package com.ssitao.code.thread.code36.task;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


/**
 * 可执行任务类，实现 Callable<String> 接口
 *
 * 【泛型说明】
 * Callable<String> 表示 call() 方法返回 String 类型
 *
 * 【任务说明】
 * 这是一个模拟耗时任务的示例：
 * - 随机等待0-10秒
 * - 返回包含任务名称的结果字符串
 *
 * 【中断处理】
 * 注意这里 catch 了 InterruptedException 但没有 e.printStackTrace()
 * 原因：当任务被取消时，Thread.sleep() 会抛出 InterruptedException
 * - 如果传播出去，会打印大量堆栈
 * - 这里静默处理，让任务自然结束
 * - cancel(true) 已经能正确取消任务，不需要打印异常
 */
public class ExecutableTask implements Callable<String> {
    /**
     * 任务名称，用于标识和日志输出
     */
    private String name;

    public ExecutableTask(String name) {
        this.name = name;
    }

    /**
     * 核心方法：模拟耗时计算任务
     *
     * 【执行流程】
     * 1. 生成随机等待时间（0-10秒）
     * 2. 打印等待时间和任务名称
     * 3. 休眠指定时间
     * 4. 返回包含任务名称的结果字符串
     *
     * 【中断处理说明】
     * 当 Future.cancel(true) 被调用时：
     * → Thread.sleep() 抛出 InterruptedException
     * → 这里选择静默处理，让 call() 正常返回
     * → 虽然返回了，但 FutureTask 已是 CANCELLED 状态
     * → ResultTask.done() 会打印 "Has been cancelled"
     *
     * @return 包含任务名称的问候字符串
     */
    @Override
    public String call() throws Exception {
        try {
            // 生成随机等待时间（0-10秒）
            Long duration = (long) (Math.random() * 10);
            System.out.printf("%s: Waiting %d seconds for results.\n", this.name, duration);
            // 休眠期间如果被中断，会抛出 InterruptedException
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            // 【重要】静默处理中断异常
            // 不打印堆栈，让任务自然结束
            // 原因：cancel() 取消任务时，sleep() 会自动中断
            // 我们不需要在这里处理，FutureTask 的状态已经是 CANCELLED
        }
        // 返回结果字符串
        return "Hello, world. I'm " + name;
    }

    /**
     * 获取任务名称
     *
     * @return 任务名称
     */
    public String getName() {
        return name;
    }
}
