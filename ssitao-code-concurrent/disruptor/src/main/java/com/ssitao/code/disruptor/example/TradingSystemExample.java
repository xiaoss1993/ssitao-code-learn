package com.ssitao.code.disruptor.example;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 真实场景示例：交易所订单处理系统
 *
 * 订单处理流程：
 * 接收订单 → 验证 → 风控检查 → 持仓更新 → 匹配引擎 → 成交回报
 *
 * Disruptor 3.3.7 API
 */
public class TradingSystemExample {

    public static void main(String[] args) throws InterruptedException {
        // 统计
        AtomicLong orderReceived = new AtomicLong(0);
        AtomicLong orderValidated = new AtomicLong(0);
        AtomicLong orderRiskChecked = new AtomicLong(0);
        AtomicLong orderMatched = new AtomicLong(0);
        AtomicLong tradesExecuted = new AtomicLong(0);

        Executor executor = Executors.newFixedThreadPool(8);
        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                new OrderEventFactory(),
                4096,
                executor
        );

        // 1. 订单验证
        EventHandler<OrderEvent> validator = (event, sequence, endOfBatch) -> {
            orderValidated.incrementAndGet();
            if (event.getPrice() <= 0 || event.getQuantity() <= 0) {
                event.setRejected(true);
                event.setRejectReason("Invalid price or quantity");
            }
        };

        // 2. 风险检查
        EventHandler<OrderEvent> riskChecker = (event, sequence, endOfBatch) -> {
            if (!event.isRejected()) {
                orderRiskChecked.incrementAndGet();
                if (event.getQuantity() > 10000) {
                    event.setRejected(true);
                    event.setRejectReason("Position limit exceeded");
                }
            }
        };

        // 3. 持仓更新
        EventHandler<OrderEvent> positionUpdater = (event, sequence, endOfBatch) -> {
            if (!event.isRejected()) {
                // 更新持仓
            }
        };

        // 4. 订单匹配引擎
        EventHandler<OrderEvent> matcher = (event, sequence, endOfBatch) -> {
            if (!event.isRejected()) {
                orderMatched.incrementAndGet();
                event.setMatched(true);
            }
        };

        // 5. 成交回报
        EventHandler<OrderEvent> tradeExecutor = (event, sequence, endOfBatch) -> {
            if (event.isMatched()) {
                tradesExecuted.incrementAndGet();
            }
        };

        // 设置处理链
        disruptor.handleEventsWith(validator)
                .then(riskChecker)
                .then(positionUpdater)
                .then(matcher)
                .then(tradeExecutor);

        disruptor.start();
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();

        // 启动多个生产者模拟接收订单
        int producerCount = 2;
        int ordersPerProducer = 50000;
        Thread[] producers = new Thread[producerCount];

        for (int p = 0; p < producerCount; p++) {
            producers[p] = new Thread(() -> {
                for (int i = 0; i < ordersPerProducer; i++) {
                    long sequence = ringBuffer.next();
                    try {
                        OrderEvent event = ringBuffer.get(sequence);
                        event.setOrderId("ORD-" + System.nanoTime() + "-" + i);
                        event.setSymbol(new String[]{"AAPL", "GOOGL", "MSFT", "AMZN"}[i % 4]);
                        event.setPrice(100.0 + (i % 100) * 0.01);
                        event.setQuantity(10 + (i % 100));
                        event.setBuy(true);
                        event.setRejected(false);
                        event.setMatched(false);
                        orderReceived.incrementAndGet();
                    } finally {
                        ringBuffer.publish(sequence);
                    }
                }
            });
        }

        // 启动生产者并计时
        long startTime = System.currentTimeMillis();
        for (Thread producer : producers) {
            producer.start();
        }

        for (Thread producer : producers) {
            producer.join();
        }

        Thread.sleep(2000);
        long endTime = System.currentTimeMillis();

        System.out.println("\n========== 交易系统处理结果 ==========");
        System.out.println("接收订单数: " + orderReceived.get());
        System.out.println("验证通过数: " + orderValidated.get());
        System.out.println("风险检查通过数: " + orderRiskChecked.get());
        System.out.println("匹配成功数: " + orderMatched.get());
        System.out.println("成交数: " + tradesExecuted.get());
        System.out.println("总耗时: " + (endTime - startTime) + " ms");
        System.out.println("吞吐量: " + (orderReceived.get() * 1000 / (endTime - startTime)) + " orders/sec");

        disruptor.shutdown();
        ((ExecutorService) executor).shutdown();
    }
}
