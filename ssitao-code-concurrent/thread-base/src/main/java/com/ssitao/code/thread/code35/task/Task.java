package com.ssitao.code.thread.code35.task;

import java.util.concurrent.Callable;

/**
 * 任务类，实现Callable接口
 *
 * 【任务说明】
 * 这是一个模拟长时任务的示例：
 * - 无限循环，每100毫秒打印一次消息
 * - 正常情况下永远不会结束
 * - 只能通过外部取消来终止
 *
 * 【为什么使用 while(true) 而不是具体条件？】
 * 为了演示 cancel() 机制
 * 真实场景可能是：
 * - 大文件下载
 * - 长时计算
 * - 持续监听
 *
 * 【与 Thread.interrupt() 的关系】
 * 当 Future.cancel(true) 被调用时
 * → 会调用执行线程的 interrupt() 方法
 * → Thread.sleep() 会检测到中断状态并抛出 InterruptedException
 * → 任务捕获异常后可以优雅退出
 */
public class Task implements Callable<String> {
    /**
     * 核心方法：无限循环任务
     *
     * 【执行流程】
     * 1. 进入无限循环
     * 2. 打印消息
     * 3. 休眠100毫秒
     * 4. 重复...
     *
     * 【中断响应】
     * 当 Thread.interrupt() 被调用时：
     * - Thread.sleep(100) 会抛出 InterruptedException
     * - 如果不捕获，异常会传播到线程池
     * - 任务被标记为失败/取消
     *
     * @return 不会返回（任务永远不会正常结束）
     * @throws Exception 如果任务被中断
     */
    @Override
    public String call() throws Exception {
        // 无限循环，模拟长时任务
        while (true) {
            System.out.printf("Task: Test\n");
            // 休眠100毫秒
            // 【重要】sleep() 会响应中断
            // 当 cancel(true) 被调用时，这里会抛出 InterruptedException
            Thread.sleep(100);
        }
    }
}
