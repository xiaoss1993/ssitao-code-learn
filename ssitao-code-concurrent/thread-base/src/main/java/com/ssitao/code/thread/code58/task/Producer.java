package com.ssitao.code.thread.code58.task;


/**
 * 生产者类 - 向队列中添加事件
 *
 * 实现 Runnable 接口，在独立线程中运行
 * 负责创建并向队列中添加事件
 *
 * 设计特点：
 * 1. 使用共享的 MyPriorityTransferQueue
 * 2. 每个生产者生产100个事件
 * 3. 使用 put() 方法（阻塞添加）
 *
 * 生产者数量：10个线程
 * 总事件数量：10 × 100 = 1000 个事件
 *
 * 与消费者配合：
 * - 消费者期望消费 1002 个事件
 * - 1000 个来自生产者，2 个来自主线程的 transfer()
 */
public class Producer implements Runnable{

    /**
     * 共享的优先级传输队列
     */
    private MyPriorityTransferQueue<Event> buffer;

    /**
     * 构造函数
     *
     * @param buffer 共享的优先级传输队列
     */
    public Producer(MyPriorityTransferQueue<Event> buffer) {
        this.buffer = buffer;
    }

    /**
     * 任务执行方法
     *
     * 生产100个事件，每个事件包含：
     * - thread: 线程名称
     * - priority: 事件优先级（0-99）
     */
    @Override
    public void run() {
        // 生产100个事件
        for (int i = 0; i < 100; i++) {
            // 创建事件
            Event event = new Event(Thread.currentThread().getName(), i);
            // 添加到队列
            buffer.put(event);
        }
    }
}
