package com.ssitao.code.disruptor.example;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 多生产者示例
 * 演示多个线程同时向 RingBuffer 发布事件
 *
 * Disruptor 3.3.7 API
 */
public class MultiProducerExample {

    public static void main(String[] args) throws InterruptedException {
        // 1. 创建事件工厂和 Disruptor
        LongEventFactory factory = new LongEventFactory();
        int bufferSize = 1024;
        Executor executor = Executors.newFixedThreadPool(4);
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, executor);

        // 2. 设置消费者
        AtomicLong consumedCount = new AtomicLong(0);
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            consumedCount.incrementAndGet();
        });

        // 3. 启动
        disruptor.start();
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        // 4. 多个生产者同时发布事件
        int producerCount = 3;
        int eventsPerProducer = 10000;
        Thread[] producers = new Thread[producerCount];

        for (int p = 0; p < producerCount; p++) {
            final int producerId = p;
            producers[p] = new Thread(() -> {
                for (long i = 0; i < eventsPerProducer; i++) {
                    long sequence = ringBuffer.next();
                    try {
                        LongEvent event = ringBuffer.get(sequence);
                        event.setValue(producerId * 1000000 + i);
                    } finally {
                        ringBuffer.publish(sequence);
                    }
                }
            });
        }

        // 5. 启动所有生产者并计时
        long startTime = System.nanoTime();
        for (Thread producer : producers) {
            producer.start();
        }

        // 6. 等待所有生产者完成
        for (Thread producer : producers) {
            producer.join();
        }

        // 7. 等待消费者处理完所有事件
        Thread.sleep(1000);
        long endTime = System.nanoTime();

        long totalEvents = (long) producerCount * eventsPerProducer;
        System.out.println("总发布事件数: " + totalEvents);
        System.out.println("总消费事件数: " + consumedCount.get());
        System.out.println("耗时: " + (endTime - startTime) / 1_000_000 + " ms");
        System.out.println("吞吐量: " + (totalEvents * 1_000_000_000L / (endTime - startTime)) + " events/sec");

        disruptor.shutdown();
        ((ExecutorService) executor).shutdown();
    }
}
