package com.ssitao.code.disruptor.example;

import com.lmax.disruptor.EventFactory;

/**
 * 交易事件工厂
 */
public class TradeEventFactory implements EventFactory<TradeEvent> {

    @Override
    public TradeEvent newInstance() {
        return new TradeEvent();
    }
}
