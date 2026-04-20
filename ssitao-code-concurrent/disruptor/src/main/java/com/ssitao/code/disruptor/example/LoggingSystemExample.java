package com.ssitao.code.disruptor.example;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 真实场景示例：日志处理系统
 *
 * 处理流程：
 * 日志 → 解析 → 过滤 → 写入文件/发送告警 → 指标统计
 *
 * Disruptor 3.3.7 API
 */
public class LoggingSystemExample {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static void main(String[] args) throws InterruptedException {
        AtomicLong logReceived = new AtomicLong(0);
        AtomicLong logParsed = new AtomicLong(0);
        AtomicLong logFiltered = new AtomicLong(0);
        AtomicLong logWritten = new AtomicLong(0);
        AtomicLong alertsSent = new AtomicLong(0);
        AtomicLong metricsUpdated = new AtomicLong(0);

        Executor executor = Executors.newFixedThreadPool(6);
        Disruptor<LogEvent> disruptor = new Disruptor<>(
                new LogEventFactory(),
                8192,
                executor
        );

        // 1. 日志解析
        EventHandler<LogEvent> parser = (event, sequence, endOfBatch) -> {
            logParsed.incrementAndGet();
            String level = event.getLevel().toUpperCase();
            if (level.contains("ERROR") || level.contains("FATAL")) {
                event.setAlertLevel(true);
            }
        };

        // 2. 日志过滤/分类
        EventHandler<LogEvent> filter = (event, sequence, endOfBatch) -> {
            logFiltered.incrementAndGet();
            if (event.getLevel().contains("DEBUG")) {
                event.setCategory("DEBUG");
            } else if (event.getLevel().contains("INFO")) {
                event.setCategory("INFO");
            } else if (event.getLevel().contains("WARN")) {
                event.setCategory("WARN");
            } else if (event.getLevel().contains("ERROR") || event.getLevel().contains("FATAL")) {
                event.setCategory("ERROR");
            }
        };

        // 3. 写入文件
        EventHandler<LogEvent> fileWriter = (event, sequence, endOfBatch) -> {
            logWritten.incrementAndGet();
        };

        // 4. 发送告警（仅针对 ERROR/FATAL）
        EventHandler<LogEvent> alertSender = (event, sequence, endOfBatch) -> {
            if (event.isAlertLevel()) {
                alertsSent.incrementAndGet();
            }
        };

        // 5. 指标统计
        EventHandler<LogEvent> metrics = (event, sequence, endOfBatch) -> {
            metricsUpdated.incrementAndGet();
        };

        // 设置处理链
        disruptor.handleEventsWith(parser)
                .then(filter)
                .then(fileWriter, alertSender)
                .then(metrics);

        disruptor.start();
        RingBuffer<LogEvent> ringBuffer = disruptor.getRingBuffer();

        // 模拟日志生产者
        String[] levels = {"DEBUG", "INFO", "INFO", "INFO", "WARN", "ERROR", "INFO", "INFO", "ERROR", "FATAL"};
        String[] messages = {
                "User login successful",
                "Database query executed",
                "Cache hit for key: user_session",
                "API request processed",
                "Slow query detected: 2.5s",
                "Connection timeout to service",
                "Request completed in 150ms",
                "Session refreshed",
                "Failed to send notification",
                "Critical system failure"
        };

        int producerCount = 3;
        int logsPerProducer = 10000;
        Thread[] producers = new Thread[producerCount];

        for (int p = 0; p < producerCount; p++) {
            producers[p] = new Thread(() -> {
                for (int i = 0; i < logsPerProducer; i++) {
                    long sequence = ringBuffer.next();
                    try {
                        LogEvent event = ringBuffer.get(sequence);
                        event.setTimestamp(LocalDateTime.now().format(FORMATTER));
                        event.setLevel(levels[i % levels.length]);
                        event.setMessage(messages[i % messages.length] + " #" + i);
                        event.setThreadName(Thread.currentThread().getName());
                        event.setAlertLevel(false);
                        logReceived.incrementAndGet();
                    } finally {
                        ringBuffer.publish(sequence);
                    }
                }
            });
        }

        long startTime = System.currentTimeMillis();
        for (Thread producer : producers) {
            producer.start();
        }

        for (Thread producer : producers) {
            producer.join();
        }

        Thread.sleep(1000);
        long endTime = System.currentTimeMillis();

        System.out.println("\n========== 日志处理系统结果 ==========");
        System.out.println("接收日志数: " + logReceived.get());
        System.out.println("解析日志数: " + logParsed.get());
        System.out.println("过滤日志数: " + logFiltered.get());
        System.out.println("写入文件数: " + logWritten.get());
        System.out.println("发送告警数: " + alertsSent.get());
        System.out.println("指标更新数: " + metricsUpdated.get());
        System.out.println("总耗时: " + (endTime - startTime) + " ms");
        System.out.println("吞吐量: " + (logReceived.get() * 1000 / (endTime - startTime)) + " logs/sec");

        disruptor.shutdown();
        ((ExecutorService) executor).shutdown();
    }
}
