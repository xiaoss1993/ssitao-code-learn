package com.ssitao.code.thread.code16.task;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 事件存储类，模拟生产者-消费者模式中的缓冲区
 *
 * 【设计模式】生产者-消费者模式
 * - 生产者(Producer)：向存储区添加事件
 * - 消费者(Consumer)：从存储区取出事件处理
 * - 本类作为两者之间的缓冲区/共享队列
 *
 * 【线程安全机制】
 * - 使用synchronized保证方法互斥访问
 * - 使用wait()/notify()实现线程间的通信和协调
 *
 * 【容量控制】
 * - 最大容量：10个事件
 * - 满时：生产者必须等待消费者消费
 * - 空时：消费者必须等待生产者生产
 *
 * 【wait()/notify()说明】
 * - wait()：线程进入等待状态，释放对象锁
 * - notify()：唤醒一个在该对象上等待的线程
 * - 必须先获取对象锁才能调用，通常在条件不满足时调用wait()
 */
public class EventStorage {
    /**
     * 最大存储容量
     * 当存储的事件数量达到此值时，生产者必须等待
     */
    private int maxSize;

    /**
     * 存储事件的容器
     * 使用LinkedList实现，支持在头部和尾部高效添加/删除
     */
    private List<Date> storage;

    /**
     * 构造函数
     * 初始化存储区：最大容量10，初始为空
     */
    public EventStorage() {
        this.maxSize = 10;
        this.storage = new LinkedList<Date>();
    }

    /**
     * 生产事件（同步方法）
     *
     * 当存储区满时，生产者调用wait()进入等待状态，让出CPU和锁。
     * 当成功添加一个事件后，调用notify()唤醒可能等待的消费者。
     *
     * 【执行流程】
     * 1. 检查存储区是否已满
     * 2. 如果满了 → wait()等待，释放锁
     * 3. 如果没满 → 添加新事件，打印当前容量，notify()唤醒消费者
     *
     * 【注意】使用while而非if判断条件
     * 原因：wait()可能返回但条件仍不满足（如虚假唤醒），需重新检查
     */
    public synchronized void set() {
        // 使用while循环，防止虚假唤醒
        while (this.storage.size() == this.maxSize) {
            try {
                // 存储区满，生产者等待
                // wait()会释放对象锁，让消费者可以进入get()方法
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 存储区未满，添加新事件
        this.storage.add(new Date());
        System.out.printf("Set: %d\n", storage.size());

        // 唤醒一个等待的线程（通常是等待消费的消费者）
        notify();
    }

    /**
     * 消费事件（同步方法）
     *
     * 当存储区空时，消费者调用wait()进入等待状态，让出CPU和锁。
     * 当成功消费一个事件后，调用notify()唤醒可能等待的生产者。
     *
     * 【执行流程】
     * 1. 检查存储区是否为空
     * 2. 如果为空 → wait()等待，释放锁
     * 3. 如果不为空 → 消费(移除)一个事件，打印剩余容量，notify()唤醒生产者
     *
     * 【注意】使用while而非if判断条件
     * 原因：防止虚假唤醒，必须重新检查条件
     */
    public synchronized void get() {
        // 使用while循环，防止虚假唤醒
        while (this.storage.size() == 0) {
            try {
                // 存储区空，消费者等待
                // wait()会释放对象锁，让生产者可以进入set()方法
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 存储区不为空，消费一个事件
        // poll()移除并返回头部元素，如果队列为空返回null
        System.out.printf("Get: %d: %s\n", storage.size(), ((LinkedList<?>) storage).poll());

        // 唤醒一个等待的线程（通常是等待生产的生产者）
        notify();
    }
}
