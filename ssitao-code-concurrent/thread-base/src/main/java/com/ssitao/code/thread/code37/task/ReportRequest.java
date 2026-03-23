package com.ssitao.code.thread.code37.task;

import java.util.concurrent.CompletionService;

/**
 * 报表请求类，实现 Runnable（生产者角色）
 *
 * 【设计说明】
 * ReportRequest 是 CompletionService 的生产者
 * - 持有 CompletionService 引用
 * - run() 方法将 ReportGenerator 提交到 CompletionService
 *
 * 【本例场景】
 * 模拟两个报表生成系统：
 * - Face（人脸识别报表系统）
 * - Online（在线报表系统）
 * 每个系统独立运行，各自提交自己的报表生成任务
 *
 * 【为什么用 Runnable 而不是直接提交？】
 * ReportRequest 作为独立线程运行
 * 可以包含更多的业务逻辑（如日志、监控、错误重试等）
 */
public class ReportRequest implements Runnable {
    /**
     * 报表请求的名称（标识来源系统）
     */
    private String name;
    /**
     * 完成服务引用，用于提交任务
     */
    private CompletionService<String> service;

    /**
     * 构造函数
     *
     * @param name    报表请求名称
     * @param service 完成服务引用
     */
    public ReportRequest(String name, CompletionService<String> service) {
        this.name = name;
        this.service = service;
    }

    /**
     * 核心方法：提交报表生成任务
     *
     * 【执行流程】
     * 1. 创建 ReportGenerator（实际的任务）
     * 2. 通过 service.submit() 提交到线程池
     * 3. 任务异步执行，结果通过 CompletionService 获取
     *
     * 【注意】
     * submit() 是异步的，立即返回
     * 任务会在后台线程中执行，不会阻塞当前线程
     */
    @Override
    public void run() {
        ReportGenerator reportGenerator = new ReportGenerator(name, "Report");
        service.submit(reportGenerator);
    }
}
