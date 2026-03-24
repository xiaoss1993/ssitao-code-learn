package com.ssitao.code.thread.code45.task;

import java.util.Date;
import java.util.concurrent.DelayQueue;

/**
 * 任务类 - 事件生产者
 *
 * 每个 Task 实例在独立线程中运行
 * 负责向 DelayQueue 中添加延迟事件
 *
 * 设计特点：
 * 1. 多个线程并发添加事件，提高吞吐量
 * 2. 每个任务创建100个事件，全部使用相同的延迟时间
 * 3. 任务ID不同，延迟时间也不同（ID * 1秒）
 *
 * 这样设计可以模拟：
 * - 不同优先级的任务在不同时间到期
 * - 验证 DelayQueue 按到期时间排序的特性
 */
public class Task implements Runnable {

    /**
     * 任务编号，用于确定延迟时间
     * ID越大，延迟越长（ID * 1秒）
     */
    private int id;

    /**
     * 共享的延迟队列，所有任务的事件都添加到这个队列
     * DelayQueue 是线程安全的，支持并发添加
     */
    private DelayQueue<Event> queue;

    /**
     * 构造函数，初始化任务属性
     *
     * @param id    任务编号（用于计算延迟时间）
     * @param queue 共享的延迟队列
     */
    public Task(int id, DelayQueue<Event> queue) {
        this.id = id;
        this.queue = queue;
    }


    /**
     * 任务执行入口
     *
     * 核心逻辑：
     * 1. 计算延迟时间 = 当前时间 + (ID * 1秒)
     *    - 任务1: 延迟1秒
     *    - 任务2: 延迟2秒
     *    - 任务3: 延迟3秒
     *    - 任务4: 延迟4秒
     *    - 任务5: 延迟5秒
     *
     * 2. 创建100个事件，全部使用相同的延迟时间
     * 3. 将所有事件添加到延迟队列中
     */
    @Override
    public void run() {

        // 获取当前时间
        Date now = new Date();

        // 计算激活时间 = 当前时间 + (ID * 1秒)
        // 这样Task 1的事件会在1秒后到期，Task 5的事件会在5秒后到期
        Date delay = new Date();
        delay.setTime(now.getTime() + (id * 1000));

        System.out.printf("Thread %s: created 100 events with delay %s\n", id, delay);

        // 创建100个事件，全部使用相同的延迟时间
        for (int i = 0; i < 100; i++) {
            Event event = new Event(delay);
            queue.add(event);
        }
    }

}
