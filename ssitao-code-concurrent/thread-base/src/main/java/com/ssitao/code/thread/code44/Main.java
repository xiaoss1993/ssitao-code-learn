package com.ssitao.code.thread.code44;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * LinkedBlockingDeque 阻塞队列演示：生产者-消费者模式
 *
 * 本示例展示 LinkedBlockingDeque 的特点：
 * 1. 有界队列：容量固定为3
 * 2. 阻塞操作：take() 在队列为空时会阻塞等待
 * 3. 线程安全：多线程并发访问无需额外同步
 *
 * 工作流程：
 * - Client（生产者）：每2秒向队列添加5个请求
 * - Main（消费者）：每300ms从队列取走3个请求
 * - 由于队列容量为3，当队列满时 put() 会阻塞
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // ========== 1. 创建有界阻塞队列 ==========
        // 容量设为3，这意味着：
        // - 当队列满时，put() 操作会阻塞
        // - 当队列空时，take() 操作会阻塞
        LinkedBlockingDeque<String> list = new LinkedBlockingDeque<>(3);

        // ========== 2. 创建并启动客户端线程 ==========
        // Client 会不断向队列添加请求
        Client client = new Client(list);
        Thread thread = new Thread(client);
        thread.start();

        // ========== 3. 主线程消费请求 ==========
        // 循环5次，每次取走3个请求
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                // take(): 获取并移除队列头部元素
                // 如果队列为空，会阻塞直到有元素可用
                String request = list.take();
                System.out.printf(
                    "Main: Request: %s at %s. Size: %d\n",
                    request,
                    new Date(),
                    list.size()
                );
            }
            // 每批次之间暂停300毫秒
            TimeUnit.MILLISECONDS.sleep(300);
        }

        System.out.printf("Main: End of the program.\n");
    }
}
