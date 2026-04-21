package com.ssitao.code.designpattern.producer.consumer.web;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Web应用中的生产者-消费者模式
 *
 * 应用场景：
 * 1. 异步任务队列 - 邮件/短信发送
 * 2. 日志收集 - ELK架构
 * 3. 消息队列 - Kafka/RocketMQ
 * 4. 批量处理 - 批量入库
 * 5. 缓存更新 - 异步刷新缓存
 */
public class WebProducerConsumerDemo {

    public static void main(String[] args) {
        System.out.println("=== Web生产者-消费者示例 ===\n");

        // 1. 异步任务队列
        System.out.println("1. 异步任务队列");
        asyncTaskDemo();

        // 2. 日志收集
        System.out.println("\n2. 日志收集");
        logCollectionDemo();

        // 3. 批量处理
        System.out.println("\n3. 批量处理");
        batchProcessDemo();
    }

    /**
     * 异步任务队列示例 - 邮件发送
     */
    private static void asyncTaskDemo() {
        // 创建任务队列
        BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>(100);

        // 任务处理线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 启动消费者（处理任务）
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                while (true) {
                    try {
                        Task task = taskQueue.take();
                        System.out.println("处理任务: " + task.getType());
                        // 模拟处理
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
        }

        // 模拟生产者（接收请求）
        for (int i = 1; i <= 5; i++) {
            Task task = new Task("email", "邮件-" + i);
            try {
                taskQueue.put(task);
                System.out.println("添加任务: " + task.getType());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 日志收集示例 - 类似ELK架构
     */
    private static void logCollectionDemo() {
        BlockingQueue<LogMessage> logQueue = new LinkedBlockingQueue<>(1000);

        // 日志收集器（生产者）
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                LogMessage log = new LogMessage("INFO", "用户操作-" + i);
                try {
                    logQueue.put(log);
                    System.out.println("收集日志: " + log.getMessage());
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "LogProducer");

        // 日志写入器（消费者）
        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    LogMessage log = logQueue.take();
                    System.out.println("写入日志: [" + log.getLevel() + "] " + log.getMessage());
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "LogConsumer");

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.interrupt();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量处理示例 - 订单批量入库
     */
    private static void batchProcessDemo() {
        BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>(100);

        // 订单接收线程（生产者）
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                Order order = new Order("ORD-" + i, 100.0 * i);
                try {
                    orderQueue.put(order);
                    System.out.println("接收订单: " + order.getOrderId());
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "OrderProducer");

        // 批量处理线程（消费者）
        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    // 收集一批订单
                    OrderBatch batch = new OrderBatch();
                    while (batch.size() < 3 && !orderQueue.isEmpty()) {
                        Order order = orderQueue.poll(1, TimeUnit.SECONDS);
                        if (order != null) {
                            batch.add(order);
                        }
                    }

                    if (batch.size() > 0) {
                        System.out.println("批量处理: " + batch.size() + " 个订单");
                        // 模拟批量入库
                        Thread.sleep(500);
                    }

                    if (orderQueue.isEmpty() && batch.size() == 0) {
                        break;
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "OrderConsumer");

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 任务
 */
class Task {
    private String type;
    private String content;

    public Task(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() { return type; }
    public String getContent() { return content; }
}

/**
 * 日志消息
 */
class LogMessage {
    private String level;
    private String message;
    private long timestamp;

    public LogMessage(String level, String message) {
        this.level = level;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getLevel() { return level; }
    public String getMessage() { return message; }
    public long getTimestamp() { return timestamp; }
}

/**
 * 订单
 */
class Order {
    private String orderId;
    private double amount;

    public Order(String orderId, double amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getOrderId() { return orderId; }
    public double getAmount() { return amount; }
}

/**
 * 订单批次
 */
class OrderBatch {
    private java.util.List<Order> orders = new java.util.ArrayList<>();

    public void add(Order order) {
        orders.add(order);
    }

    public int size() {
        return orders.size();
    }
}

/**
 * Spring中的生产者-消费者使用示例
 *
 * 1. @Async 异步任务
 * @Service
 * public class EmailService {
 *     @Async("taskExecutor")
 *     public void sendEmailAsync(Email email) {
 *         // 异步发送
 *     }
 * }
 *
 * 2. ThreadPoolTaskExecutor
 * @Configuration
 * public class TaskPoolConfig {
 *     @Bean("taskExecutor")
 *     public Executor taskExecutor() {
 *         ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
 *         executor.setCorePoolSize(10);
 *         executor.setQueueCapacity(200);
 *         executor.setThreadNamePrefix("async-");
 *         return executor;
 *     }
 * }
 *
 * 3. CompletableFuture
 * public CompletableFuture<Result> processAsync() {
 *     return CompletableFuture.supplyAsync(() -> {
 *         // 处理
 *         return result;
 *     });
 * }
 *
 * 4. 消息队列集成
 * @RabbitListener(queues = "orderQueue")
 * public void handleOrder(Order order) {
 *     // 消费订单消息
 * }
 */
