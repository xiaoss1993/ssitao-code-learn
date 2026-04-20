package com.ssitao.code.disruptor.example;

import com.lmax.disruptor.EventFactory;

/**
 * 事件工厂 - 用于创建事件对象
 * Disruptor 预分配事件对象以避免 GC 压力
 */
public class LongEventFactory implements EventFactory<LongEvent> {

    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
