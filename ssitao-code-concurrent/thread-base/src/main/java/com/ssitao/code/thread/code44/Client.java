package com.ssitao.code.thread.code44;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 客户端任务类：模拟向队列添加请求的生产者
 *
 * 执行逻辑：
 * - 总共发送 3 批请求（i = 0, 1, 2）
 * - 每批发送 5 个请求（j = 0, 1, 2, 3, 4）
 * - 每批之间暂停 2 秒
 * - 请求格式："批次:序号"（如 "0:3" 表示第0批第3个请求）
 *
 * LinkedBlockingDeque.put() 特点：
 * - 如果队列已满（容量为3），会阻塞直到队列有空位
 * - 这是阻塞队列的核心特性：可实现生产者-消费者模式
 */
public class Client implements Runnable {

    /**
     * 目标阻塞队列引用
     */
    private LinkedBlockingDeque<String> requestList;

    /**
     * 构造函数
     *
     * @param requestList 目标阻塞队列
     */
    public Client(LinkedBlockingDeque<String> requestList) {
        this.requestList = requestList;
    }

    /**
     * 核心方法：发送请求到队列
     *
     * 执行流程：
     * 1. 外层循环：3批次
     * 2. 内层循环：每批次5个请求
     * 3. 每批次结束后：暂停2秒
     */
    @Override
    public void run() {
        // 外层循环：3批次请求
        for (int i = 0; i < 3; i++) {
            // 内层循环：每批次5个请求
            for (int j = 0; j < 5; j++) {
                // 构建请求字符串，格式："批次:序号"
                StringBuilder request = new StringBuilder();
                request.append(i);      // 批次号
                request.append(":");    // 分隔符
                request.append(j);      // 序号

                try {
                    // ========== put(): 插入元素到队列尾部 ==========
                    // 如果队列已满，会阻塞等待直到队列有空位
                    // 这体现了阻塞队列的核心特性
                    requestList.put(request.toString());

                } catch (InterruptedException e) {
                    // 当线程在阻塞时被中断，会抛出此异常
                    e.printStackTrace();
                }

                // 打印发送信息
                System.out.printf("Client: %s at %s.\n", request, new Date());
            }

            // ========== 批次间隔：暂停2秒 ==========
            // 每发送完一批5个请求后，暂停2秒再发送下一批
            // 这样可以观察队列满时的阻塞效果
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("Client: End.\n");
    }
}
