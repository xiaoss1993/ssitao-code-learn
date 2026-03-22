package com.ssitao.code.thread.code16.task;

/**
 * 消费者线程类
 *
 * 实现了Runnable接口，作为线程任务运行。
 * 职责：从EventStorage存储区反复消费(取出)事件。
 *
 * 【生产者-消费者模式中的角色】
 * 本类扮演"消费者"角色，从共享缓冲区取出数据/事件进行处理。
 *
 * 【执行行为】
 * 循环100次，每次调用eventStorage.get()消费一个事件。
 * 当存储区空时，get()内部会等待(调用wait())直到生产者生产。
 *
 * 【线程安全】
 * 线程安全由EventStorage的synchronized方法和wait()/notify()机制保证。
 */
public class Consumer implements Runnable {
    /**
     * 事件存储区引用
     * 生产者和消费者共享同一个存储区对象
     */
    private EventStorage storage;

    /**
     * 构造函数
     *
     * @param storage 事件存储对象
     */
    public Consumer(EventStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        // 循环100次，每次从存储区消费一个事件
        // EventStorage.get()方法会处理空的情况（等待生产者）
        for (int i = 0; i < 100; i++) {
            this.storage.get();
        }
    }
}
