package com.ssitao.code.thread.code27.core;


import com.ssitao.code.thread.code27.task.Consumer;
import com.ssitao.code.thread.code27.task.Producer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * 代码27: Exchanger数据交换器演示
 *
 * 本示例演示了Java并发包中Exchanger的使用方法。
 *
 * Exchanger核心概念：
 * - Exchanger<E>是一种用于两个线程之间交换数据的同步工具
 * - 它有一个泛型参数E，表示交换的数据类型
 * - 当一个线程调用exchange()方法时，会阻塞直到另一个线程也调用exchange()
 * - 然后两个线程的数据会互换
 *
 * 典型应用场景：
 * - 生产者-消费者模式：生产者将数据装满buffer后与消费者的空buffer交换
 * - 遗传算法：两个染色体交换数据产生新的个体
 * - 其他需要线程间数据交换的场景
 *
 * 执行流程：
 * 1. 创建两个初始为空的buffer（生产者用buffer1，消费者用buffer2）
 * 2. 启动生产者线程，向buffer1添加数据
 * 3. 启动消费者线程，从buffer中取出数据
 * 4. 当buffer满时，双方交换buffer（生产者获得空buffer继续生产，消费者获得满buffer继续消费）
 * 5. 重复交换，直到所有100个事件都被生产和消费
 *
 * 注意：
 * - 两个线程必须共享同一个Exchanger实例
 * - 交换的是缓冲区的引用，而不是复制数据
 * - 如果只有一个线程调用exchange()，该线程会一直阻塞
 */
public class Main {
    public static void main(String[] args) {
        // 创建两个缓冲区
        // buffer1由生产者使用：初始为空，装满数据后与消费者交换
        // buffer2由消费者使用：初始为空，获得生产者的满buffer后消费
        List<String> buffer1 = new ArrayList<>();
        List<String> buffer2 = new ArrayList<>();

        // 创建一个Exchanger，用于两个线程之间交换List<String>类型的缓冲区
        Exchanger<List<String>> exchanger = new Exchanger<>();

        // 创建生产者和消费者任务
        // 生产者向buffer1添加数据，满10个后调用exchanger.exchange()交换
        // 消费者从buffer（交换后获得的）中取出数据消费
        Producer producer = new Producer(exchanger, buffer1);
        Consumer consumer = new Consumer(exchanger, buffer2);

        // 创建并启动生产者和消费者线程
        // 启动后，两者会通过Exchanger交替交换缓冲区
        Thread threadProducer = new Thread(producer, "Producer");
        Thread threadConsumer = new Thread(consumer, "Consumer");

        threadProducer.start();
        threadConsumer.start();

        // 等待两个线程执行完毕
        // 由于每个线程都要循环10次交换，生产者和消费者会交替执行
        try {
            threadProducer.join();
            threadConsumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main: All tasks finished.");
    }
}