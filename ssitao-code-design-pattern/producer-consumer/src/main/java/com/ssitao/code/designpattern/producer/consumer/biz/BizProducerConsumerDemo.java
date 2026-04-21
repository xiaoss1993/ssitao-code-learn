package com.ssitao.code.designpattern.producer.consumer.biz;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 业务场景中的生产者-消费者模式
 *
 * 典型应用：
 * 1. 秒杀系统 - 限流下单
 * 2. 日志处理 - 异步写日志
 * 3. 缓存同步 - 缓存更新
 * 4. 文件处理 - 大文件分片上传
 * 5. 数据同步 - 多数据源同步
 */
public class BizProducerConsumerDemo {

    public static void main(String[] args) {
        System.out.println("=== 业务场景生产者-消费者 ===\n");

        // 1. 秒杀系统
        System.out.println("1. 秒杀系统");
        seckillDemo();

        // 2. 日志处理
        System.out.println("\n2. 日志处理");
        logHandleDemo();

        // 3. 订单处理
        System.out.println("\n3. 订单处理");
        orderProcessDemo();
    }

    /**
     * 秒杀系统示例
     */
    private static void seckillDemo() {
        // 库存队列
        BlockingQueue<SeckillItem> stockQueue = new LinkedBlockingQueue<>(100);

        // 模拟库存商品
        for (int i = 1; i <= 10; i++) {
            try {
                stockQueue.put(new SeckillItem("商品-" + i, 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 秒杀线程（消费者）
        ExecutorService seckillExecutor = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 5; i++) {
            final int userId = i;
            seckillExecutor.submit(() -> {
                while (true) {
                    try {
                        SeckillItem item = stockQueue.poll(1, TimeUnit.SECONDS);
                        if (item != null) {
                            System.out.println("用户" + userId + " 秒杀成功: " + item.getName());
                        } else {
                            System.out.println("用户" + userId + " 秒杀失败，商品已抢光");
                            break;
                        }
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
        }

        seckillExecutor.shutdown();
        try {
            seckillExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 日志处理示例
     */
    private static void logHandleDemo() {
        // 日志队列
        BlockingQueue<LogEntry> logQueue = new LinkedBlockingQueue<>(1000);

        // 日志生产者（多个线程产生日志）
        ExecutorService producerExecutor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            final int threadId = i;
            producerExecutor.submit(() -> {
                for (int j = 1; j <= 5; j++) {
                    LogEntry log = new LogEntry("INFO", "业务日志-" + j);
                    try {
                        logQueue.put(log);
                        System.out.println("产生日志: " + log.getMessage());
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
        }

        // 日志消费者（单个线程处理）
        ExecutorService consumerExecutor = Executors.newSingleThreadExecutor();
        consumerExecutor.submit(() -> {
            while (true) {
                try {
                    LogEntry log = logQueue.take();
                    System.out.println("处理日志: [" + log.getLevel() + "] " + log.getMessage());
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        producerExecutor.shutdown();
        try {
            producerExecutor.awaitTermination(2, TimeUnit.SECONDS);
            Thread.sleep(1000);
            consumerExecutor.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订单处理示例
     */
    private static void orderProcessDemo() {
        // 订单队列
        BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>(100);
        AtomicInteger processedCount = new AtomicInteger(0);

        // 订单生产者
        ExecutorService producerExecutor = Executors.newFixedThreadPool(2);
        for (int i = 1; i <= 6; i++) {
            final int orderId = i;
            producerExecutor.submit(() -> {
                try {
                    Order order = new Order("ORD-" + orderId, 100.0 * orderId);
                    orderQueue.put(order);
                    System.out.println("创建订单: " + order.getOrderId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 订单消费者
        ExecutorService consumerExecutor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            consumerExecutor.submit(() -> {
                while (true) {
                    try {
                        Order order = orderQueue.poll(1, TimeUnit.SECONDS);
                        if (order != null) {
                            // 处理订单
                            System.out.println("处理订单: " + order.getOrderId());
                            processedCount.incrementAndGet();
                        } else if (processedCount.get() >= 6) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
        }

        producerExecutor.shutdown();
        consumerExecutor.shutdown();
        try {
            producerExecutor.awaitTermination(3, TimeUnit.SECONDS);
            consumerExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 秒杀商品
 */
class SeckillItem {
    private String name;
    private int stock;

    public SeckillItem(String name, int stock) {
        this.name = name;
        this.stock = stock;
    }

    public String getName() { return name; }
    public int getStock() { return stock; }
}

/**
 * 日志条目
 */
class LogEntry {
    private String level;
    private String message;
    private long timestamp;

    public LogEntry(String level, String message) {
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
