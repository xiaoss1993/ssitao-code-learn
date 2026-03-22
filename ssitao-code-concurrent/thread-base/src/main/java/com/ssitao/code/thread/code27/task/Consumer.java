package com.ssitao.code.thread.code27.task;

import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * 消费者类 - Exchanger数据交换演示
 *
 * Exchanger工作原理：
 * - 用于两个线程之间交换彼此的数据缓冲区
 * - 当一方调用exchange()时，会阻塞直到另一方也调用exchange()
 * - 然后两个缓冲区的引用会互换，实现数据交换
 *
 * 本示例的应用场景（生产者-消费者模式）：
 * - 生产者：生产数据，满10个后与消费者交换缓冲区
 * - 消费者：消费数据（从buffer中取出），处理完后清空buffer，与生产者交换
 *
 * 交换过程：
 * 1. 初始时，生产者的buffer有数据，消费者的buffer为空
 * 2. 消费者调用exchange(buffer)，阻塞等待
 * 3. 生产者调用exchange(buffer)，两者交换
 * 4. 交换后：生产者获得空buffer继续生产，消费者获得装满数据的buffer继续消费
 *
 * 这种模式的优点：
 * - 生产者和消费者可以并发运行，提高吞吐量
 * - 无需锁或阻塞队列，通过交换缓冲区实现数据传递
 * - 减少了线程间的竞争
 */
public class Consumer implements Runnable {
    /**
     * 消费者消费数据的地方，也是与生产者交换数据的地方
     */
    private List<String> buffer;
    /**
     * 同步生产者与消费者交换数据的交换对象
     */
    private final Exchanger<List<String>> exchanger;

    /**
     * 构造函数，初始化属性
     *
     * @param exchanger 数据的交换对象
     * @param buffer    数据存储对象（初始为空）
     */
    public Consumer(Exchanger<List<String>> exchanger, List<String> buffer) {
        this.exchanger = exchanger;
        this.buffer = buffer;
    }

    /**
     * 线程执行入口，消费100个事件
     *
     * 执行流程：
     * 1. 调用exchanger.exchange(buffer)与生产者交换数据
     *    - 阻塞等待，直到生产者也想交换
     *    - 交换完成后，当前buffer（空）给生产者，获得装有新数据的buffer
     * 2. 从buffer中取出10个事件进行处理（打印）
     * 3. 重复10次，共消费100个事件
     *
     * 关键点：
     * - exchange()是阻塞的，只有双方都调用时才会完成交换
     * - 交换的是整个buffer的引用，而不是buffer中的数据
     */
    @Override
    public void run() {
        int cycle = 1;

        // 共循环10次，每次处理10个事件，总计100个事件
        for (int i = 0; i < 10; i++) {
            System.out.printf("Consumer: Cycle %d\n", cycle);

            try {
                // 与生产者交换数据
                // 交换前：buffer是空的（或上一轮消费完剩下的）
                // 交换后：buffer变成生产者那边交换过来的，装有10个新数据
                // 如果生产者还没生产完，当前线程会在这里阻塞
                buffer = exchanger.exchange(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 打印当前buffer中的事件数量
            System.out.printf("Consumer: %d\n", buffer.size());

            // 从buffer中取出并消费10个事件
            for (int j = 0; j < 10; j++) {
                String message = buffer.get(0); // 获取第一个元素（不移除）
                System.out.printf("Consumer: %s\n", message);
                buffer.remove(0); // 取出后从buffer中移除
            }

            cycle++;
        }
    }
}