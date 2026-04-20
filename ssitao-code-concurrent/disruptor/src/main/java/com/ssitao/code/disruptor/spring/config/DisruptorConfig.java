package com.ssitao.code.disruptor.spring.config;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Spring 配置类 - 管理 Disruptor 生命周期
 */
@Configuration
public class DisruptorConfig {

    /**
     * 定义 OrderEvent 事件工厂
     */
    @Bean
    public OrderEventFactory orderEventFactory() {
        return new OrderEventFactory();
    }

    /**
     * 创建订单事件 Disruptor
     */
    @Bean
    public Disruptor<OrderEvent> orderDisruptor(OrderEventFactory factory) {
        int bufferSize = 4096;
        Executor executor = Executors.newFixedThreadPool(4);
        Disruptor<OrderEvent> disruptor = new Disruptor<>(factory, bufferSize, executor);

        // 设置消费者链
        disruptor.handleEventsWith(orderValidatorHandler())
                 .then(orderValidatorHandler())
                 .then(orderMatcherHandler())
                 .then(orderExecutorHandler());

        disruptor.start();
        return disruptor;
    }

    /**
     * 获取 RingBuffer
     */
    @Bean
    public RingBuffer<OrderEvent> orderRingBuffer(Disruptor<OrderEvent> orderDisruptor) {
        return orderDisruptor.getRingBuffer();
    }

    @Bean
    public EventHandler<OrderEvent> orderValidatorHandler() {
        return (event, sequence, endOfBatch) -> {
            System.out.println("[Validator] 处理订单: " + event.getOrderId());
            if (event.getPrice() <= 0) {
                event.setRejected(true);
                event.setRejectReason("Invalid price");
            }
        };
    }

    @Bean
    public EventHandler<OrderEvent> orderRiskHandler() {
        return (event, sequence, endOfBatch) -> {
            if (!event.isRejected()) {
                System.out.println("[RiskCheck] 检查订单: " + event.getOrderId());
                if (event.getQuantity() > 10000) {
                    event.setRejected(true);
                    event.setRejectReason("Position limit exceeded");
                }
            }
        };
    }

    @Bean
    public EventHandler<OrderEvent> orderMatcherHandler() {
        return (event, sequence, endOfBatch) -> {
            if (!event.isRejected()) {
                System.out.println("[Matcher] 匹配订单: " + event.getOrderId());
                event.setMatched(true);
            }
        };
    }

    @Bean
    public EventHandler<OrderEvent> orderExecutorHandler() {
        return (event, sequence, endOfBatch) -> {
            if (event.isMatched()) {
                System.out.println("[Executor] 成交订单: " + event.getOrderId());
            } else if (event.isRejected()) {
                System.out.println("[Executor] 拒绝订单: " + event.getOrderId() + ", 原因: " + event.getRejectReason());
            }
        };
    }
}