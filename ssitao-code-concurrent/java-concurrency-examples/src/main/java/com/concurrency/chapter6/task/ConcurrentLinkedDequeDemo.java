package com.concurrency.chapter6.task;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * ConcurrentLinkedDeque非阻塞线程安全列表示例
 */
public class ConcurrentLinkedDequeDemo {

    public static void demo() throws InterruptedException {
        ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<>();

        // 生产者线程添加元素
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                String item = "Item-" + i;
                list.add(item);
                System.out.println("添加: " + item);
            }
        }, "Producer");

        // 消费者线程获取元素
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String item = list.pollFirst();
                if (item != null) {
                    System.out.println("获取: " + item);
                }
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("剩余元素: " + list.size());
    }
}
