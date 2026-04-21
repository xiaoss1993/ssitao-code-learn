package com.ssitao.code.designpattern.producer.consumer.waitnotify;

import java.util.LinkedList;
import java.util.Queue;

/**
 * wait/notify 实现生产者-消费者
 *
 * JDK5之前的方式：
 * - 使用Object的wait()和notify()
 * - 需要 synchronized 同步代码块
 */
public class WaitNotifyDemo {

    public static void main(String[] args) {
        System.out.println("=== wait/notify 实现 ===\n");

        // 创建共享容器
        SharedQueue<String> queue = new SharedQueue<>(5);

        // 启动生产者
        new Thread(new ProducerWaitNotify(queue), "Producer-1").start();
        new Thread(new ProducerWaitNotify(queue), "Producer-2").start();

        // 启动消费者
        new Thread(new ConsumerWaitNotify(queue), "Consumer-1").start();
        new Thread(new ConsumerWaitNotify(queue), "Consumer-2").start();

        // 运行3秒后停止
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 共享队列 - 使用wait/notify实现
 */
class SharedQueue<T> {
    private Queue<T> queue = new LinkedList<>();
    private int capacity;

    public SharedQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 添加元素 - 生产者调用
     */
    public synchronized void put(T item) throws InterruptedException {
        // 队列满则等待
        while (queue.size() >= capacity) {
            System.out.println(Thread.currentThread().getName() + " 队列满，等待...");
            wait();
        }

        // 添加元素
        queue.offer(item);
        System.out.println(Thread.currentThread().getName() + " 生产: " + item);

        // 唤醒消费者
        notifyAll();
    }

    /**
     * 取出元素 - 消费者调用
     */
    public synchronized T take() throws InterruptedException {
        // 队列空则等待
        while (queue.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + " 队列空，等待...");
            wait();
        }

        // 取出元素
        T item = queue.poll();
        System.out.println(Thread.currentThread().getName() + " 消费: " + item);

        // 唤醒生产者
        notifyAll();

        return item;
    }

    public synchronized int size() {
        return queue.size();
    }
}

/**
 * 生产者
 */
class ProducerWaitNotify implements Runnable {
    private final SharedQueue<String> queue;
    private int count = 0;

    public ProducerWaitNotify(SharedQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String item = "商品-" + (++count);
                queue.put(item);
                Thread.sleep((long) (Math.random() * 500));
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

/**
 * 消费者
 */
class ConsumerWaitNotify implements Runnable {
    private final SharedQueue<String> queue;

    public ConsumerWaitNotify(SharedQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                queue.take();
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
