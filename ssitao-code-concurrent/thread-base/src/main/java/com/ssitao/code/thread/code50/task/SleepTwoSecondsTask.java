package com.ssitao.code.thread.code50.task;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 任务类 - Callable 实现示例
 *
 * 实现 Callable 接口，可作为 ThreadPoolExecutor.submit() 的参数
 * 与 Runnable 的区别：
 * - Callable 有返回值（泛型），Runnable 没有
 * - Callable 可以抛出异常，Runnable 不能
 * - Callable 的 call() 方法会被 Future.get() 返回
 *
 * 任务逻辑：
 * 1. 睡眠2秒（模拟耗时操作）
 * 2. 返回当前时间字符串
 *
 * 设计目的：
 * - 演示 Callable 的基本用法
 * - 作为执行器框架的任务示例
 */
public class SleepTwoSecondsTask implements Callable<String> {


    /**
     * 任务执行方法
     *
     * @return 当前时间字符串（任务完成时的时间）
     * @throws Exception 如果执行过程中发生异常
     *
     * 注意：
     * - 此方法由线程池中的工作线程调用
     * - 睡眠2秒模拟耗时操作（如IO、网络请求等）
     * - 返回值会被包装在 Future 中
     */
    @Override
    public String call() throws Exception {
        // 睡眠2秒，模拟耗时操作
        TimeUnit.SECONDS.sleep(2);
        // 返回当前时间字符串
        return new Date().toString();
    }

}