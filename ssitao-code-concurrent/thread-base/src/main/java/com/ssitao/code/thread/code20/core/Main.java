package com.ssitao.code.thread.code20.core;


import com.ssitao.code.thread.code20.task.Consumer;
import com.ssitao.code.thread.code20.task.Producer;
import com.ssitao.code.thread.code20.utils.Buffer;
import com.ssitao.code.thread.code20.utils.FileMock;

/**
 * 主启动类
 * 演示使用Lock和Condition实现生产者-消费者问题
 */
public class Main {
    public static void main(String[] args) {
        // 创建一个文件模拟对象，包含101行数据，每行10个字符
        FileMock mock = new FileMock(101, 10);

        // 创建一个缓冲对象，最多可以缓存20行数据
        Buffer buffer = new Buffer(20);

        // 创建一个生产者对象，从文件读取数据并放入缓冲区
        Producer producer = new Producer(mock, buffer);
        Thread threadProducer = new Thread(producer, "Producer");

        // 创建三个消费者对象，从缓冲区读取数据并处理
        Consumer consumers[] = new Consumer[3];
        Thread threadConsumers[] = new Thread[3];

        for (int i = 0; i < 3; i++) {
            consumers[i] = new Consumer(buffer);
            threadConsumers[i] = new Thread(consumers[i], "Consumer " + i);
        }

        // 启动生产者和消费者线程
        // 生产者开始从文件读取并放入缓冲区
        threadProducer.start();
        // 三个消费者同时开始从缓冲区取数据处理
        for (int i = 0; i < 3; i++) {
            threadConsumers[i].start();
        }
    }
}
