package com.ssitao.code.thread.code16.core;

import com.ssitao.code.thread.code16.task.Consumer;
import com.ssitao.code.thread.code16.task.EventStorage;
import com.ssitao.code.thread.code16.task.Producer;

/**
 * Code16: 使用wait()/notify()实现生产者-消费者模式
 *
 * 本示例演示Java中经典的线程通信模式：生产者-消费者问题。
 *
 * 【模式说明】
 * 生产者-消费者模式用于解决生产速度和消费速度不匹配的问题。
 * 通过一个缓冲区(EventStorage)解耦生产者和消费者，两者无需直接交互。
 *
 * 【角色分工】
 * - Producer(生产者)：向存储区添加事件，共100个
 * - Consumer(消费者)：从存储区取出事件，共100个
 * - EventStorage(缓冲区)：容量为10，平衡生产和消费速度
 *
 * 【执行流程】
 * 1. 创建共享的EventStorage（缓冲区容量10）
 * 2. 创建1个Producer线程和1个Consumer线程
 * 3. Producer生产100个事件
 * 4. Consumer消费100个事件
 * 5. 两者协调工作：当缓冲区满时生产者等待，当缓冲区空时消费者等待
 *
 * 【线程协调机制】
 * - wait()/notify()：基于Object类的线程通信方法
 * - 必须先获取对象的监视器锁（synchronized）
 * - wait()会释放锁，notify()不会立即释放
 *
 * 【运行结果】
 * 最终：存储区为空（100个生产，100个消费）
 * 过程：生产和消费交替进行，保持平衡
 */
public class Main {
    public static void main(String[] args) {
        // 创建共享的事件存储区（缓冲区容量为10）
        EventStorage storage = new EventStorage();

        // 创建生产者线程：负责向存储区添加100个事件
        Producer producer = new Producer(storage);
        Thread thread1 = new Thread(producer, "Producer");

        // 创建消费者线程：负责从存储区消费100个事件
        Consumer consumer = new Consumer(storage);
        Thread thread2 = new Thread(consumer, "Consumer");

        // 启动两个线程
        // 它们会自动协调：当缓冲区满时生产者等待，空时消费者等待
        thread2.start();  // 先启动消费者
        thread1.start();  // 再启动生产者
    }
}