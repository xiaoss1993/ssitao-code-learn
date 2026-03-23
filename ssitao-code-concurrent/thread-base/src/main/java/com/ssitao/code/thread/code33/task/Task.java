package com.ssitao.code.thread.code33.task;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * 任务类，实现Callable接口，参数化为String类型
 *
 * 【泛型说明】
 * Callable<String> 表示 call() 方法返回 String 类型
 * 返回值可以通过 Future.get() 获取
 *
 * 【任务说明】
 * 这是一个简单的定时任务示例：
 * - 输出任务开始时间
 * - 返回"Hello, world"作为执行结果
 *
 * 【实际场景】
 * 真实的定时任务可能包含：
 * - 发送提醒邮件
 * - 清理过期缓存
 * - 生成报表
 * - 数据同步
 */
public class Task implements Callable<String> {
    /**
     * 任务名称，用于标识是哪个任务在执行
     */
    private String name;

    /**
     * 构造函数，初始化任务名称
     *
     * @param name 任务名称
     */
    public Task(String name) {
        this.name = name;
    }

    /**
     * 核心方法：执行定时任务
     *
     * 【执行时机】
     * 此方法不会立即执行，而是由ScheduledThreadPoolExecutor
     * 根据schedule()指定的延迟时间，在指定时间后才调用
     *
     * 【返回值】
     * 返回值会封装在Future中，可通过 future.get() 获取
     * 如果任务执行失败，get() 会抛出 ExecutionException
     *
     * @return 执行结果字符串
     * @throws Exception 如果任务执行失败
     */
    @Override
    public String call() throws Exception {
        // 输出任务开始执行的时间戳
        // 因为是定时任务，这个时间 = 提交时间 + 延迟时间
        System.out.printf("%s: Starting at : %s\n", name, new Date());

        // 返回执行结果
        // 实际场景中可能是具体的业务数据
        return "Hello, world";
    }
}
