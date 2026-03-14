package com.ssitao.code.designpattern.producer.consumer.basic;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 生产者-消费者模式基础示例
 *
 * 核心思想：
 * 1. 生产者和消费者通过共享缓冲区进行通信
 * 2. 生产者将数据放入缓冲区，消费者从缓冲区取数据
 * 3. 缓冲区满了生产者等待，消费者取走后唤醒
 * 4. 缓冲区空了消费者等待，生产者放入后唤醒
 *
 * 优点：
 * 1. 解耦生产者和消费者
 * 2. 支持并发处理
 * 3. 平衡生产速度和消费速度
 */
public class ProducerConsumerBasicDemo {

    public static void main(String[] args) {
        System.out.println("=== 生产者-消费者模式基础示例 ===\n");

        // 创建共享队列
        BlockingQueue<Message> queue = new LinkedBlockingQueue<>(10);

        // 启动生产者
        new Thread(new Producer(queue), "Producer-1").start();
        new Thread(new Producer(queue), "Producer-2").start();

        // 启动消费者
        new Thread(new Consumer(queue), "Consumer-1").start();
        new Thread(new Consumer(queue), "Consumer-2").start();

        // 运行5秒后停止
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程结束");
    }
}

/**
 * 消息
 */
class Message {
    private int id;
    private String content;

    public Message(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() { return id; }
    public String getContent() { return content; }

    @Override
    public String toString() {
        return "Message[id=" + id + ", content='" + content + "']";
    }
}

/**
 * 生产者
 */
class Producer implements Runnable {
    private final BlockingQueue<Message> queue;
    private int count = 0;

    public Producer(BlockingQueue<Message> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 生产消息
                Message msg = new Message(++count, "消息-" + count);
                queue.put(msg);
                System.out.println(Thread.currentThread().getName() + " 生产: " + msg);

                // 模拟生产速度
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

/**
 * 消费者
 */
class Consumer implements Runnable {
    private final BlockingQueue<Message> queue;

    public Consumer(BlockingQueue<Message> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 消费消息
                Message msg = queue.take();
                System.out.println(Thread.currentThread().getName() + " 消费: " + msg);

                // 模拟消费速度
                Thread.sleep((long) (Math.random() * 1500));
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
