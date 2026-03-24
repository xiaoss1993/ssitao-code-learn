package com.ssitao.code.thread.code45.core;
/**
 * DelayQueue 延迟队列示例
 *
 * 本示例演示 Java 并发包中的 DelayQueue，它是一种特殊的阻塞队列
 * 特点：只有当元素的延迟到期时，才能从队列中取出该元素
 *
 * 核心概念：
 * 1. DelayQueue<E extends Delayed> - 存储实现 Delayed 接口的元素
 * 2. Delayed 接口要求实现 getDelay() 和 compareTo() 方法
 * 3. 线程池中的 ScheduledThreadPoolExecutor 就使用了类似的延迟队列机制
 */


import com.ssitao.code.thread.code45.task.Event;
import com.ssitao.code.thread.code45.task.Task;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        // 创建存储事件的延迟队列
        // DelayQueue 是无界队列，内部使用 PriorityQueue（基于堆的优先级队列）存储
        DelayQueue<Event> queue = new DelayQueue<>();

        // 创建5个线程数组
        Thread threads[] = new Thread[5];

        // 创建5个任务，每个任务在不同的线程中执行
        // 任务ID从1到5，每个任务会向队列添加100个事件
        for (int i = 0; i < threads.length; i++) {
            Task task = new Task(i + 1, queue);
            threads[i] = new Thread(task);
        }

        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }

        // 等待所有5个任务线程执行完成
        // join() 会阻塞主线程，直到每个线程执行完毕
        for (Thread thread : threads) {
            thread.join();
        }

        // 所有生产者线程完成后，主线程开始消费队列中的事件
        // 循环读取队列，直到队列为空
        do {
            int counter = 0;
            Event event;
            // 不断从队列中取出已到期的事件
            // poll() 返回 null 表示队列为空或没有到期的事件
            do {
                event = queue.poll();
                if (event != null) {
                    counter++;
                }
            } while (event != null);

            // 打印本轮读取的事件数量
            System.out.printf("At %s you have read %d events\n", new Date(), counter);

            // 休眠500毫秒后继续下一轮读取
            // 这样可以分批看到不同延迟的事件逐渐到期
            TimeUnit.MILLISECONDS.sleep(500);
        } while (queue.size() > 0);

        System.out.println("Main: All events have been processed");
    }
}
