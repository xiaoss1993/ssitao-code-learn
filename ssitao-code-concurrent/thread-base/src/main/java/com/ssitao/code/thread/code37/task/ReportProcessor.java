package com.ssitao.code.thread.code37.task;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 报表处理器类，实现 Runnable（消费者角色）
 *
 * 【设计说明】
 * ReportProcessor 是 CompletionService 的消费者
 * - 持有 CompletionService 引用
 * - run() 方法持续从 CompletionService 获取已完成的任务结果
 *
 * 【与生产者的关系】
 * 生产者（ReportRequest）提交任务，消费者（ReportProcessor）处理结果
 * 两者通过 CompletionService 内部队列解耦
 *
 * 【poll() vs take()】
 * - poll(timeout, TimeUnit): 等待指定时间，返回null如果超时
 * - take(): 一直等待，直到有结果返回
 * 本例使用 poll(20, SECONDS)，每20秒检查一次
 */
public class ReportProcessor implements Runnable {
    /**
     * 完成服务引用，用于获取已完成任务的结果
     */
    private CompletionService<String> service;
    /**
     * 结束标记，用于优雅关闭
     */
    private boolean end;

    /**
     * 构造函数
     *
     * @param service 完成服务引用
     */
    public ReportProcessor(CompletionService<String> service) {
        this.service = service;
    }


    /**
     * 核心方法：持续获取并处理已完成的报表
     *
     * 【执行流程】
     * 1. while(!end) 循环，直到收到结束信号
     * 2. poll(20, SECONDS) 等待20秒获取已完成任务
     * 3. 如果有结果，调用 get() 获取并打印
     * 4. 如果返回null（超时），继续下一次循环
     *
     * 【为什么用 poll 而不是 take？】
     * - take() 会一直阻塞，直到有结果
     * - poll(timeout) 可以定期检查 end 标记
     * - 这样才能优雅响应 shutdown 请求
     *
     * 【线程安全】
     * end 字段由 Main 线程设置，processor 线程读取
     * 但由于标志切换不频繁，不需要 volatile（实际建议用 volatile）
     */
    @Override
    public void run() {
        while (!end) {
            try {
                // poll(timeout, TimeUnit): 等待指定时间获取结果
                // 返回 null 表示超时（20秒内没有任务完成）
                // 返回 Future 表示有任务完成
                Future<String> result = service.poll(20, TimeUnit.SECONDS);
                if (result != null) {
                    // get() 获取任务返回值（任务已完成，不会阻塞）
                    String report = result.get();
                    System.out.printf("ReportReceiver: Report Received: %s\n", report);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("ReportSender: End\n");
    }

    /**
     * 设置结束标记
     *
     * @param end 结束标记，true 表示停止处理
     */
    public void setEnd(boolean end) {
        this.end = end;
    }
}
