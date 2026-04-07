# 第九章：并发

> Effective Java Chapter 9: Concurrency

## 章节概览

本章涵盖 7 个条目（78-84），讨论并发编程的最佳实践。

---

## Item 78: Synchronize access to shared mutable data

### 同步的作用

| 作用 | 说明 |
|------|------|
| 原子性 | 确保操作不可分割 |
| 可见性 | 确保一个线程的修改对其他线程可见 |

### 错误示例

```java
// 不安全！increment()和get()不是原子操作
private long count = 0;

public void increment() {
    count++;  // read-modify-write，不是原子操作
}
```

### 正确示例

```java
// 线程安全
private synchronized long increment() {
    count++;
}

public synchronized long get() {
    return count;
}
```

### volatile vs synchronized

```java
// volatile：确保可见性，但不保证原子性
private volatile boolean stopped = false;

// 适用场景：只有一个线程写入，其他线程读取
public void stop() { stopped = true; }
public boolean isStopped() { return stopped; }

// 错误！count++不是原子操作
private volatile long count = 0;
public void increment() { count++; }  // 仍然不是线程安全的！
```

### 最佳选择

```java
// 使用AtomicLong等原子类
private final AtomicLong count = new AtomicLong();

public void increment() {
    count.incrementAndGet();  // 原子操作
}
```

---

## Item 79: Avoid excessive synchronization

### 过度同步的问题

1. **性能下降**：锁竞争
2. **死锁风险**：可能在同步块内调用外来方法
3. **活跃性问题**：过多同步

### 正确做法

```java
// 在同步块外做耗时操作
public void addObserver(Observer<E> observer) {
    Observer<E> obsCopy;
    synchronized (set) {
        obsCopy = new ArrayList<>(observers);
    }
    // 在同步块外调用回调
    for (Observer<E> observer : obsCopy) {
        observer.added(this, e);  // 可能修改observers
    }
}
```

### 使用并发集合

```java
// CopyOnWriteArrayList - 迭代时无需同步
private final List<Observer<E>> observers = new CopyOnWriteArrayList<>();

public void addObserver(Observer<E> observer) {
    observers.add(observer);  // 线程安全，无需外部同步
}
```

---

## Item 80: Prefer executors, tasks, and streams to threads

### 使用ExecutorService

```java
// 创建线程池
ExecutorService executor = Executors.newFixedThreadPool(10);

// 提交任务
Future<?> future = executor.submit(() -> {
    // 任务逻辑
});

// 等待完成
future.get();  // 阻塞直到完成

// 关闭
executor.shutdown();
```

### 线程池类型

| 类型 | 说明 |
|------|------|
| `newFixedThreadPool(n)` | 固定大小n |
| `newCachedThreadPool()` | 按需创建，回收空闲线程 |
| `newSingleThreadExecutor()` | 单线程 |
| `newScheduledThreadPool(n)` | 定时/周期执行 |

### 生产者-消费者

```java
// 使用BlockingQueue
BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(100);

// 生产者
queue.put(item);  // 队列满时阻塞

// 消费者
Integer item = queue.take();  // 队列空时阻塞
```

---

## Item 81: Prefer concurrency utilities to wait and notify

### 高级并发工具

| 工具 | 用途 |
|------|------|
| `CountDownLatch` | 一次性倒计时 |
| `CyclicBarrier` | 可重用屏障 |
| `Semaphore` | 限流 |
| `ConcurrentHashMap` | 高效并发Map |
| `BlockingQueue` | 阻塞队列 |

### CountDownLatch示例

```java
CountDownLatch startSignal = new CountDownLatch(1);  // 开始信号
CountDownLatch doneSignal = new CountDownLatch(n);   // 完成计数

// 工作者线程
new Thread(() -> {
    startSignal.await();  // 等待开始
    doWork();
    doneSignal.countDown();  // 完成
}).start();

// 主线程
startSignal.countDown();  // 发出开始信号
doneSignal.await();       // 等待所有完成
```

### Semaphore限流

```java
Semaphore permits = new Semaphore(3);  // 3个许可

public void doWork() throws InterruptedException {
    permits.acquire();  // 获取许可
    try {
        // 工作
    } finally {
        permits.release();  // 释放许可
    }
}
```

---

## Item 82: Document thread safety

### 线程安全级别

| 级别 | 说明 | 示例 |
|------|------|------|
| 不可变 | 不需要同步 | String, BigInteger |
| 无条件线程安全 | 所有方法线程安全 | ConcurrentHashMap |
| 有条件线程安全 | 部分方法需要同步 | Collections.synchronizedList |
| 非线程安全 | 需要外部同步 | ArrayList, HashMap |
| 线程对立 | 并发时出错 | - |

### 文档注释

```java
/**
 * 此类是线程安全的。
 *
 * <p>此类的实例可以安全地被多个线程并发访问。
 * 不需要外部同步。
 *
 * @see java.util.concurrent
 */
public class ThreadSafeClass { }
```

### @ThreadSafe注解

```java
@ThreadSafe
public class MyThreadSafeClass { }

@NotThreadSafe
public class MyUnsafeClass { }
```

---

## 章节总结

| Item | 核心原则 |
|------|----------|
| 78 | 同步访问共享可变数据 |
| 79 | 避免过度同步 |
| 80 | 优先使用ExecutorService而非Thread |
| 81 | 优先使用并发工具而非wait/notify |
| 82 | 文档化线程安全程度 |

---

## 实战示例

```java
// 完整的生产者-消费者
public class ProducerConsumerExample {
    private final BlockingQueue<Data> queue = new LinkedBlockingQueue<>(100);

    public void start() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 生产者
        executor.submit(() -> {
            while (true) {
                Data data = produce();
                queue.put(data);
            }
        });

        // 消费者
        executor.submit(() -> {
            while (true) {
                Data data = queue.take();
                consume(data);
            }
        });

        // 优雅关闭
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
    }
}
```

---

## 练习题

1. synchronized和volatile有什么区别？
2. 为什么increment()不是线程安全的，即使加了volatile？
3. 什么时候使用AtomicInteger比synchronized更好？
4. CountDownLatch和CyclicBarrier有什么区别？
5. 如何实现一个限流器？
