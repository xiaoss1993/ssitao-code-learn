package com.ssitao.code.disruptor.example;

import com.lmax.disruptor.EventFactory;

/**
 * 日志事件工厂
 */
public class LogEventFactory implements EventFactory<LogEvent> {

    @Override
    public LogEvent newInstance() {
        return new LogEvent();
    }
}
