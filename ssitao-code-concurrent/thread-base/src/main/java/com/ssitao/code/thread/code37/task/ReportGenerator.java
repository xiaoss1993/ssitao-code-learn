package com.ssitao.code.thread.code37.task;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 报表生成类，实现 Callable<String>（任务角色）
 *
 * 【设计说明】
 * ReportGenerator 是实际执行报表生成的任务
 * - 实现 Callable 接口，可以返回执行结果
 * - 由线程池异步执行
 * - 耗时0-10秒（随机）
 *
 * 【本例场景】
 * 模拟一个真实的报表生成过程：
 * - 需要一定的处理时间（sleep 模拟）
 * - 返回格式化的报表名称
 *
 * 【与 Runnable 的区别】
 * - Runnable.run() 无返回值
 * - Callable.call() 有返回值，可抛检查异常
 * - ExecutorService.submit(Callable) 返回 Future，可以获取返回值
 */
public class ReportGenerator implements Callable<String> {
    /**
     * 报表发送者（标识来源系统）
     */
    private String sender;
    /**
     * 报表标题
     */
    private String title;

    /**
     * 构造函数
     *
     * @param sender 报表发送者
     * @param title  报表标题
     */
    public ReportGenerator(String sender, String title) {
        this.sender = sender;
        this.title = title;
    }

    /**
     * 核心方法：模拟生成报表
     *
     * 【执行流程】
     * 1. 生成随机耗时（0-10秒）
     * 2. 打印开始信息
     * 3. 休眠模拟处理时间
     * 4. 返回格式化的报表名称
     *
     * 【返回值】
     * 返回 "sender: title" 格式的字符串
     * 例如："Face: Report" 或 "Online: Report"
     * 该返回值会被 CompletionService 传递给消费者
     *
     * @return 报表名称字符串
     * @throws Exception 如果执行被中断
     */
    @Override
    public String call() throws Exception {
        try {
            // 生成随机处理时间（0-10秒）
            Long duration = (long) (Math.random() * 10);
            System.out.printf("%s_%s: ReportGenerator: Generating a report during %d seconds\n", this.sender, this.title, duration);
            // 休眠模拟报表生成过程
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 返回格式化的报表名称
        String ret = sender + ": " + title;
        return ret;
    }
}
