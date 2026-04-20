package com.ssitao.code.disruptor.example;

/**
 * 事件对象 - 用于在 RingBuffer 中传递的数据
 */
public class LongEvent {
    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
