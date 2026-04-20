package com.ssitao.code.disruptor.spring.service;

import com.lmax.disruptor.RingBuffer;
import com.ssitao.code.disruptor.spring.config.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单服务 - 通过 RingBuffer 发布订单事件
 */
@Service
public class OrderService {

    @Autowired
    private RingBuffer<OrderEvent> orderRingBuffer;

    /**
     * 下单 - 将订单发布到 Disruptor 处理
     */
    public void placeOrder(String orderId, String symbol, double price, int quantity) {
        long sequence = orderRingBuffer.next();
        try {
            OrderEvent event = orderRingBuffer.get(sequence);
            event.setOrderId(orderId);
            event.setSymbol(symbol);
            event.setPrice(price);
            event.setQuantity(quantity);
            event.setRejected(false);
            event.setMatched(false);
        } finally {
            orderRingBuffer.publish(sequence);
        }
    }

    /**
     * 批量下单
     */
    public void placeOrders(int count) {
        for (int i = 0; i < count; i++) {
            placeOrder(
                "ORD-" + System.nanoTime() + "-" + i,
                new String[]{"AAPL", "GOOGL", "MSFT", "AMZN"}[i % 4],
                100.0 + (i % 100) * 0.01,
                10 + (i % 100)
            );
        }
    }
}