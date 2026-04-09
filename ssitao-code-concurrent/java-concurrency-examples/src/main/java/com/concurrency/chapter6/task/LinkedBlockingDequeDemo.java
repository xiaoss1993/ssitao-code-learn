package com.concurrency.chapter6.task;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * LinkedBlockingDeque阻塞线程安全列表示例
 */
public class LinkedBlockingDequeDemo {

    public static void demo() throws InterruptedException {
        BlockingDeque<String> deque = new LinkedBlockingDeque<>(3); // 容量为3

        // 生产者
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    String item = "Item-" + i;
                    deque.putFirst(item); // 阻塞直到有空间
                    System.out.println("添加: " + item + " (队列大小: " + deque.size() + ")");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Producer");

        // 消费者
        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(500);
                for (int i = 0; i < 3; i++) {
                    String item = deque.takeLast(); // 阻塞直到有元素
                    System.out.println("获取: " + item + " (队列大小: " + deque.size() + ")");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }
}
