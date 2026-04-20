package com.ssitao.code.disruptor.example;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 多消费者示例 - 演示消费者依赖关系
 *
 * 依赖关系：
 *           ┌─▶ enricher  ─┐
 * Producer ─┼─▶ persister ─┼─▶ notifier ─▶ 最终处理
 *           └─▶ notifier  ─┘
 *
 * Disruptor 3.3.7 API
 */
public class MultiConsumerExample {

    public static void main(String[] args) throws InterruptedException {
        // 1. 创建 Disruptor
        int bufferSize = 1024;
        Executor executor = Executors.newFixedThreadPool(5);
        Disruptor<TradeEvent> disruptor = new Disruptor<>(new TradeEventFactory(), bufferSize, executor);

        // 2. 创建处理器
        AtomicLong enrichCount = new AtomicLong(0);
        AtomicLong persistCount = new AtomicLong(0);
        AtomicLong notifyCount = new AtomicLong(0);

        EventHandler<TradeEvent> enricher = (event, sequence, endOfBatch) -> {
            enrichCount.incrementAndGet();
            // 丰富交易数据（如添加时间戳、生成交易ID等）
        };

        EventHandler<TradeEvent> persister = (event, sequence, endOfBatch) -> {
            persistCount.incrementAndGet();
            // 持久化交易数据
        };

        EventHandler<TradeEvent> notifier = (event, sequence, endOfBatch) -> {
            notifyCount.incrementAndGet();
            // 通知相关系统
        };

        // 3. 设置依赖关系
        // enricher 和 persister 并行处理，完成后串行执行 notifier
        disruptor.handleEventsWith(enricher, persister)
                 .then(notifier);

        // 4. 启动
        disruptor.start();
        RingBuffer<TradeEvent> ringBuffer = disruptor.getRingBuffer();

        // 5. 发布事件
        int eventCount = 1000;
        for (int i = 0; i < eventCount; i++) {
            long sequence = ringBuffer.next();
            try {
                TradeEvent event = ringBuffer.get(sequence);
                event.setSymbol("AAPL");
                event.setPrice(100.0 + i);
                event.setQuantity(10);
            } finally {
                ringBuffer.publish(sequence);
            }
        }

        // 6. 等待处理完成
        Thread.sleep(500);

        System.out.println("enricher 处理: " + enrichCount.get());
        System.out.println("persister 处理: " + persistCount.get());
        System.out.println("notifier 处理: " + notifyCount.get());

        disruptor.shutdown();
        ((ExecutorService) executor).shutdown();
    }
}
