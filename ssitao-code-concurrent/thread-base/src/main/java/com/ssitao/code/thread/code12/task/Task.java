package com.ssitao.code.thread.code12.task;

import java.util.concurrent.TimeUnit;

/**
 * 简单的任务实现
 *
 * 演示最简单的 Runnable 用法：让当前线程睡眠1秒
 */
public class Task implements Runnable {

    /**
     * 任务执行逻辑
     *
     * 当线程调用 start() 后，会执行此方法
     * 此处只是简单让线程睡眠1秒
     */
    @Override
    public void run() {
        try {
            // 睡眠1秒，模拟任务执行过程
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            // 如果线程被中断，捕获异常并打印
            // 注意：InterruptedException 通常表示线程被请求停止
            e.printStackTrace();
        }
    }
}
