# Disruptor 教程总结

## 目录
1. [简介](#1-简介)
2. [核心概念](#2-核心概念)
3. [快速开始](#3-快速开始)
4. [示例详解](#4-示例详解)
5. [高级特性](#5-高级特性)
6. [性能优化](#6-性能优化)
7. [应用场景](#7-应用场景)

---

## 1. 简介

Disruptor 是 LMAX 开发的高性能并发消息传递框架，主要特点：

- **无锁设计**：通过 CAS 操作实现线程间通信
- **环形缓冲区**：预分配内存，避免 GC 压力
- **极高吞吐**：比 BlockingQueue 高一个数量级
- **极低延迟**：纳秒级处理时间

### Maven 依赖

```xml
<dependency>
    <groupId>com.lmax</groupId>
    <artifactId>disruptor</artifactId>
    <version>3.4.4</version>
</dependency>
```

---

## 2. 核心概念

### 2.1 RingBuffer（环形缓冲区）

Disruptor 的核心数据结构，一个预分配的固定大小数组。

```java
// 大小必须为 2 的幂
int bufferSize = 1024;
```

### 2.2 Event（事件）

在 RingBuffer 中传递的数据对象。

```java
public class LongEvent {
    private long value;
    // getter/setter
}
```

### 2.3 EventFactory（事件工厂）

用于创建事件对象，Disruptor 启动时预分配所有事件。

```java
public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
```

### 2.4 EventHandler（事件处理器）

消费者接口，处理业务逻辑。

```java
EventHandler<LongEvent> handler = (event, sequence, endOfBatch) -> {
    System.out.println("处理: " + event.getValue());
};
```

### 2.5 Producer（生产者）

向 RingBuffer 发布事件。

```java
long sequence = ringBuffer.next();
try {
    LongEvent event = ringBuffer.get(sequence);
    event.setValue(123);
} finally {
    ringBuffer.publish(sequence);
}
```

---

## 3. 快速开始

### 完整示例

```java
public class QuickStart {
    public static void main(String[] args) {
        // 1. 创建事件工厂
        LongEventFactory factory = new LongEventFactory();

        // 2. 创建 Disruptor
        int bufferSize = 1024;
        Executor executor = Executors.newSingleThreadExecutor();
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, executor);

        // 3. 设置消费者
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            System.out.println("消费事件: " + event.getValue());
        });

        // 4. 启动
        disruptor.start();

        // 5. 获取 RingBuffer 并发布事件
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        for (long i = 0; i < 10; i++) {
            long sequence = ringBuffer.next();
            try {
                LongEvent event = ringBuffer.get(sequence);
                event.setValue(i);
            } finally {
                ringBuffer.publish(sequence);
            }
        }

        // 6. 关闭
        disruptor.shutdown();
        executor.shutdown();
    }
}
```

### 运行流程图

```
┌─────────────────────────────────────────────────────────┐
│                      Disruptor                          │
│  ┌─────────┐    ┌──────────────┐    ┌───────────────┐  │
│  │ Producer│───▶│  RingBuffer │───▶│  EventHandler │  │
│  └─────────┘    └──────────────┘    └───────────────┘  │
│       │                │                    │          │
│       │            预分配内存              业务处理       │
│       │         (无GC压力)              (单线程消费)     │
└─────────────────────────────────────────────────────────┘
```

---

## 4. 示例详解

### 4.1 简单示例 (SimpleExample)

**目标**：掌握 Disruptor 基本使用流程

**要点**：
- EventFactory 预创建事件对象
- RingBuffer.next() 获取下一个可用位置
- publish() 发布事件
- 必须使用 try-finally 确保发布

### 4.2 多生产者示例 (MultiProducerExample)

**目标**：多个线程同时发布事件

**配置**：
```java
Executor executor = Executors.newFixedThreadPool(4);
Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, executor);
```

**特点**：
- RingBuffer 本身支持多生产者
- 通过 CAS 保证线程安全
- 可测试系统吞吐量

```
┌─────────┐  ┌─────────┐  ┌─────────┐
│Producer1│  │Producer2│  │Producer3│
└────┬────┘  └────┬────┘  └────┬────┘
     │            │            │
     └────────────┼────────────┘
                  ▼
           ┌────────────┐
           │ RingBuffer │
           └────────────┘
                  │
                  ▼
           ┌────────────┐
           │  Consumer  │
           └────────────┘
```

### 4.3 多消费者依赖示例 (MultiConsumerExample)

**目标**：消费者之间有依赖关系，需要串行/并行处理

**依赖配置**：
```java
disruptor.handleEventsWith(validator)           // 第一步
         .then(enricher, persister)               // 第二步（并行）
         .then(notifier, finalHandler);           // 第三步（并行）
```

**依赖关系图**：
```
           ┌─▶ Handler2 ─┐
Producer ─┼─▶ Handler3 ─┤─▶ Handler4 (最终处理)
           └─▶ Handler4 ─┘
```

### 4.4 交易系统示例 (TradingSystemExample)

**目标**：模拟真实交易所订单处理流程

**处理流程**：
```
接收订单 → 验证 → 风控检查 ─┐
                          ├→ 持仓更新 → 匹配引擎 → 成交回报
              账户更新 ────┘
```

**代码示例**：
```java
disruptor.handleEventsWith(validator)           // 1. 验证订单
        .then(riskChecker, accountUpdater)        // 2. 风控 + 账户（并行）
        .then(positionUpdater)                     // 3. 持仓更新
        .then(matcher)                            // 4. 订单匹配
        .then(tradeExecutor);                     // 5. 成交回报
```

**性能数据**：
- 10万订单处理
- 2个生产者并发发布
- 吞吐量可达数十万 orders/sec

### 4.5 日志处理示例 (LoggingSystemExample)

**目标**：高吞吐日志采集和处理

**处理流程**：
```
日志 → 解析 → 过滤 → ┌─▶ 写入文件
                     ├─▶ 发送告警（ERROR/FATAL）
                     └─▶ 指标统计
```

### 4.6 Spring 集成示例 (DisruptorSpring)

**目标**：在 Spring Boot 中使用 Disruptor

**项目结构**：
```
src/main/java/com/ssitao/code/disruptor/spring/
├── DisruptorSpringApplication.java    # Spring Boot 启动类
├── config/
│   ├── DisruptorConfig.java            # Disruptor 配置
│   ├── OrderEvent.java                 # 订单事件
│   └── OrderEventFactory.java          # 事件工厂
├── service/
│   └── OrderService.java               # 订单服务
└── controller/
    └── OrderController.java            # HTTP 接口
```

**配置类**：
```java
@Configuration
public class DisruptorConfig {

    @Bean
    public Disruptor<OrderEvent> orderDisruptor(OrderEventFactory factory) {
        Disruptor<OrderEvent> disruptor = new Disruptor<>(
            factory, 4096, Executors.newFixedThreadPool(4));

        disruptor.handleEventsWith(validatorHandler())
                 .then(riskHandler())
                 .then(matcherHandler())
                 .then(executorHandler());

        disruptor.start();
        return disruptor;
    }

    @Bean
    public RingBuffer<OrderEvent> orderRingBuffer(Disruptor<OrderEvent> disruptor) {
        return disruptor.getRingBuffer();
    }
}
```

**服务类**：
```java
@Service
public class OrderService {
    @Autowired
    private RingBuffer<OrderEvent> orderRingBuffer;

    public void placeOrder(String orderId, String symbol, double price, int quantity) {
        long sequence = orderRingBuffer.next();
        try {
            OrderEvent event = orderRingBuffer.get(sequence);
            event.setOrderId(orderId);
            event.setSymbol(symbol);
            event.setPrice(price);
            event.setQuantity(quantity);
        } finally {
            orderRingBuffer.publish(sequence);
        }
    }
}
```

**接口调用**：
```bash
# 下单
curl "http://localhost:8080/order/place?orderId=ORD001&symbol=AAPL&price=100.0&quantity=10"

# 批量下单
curl "http://localhost:8080/order/batch?count=100"
```

---

## 5. 高级特性

### 5.1 等待策略 (WaitStrategy)

控制消费者如何等待事件到达。

| 策略 | 特点 | 适用场景 |
|------|------|---------|
| BlockingWaitStrategy | 使用锁，CPU 占用最低 | 吞吐要求低，延迟不敏感 |
| SleepingWaitStrategy | 自旋 + yield + sleep | 低延迟，跨进程通信 |
| YieldingWaitStrategy | 自旋 + yield | 低延迟，高吞吐 |
| BusySpinWaitStrategy | 纯自旋 | 超低延迟，极高吞吐 |

```java
Disruptor<LogEvent> disruptor = new Disruptor<>(
    factory, bufferSize, executor,
    ProducerType.SINGLE,        // 或 ProducerType.MULTI
    new YieldingWaitStrategy()   // 等待策略
);
```

### 5.2 生产者类型

```java
ProducerType.SINGLE   // 单生产者（性能更好）
ProducerType.MULTI    // 多生产者
```

### 5.3 批量处理

EventHandler 的 `endOfBatch` 参数指示是否批量到达：

```java
EventHandler<Event> handler = (event, sequence, endOfBatch) -> {
    if (endOfBatch) {
        // 批量处理，性能更好
    }
};
```

### 5.4 异常处理

```java
// 方法1：设置全局异常处理器
disruptor.handleExceptionsFor(handler).with((exception, sequence, event, context) -> {
    // 记录异常，继续处理
});

// 方法2：设置特定处理器异常
disruptor.handleEventsWith(handler1, handler2);
disruptor.handleExceptionsFor(handler1).with(...);
```

---

## 6. 性能优化

### 6.1 RingBuffer 大小

- **必须是 2 的幂**：16, 32, 64, 128, 256, 512, 1024, 2048, 4096...
- **不宜过大**：占用更多内存
- **不宜过小**：导致消费者等待

### 6.2 避免伪共享 (False Sharing)

Disruptor 通过缓存行填充避免伪共享。

```
┌─────────────────────────────────────┐
│  Sequence (8 bytes)                │
├─────────────────────────────────────┤
│  Padding (56 bytes) - 填充         │
├─────────────────────────────────────┤
│  实际数据                           │
├─────────────────────────────────────┤
│  Padding (56 bytes) - 填充         │
└─────────────────────────────────────┘
```

### 6.3 最佳实践

1. **对象池复用**：避免频繁创建对象
2. **批量发布**：攒批发布减少竞争
3. **合理分区**：多消费者时注意分区策略
4. **监控水位**：关注 RingBuffer 消费进度

---

## 7. 应用场景

### 7.1 金融交易系统
- 订单匹配引擎
- 实时价格分发
- 风险控制

### 7.2 日志处理
- 集中式日志采集
- 日志分析管道

### 7.3 消息队列
- 高性能异步通信
- 事件驱动架构

### 7.4 复杂事件处理
- 实时数据分析
- 模式匹配
- 流式计算

### 7.5 Spring Boot 集成
- 微服务中作为本地高速处理层
- HTTP 请求 → Disruptor → 异步处理
- 配合 Kafka 使用：Kafka → 本地 Disruptor → 数据库

---

## 8. Spring Boot 快速上手

### 8.1 Maven 依赖

```xml
<dependency>
    <groupId>com.lmax</groupId>
    <artifactId>disruptor</artifactId>
    <version>3.3.7</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 8.2 配置类

```java
@Configuration
public class DisruptorConfig {

    @Bean
    public Disruptor<OrderEvent> orderDisruptor() {
        return new Disruptor<>(
            new OrderEventFactory(),
            4096,
            Executors.newFixedThreadPool(4)
        );
    }

    @Bean
    public RingBuffer<OrderEvent> orderRingBuffer(Disruptor<OrderEvent> disruptor) {
        return disruptor.getRingBuffer();
    }
}
```

### 8.3 运行

```bash
# 启动
mvn spring-boot:run

# 测试
curl "http://localhost:8080/order/place?orderId=ORD001&symbol=AAPL&price=100.0&quantity=10"
curl "http://localhost:8080/order/batch?count=1000"
```

---

## 附录：完整项目结构

```
disruptor/
├── pom.xml
├── src/main/
│   ├── java/com/ssitao/code/disruptor/
│   │   ├── example/                    # 独立示例
│   │   │   ├── SimpleExample.java
│   │   │   ├── MultiProducerExample.java
│   │   │   ├── MultiConsumerExample.java
│   │   │   ├── TradingSystemExample.java
│   │   │   ├── LoggingSystemExample.java
│   │   │   ├── LongEvent.java / LongEventFactory.java
│   │   │   ├── TradeEvent.java / TradeEventFactory.java
│   │   │   ├── OrderEvent.java / OrderEventFactory.java
│   │   │   └── LogEvent.java / LogEventFactory.java
│   │   └── spring/                    # Spring 集成示例
│   │       ├── DisruptorSpringApplication.java
│   │       ├── config/
│   │       │   ├── DisruptorConfig.java
│   │       │   ├── OrderEvent.java
│   │       │   └── OrderEventFactory.java
│   │       ├── service/
│   │       │   └── OrderService.java
│   │       └── controller/
│   │           └── OrderController.java
│   └── resources/
│       └── application.yml
└── Disruptor教程总结.md
```

## 参考资料

- [LMAX Disruptor 官方文档](https://lmax-exchange.github.io/disruptor/)
- [Disruptor GitHub](https://github.com/LMAX-Exchange/disruptor)
