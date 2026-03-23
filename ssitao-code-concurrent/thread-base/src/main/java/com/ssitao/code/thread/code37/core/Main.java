package com.ssitao.code.thread.code37.core;


import com.ssitao.code.thread.code37.task.ReportProcessor;
import com.ssitao.code.thread.code37.task.ReportRequest;

import java.util.concurrent.*;

/**
 * 代码37：使用 CompletionService 实现任务提交与结果消费分离
 *
 * 【核心概念】
 * CompletionService 是 Java 并发库中的接口
 * 它将任务提交（生产者）和结果获取（消费者）解耦
 *
 * 【CompletionService vs ExecutorService】
 *
 * 1. ExecutorService
 *    - submit() 提交任务，返回 Future
 *    - 需要自己管理 Future 列表
 *    - 按提交顺序获取结果，不灵活
 *
 * 2. CompletionService
 *    - submit() 提交任务
 *    - poll()/take() 获取已完成任务的结果
 *    - 结果按完成顺序返回（谁先完成谁先返回）
 *
 * 【本例架构】
 * ┌─────────────────┐    submit()    ┌──────────────────────┐
 * │  ReportRequest   │ ─────────────>│  CompletionService   │
 * │  (生产者-生成报表) │               │  (ExecutorCompletion) │
 * └─────────────────┘               └──────────┬───────────┘
 *                                              │
 *                                              │ poll()/take()
 *                                              ↓
 *                               ┌──────────────────────────┐
 *                               │   ReportProcessor        │
 *                               │   (消费者-处理报表结果)   │
 *                               └──────────────────────────┘
 *
 * 【应用场景】
 * - 多个任务并发执行，消费者按完成顺序处理结果
 * - 搜索引擎：同时查询多个引擎，先返回的结果先展示
 * - 股票报价：同时查询多个市场，先返回的报价先显示
 */
public class Main {
    public static void main(String[] args) {
        // ========== 1. 创建线程池和 CompletionService ==========
        // ExecutorService：负责执行任务
        // CompletionService：包装 ExecutorService，提供结果队列功能
        //
        // 内部原理：
        // CompletionService 内部维护了一个 BlockingQueue
        // 任务完成时，结果自动加入队列
        // 消费者通过 poll()/take() 从队列取结果
        ExecutorService executor = (ExecutorService) Executors.newCachedThreadPool();
        CompletionService<String> service = new ExecutorCompletionService<>(executor);

        // ========== 2. 创建报表请求对象（生产者） ==========
        // 两个请求对象分别模拟：人脸报表系统、在线报表系统
        // 每个请求会生成一份报表并提交到 CompletionService
        ReportRequest faceRequest = new ReportRequest("Face", service);
        ReportRequest onlineRequest = new ReportRequest("Online", service);
        Thread faceThread = new Thread(faceRequest);
        Thread onlineThread = new Thread(onlineRequest);

        // ========== 3. 创建报表处理器（消费者） ==========
        // 报表处理器不断从 CompletionService 获取已完成的报表
        // 按完成顺序处理，而不是提交顺序
        ReportProcessor processor = new ReportProcessor(service);
        Thread senderThread = new Thread(processor);

        // ========== 4. 启动所有线程 ==========
        System.out.printf("Main: Starting the Threads\n");
        faceThread.start();
        onlineThread.start();
        senderThread.start();

        // ========== 5. 等待报表生成者完成 ==========
        // join() 等待线程执行完毕
        // 这里等待 faceRequest 和 onlineRequest 完成
        // 注意：只是等待报表"提交"完成，不是报表"生成"完成
        // 报表生成是异步的，由线程池执行
        try {
            System.out.printf("Main: Waiting for the report generators.\n");
            faceThread.join();
            onlineThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ========== 6. 关闭线程池 ==========
        // shutdown() 停止接收新任务
        // awaitTermination() 等待所有已提交任务完成
        System.out.printf("Main: Shuting down the executor.\n");
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ========== 7. 通知处理器结束 ==========
        // 设置 end=true 让 ReportProcessor 的循环结束
        // 这样 senderThread 会正常退出
        processor.setEnd(true);
        System.out.printf("Main: Ends\n");
    }
}
