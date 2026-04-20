package com.ssitao.code.disruptor.example;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 简单示例：单生产者-单消费者
 * 演示 Disruptor 最基本的使用方式
 *
 * Disruptor 3.3.7 API
 */
public class SimpleExample {

    public static void main(String[] args) {
        // 1. 创建事件工厂
        LongEventFactory factory = new LongEventFactory();

        // 2. 设置 RingBuffer 大小（必须为2的幂）
        int bufferSize = 1024;

        // 3. 创建 Disruptor（3.3.7 简洁API）
        Executor executor = Executors.newSingleThreadExecutor();
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, executor);

        // 4. 设置消费者（事件处理器）
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            System.out.println("消费事件: " + event.getValue());
        });

        // 5. 启动 Disruptor
        disruptor.start();

        // 6. 获取 RingBuffer 用于发布事件
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        // 7. 发布事件
        for (long i = 0; i < 10; i++) {
            long sequence = ringBuffer.next();
            try {
                LongEvent event = ringBuffer.get(sequence);
                event.setValue(i);
            } finally {
                ringBuffer.publish(sequence);
            }
        }

        // 8. 关闭 Disruptor
        disruptor.shutdown();
        ((ExecutorService) executor).shutdown();

        System.out.println("简单示例完成!");
    }
}
