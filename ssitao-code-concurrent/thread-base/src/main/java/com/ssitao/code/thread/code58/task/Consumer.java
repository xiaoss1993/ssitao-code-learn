package com.ssitao.code.thread.code58.task;

/**
 * 消费者类 - 从队列中获取并处理事件
 *
 * 实现 Runnable 接口，在独立线程中运行
 * 负责从队列中获取事件并打印
 *
 * 设计特点：
 * 1. 使用 take() 方法（阻塞获取）
 * 2. 优先获取 transfered 队列中的元素
 * 3. 消费 1002 个事件：
 *    - 1000 个来自生产者
 *    - 2 个来自主线程的 transfer()
 *
 * 优先级处理：
 * - 事件按优先级排序，低优先级数字先被消费
 * - 因为 Event.compareTo() 返回 -1 当 priority < other 时
 */
public class Consumer implements Runnable {


    /**
     * 共享的优先级传输队列
     */
    private MyPriorityTransferQueue<Event> buffer;

    /**
     * 构造函数
     *
     * @param buffer 共享的优先级传输队列
     */
    public Consumer(MyPriorityTransferQueue<Event> buffer) {
        this.buffer = buffer;
    }

    /**
     * 任务执行方法
     *
     * 消费 1002 个事件：
     * 1. 从队列获取事件（阻塞等待）
     * 2. 打印事件信息（线程名称和优先级）
     */
    @Override
    public void run() {
        for (int i = 0; i < 1002; i++) {
            try {
                // 阻塞获取事件
                Event value = buffer.take();
                // 打印事件信息
                System.out.printf("Consumer: %s: %d\n", value.getThread(), value.getPriority());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
