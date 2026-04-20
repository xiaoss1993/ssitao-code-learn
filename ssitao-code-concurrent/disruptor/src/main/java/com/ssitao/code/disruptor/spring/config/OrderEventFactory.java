package com.ssitao.code.disruptor.spring.config;

import com.lmax.disruptor.EventFactory;

/**
 * 订单事件工厂
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {

    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}