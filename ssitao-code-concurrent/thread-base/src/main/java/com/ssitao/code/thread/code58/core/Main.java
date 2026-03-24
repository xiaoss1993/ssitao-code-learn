package com.ssitao.code.thread.code58.core;


import com.ssitao.code.thread.code58.task.Consumer;
import com.ssitao.code.thread.code58.task.Event;
import com.ssitao.code.thread.code58.task.MyPriorityTransferQueue;
import com.ssitao.code.thread.code58.task.Producer;

import java.util.concurrent.TimeUnit;


/**
 * 主程序 - PriorityBlockingQueue + TransferQueue 示例
 *
 * 演示使用自定义 PriorityTransferQueue 实现优先级传输队列
 *
 * 核心概念：
 * 1. TransferQueue - 一种特殊的 BlockingQueue
 *    - 支持"传输"操作：生产者等待消费者接收元素
 *    - 确保元素被消费者处理后才返回（不像 put 异步）
 *    - 用于实现任务直接传递（如 Executor 的任务窃取）
 *
 * 2. PriorityBlockingQueue - 优先级阻塞队列
 *    - 元素按优先级排序（实现 Comparable 接口）
 *    - 优先级高的元素先出队
 *    - 无界队列，不会阻塞生产者
 *
 * 3. TransferQueue 方法：
 *    - transfer(E e): 传输元素，阻塞直到被消费
 *    - tryTransfer(E e): 非阻塞传输，若无消费者返回 false
 *    - tryTransfer(E e, timeout): 限时传输
 *
 * 4. 自定义 MyPriorityTransferQueue 实现：
 *    - 结合 PriorityBlockingQueue 和 TransferQueue
 *    - 使用 counter 追踪等待的消费者数量
 *    - 使用 transfered 队列存储等待传递的元素
 *
 * 执行流程：
 * 1. 创建 PriorityTransferQueue
 * 2. 启动10个生产者线程（每个生产100个事件）
 * 3. 启动1个消费者线程（消费1002个事件）
 * 4. 主线程执行 transfer() 传输事件
 * 5. 等待所有线程完成
 *
 * 传输机制：
 * - transfer() 时如果有等待中的消费者，直接交给消费者
 * - 如果没有消费者，元素进入 transfered 队列等待
 * - 消费者 take() 时优先从 transfered 队列取
 */
public class Main {

    public static void main(String[] args) throws Exception {

        // 创建优先级传输队列
        MyPriorityTransferQueue<Event> buffer = new MyPriorityTransferQueue<>();

        // 创建生产者
        Producer producer = new Producer(buffer);

        // 创建10个生产者线程
        Thread producerThreads[] = new Thread[10];
        for (int i = 0; i < producerThreads.length; i++) {
            producerThreads[i] = new Thread(producer);
            producerThreads[i].start();
        }

        // 创建消费者
        Consumer consumer = new Consumer(buffer);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        // 打印当前等待的消费者数量
        // 此时应该为0或1（消费者刚开始或正在等待）
        System.out.printf("Main: Buffer: Consumer count: %d\n", buffer.getWaitingConsumerCount());


        // 主线程传输事件（阻塞直到被消费）
        Event myEvent = new Event("Core Event", 0);
        buffer.transfer(myEvent);
        System.out.printf("Main: My Event has been transfered.\n");


        // 等待所有生产者完成
        for (int i = 0; i < producerThreads.length; i++) {
            producerThreads[i].join();
        }

        TimeUnit.SECONDS.sleep(1);

        // 再次打印等待的消费者数量
        System.out.printf("Main: Buffer: Consumer count: %d\n", buffer.getWaitingConsumerCount());

        // 主线程再次传输事件
        myEvent = new Event("Core Event 2", 0);
        buffer.transfer(myEvent);

        // 等待消费者完成
        consumerThread.join();

        System.out.printf("Main: End of the program\n");
    }
}
