package com.ssitao.code.thread.code52.task;

import java.util.concurrent.TimeUnit;

/**
 * 任务类 - 简单的 Runnable 实现
 *
 * 实现 Runnable 接口，作为线程执行的任务
 *
 * 任务逻辑：
 * - 睡眠2秒，模拟耗时操作
 *
 * 设计目的：
 * - 作为线程工厂创建线程时的任务示例
 * - 用于测试 MyThread 的执行时间统计功能
 */
public class MyTask implements Runnable{

    /**
     * 任务执行方法
     *
     * 睡眠2秒，模拟耗时操作（如IO操作、网络请求等）
     */
    @Override
    public void run() {
        try {
            // 睡眠2秒
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
