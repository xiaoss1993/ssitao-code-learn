# JUC 源码学习指南

## 学习路线

### 1. 基础构建块 (Atomic)
- `AtomicInteger` / `AtomicLong` / `AtomicBoolean`
- `AtomicReference`
- `AtomicIntegerArray` / `AtomicLongArray`
- `AtomicMarkableReference`
- `AtomicStampedReference`

### 2. 同步器 (Locks)
- `AQS` (AbstractQueuedSynchronizer) - 核心
- `ReentrantLock` - 可重入锁
- `ReentrantReadWriteLock` - 读写锁
- `Semaphore` - 信号量
- `CountDownLatch` - 倒计时门闩
- `CyclicBarrier` - 循环屏障
- `Phaser` - 阶段同步器

### 3. 集合框架 (Collections)
- `ConcurrentHashMap`
- `ConcurrentLinkedQueue` / `ConcurrentLinkedDeque`
- `LinkedBlockingQueue` / `LinkedBlockingDeque`
- `CopyOnWriteArrayList` / `CopyOnWriteArraySet`
- `PriorityBlockingQueue`
- `DelayQueue`

### 4. 执行框架 (Executors)
- `ThreadPoolExecutor`
- `ScheduledThreadPoolExecutor`
- `ForkJoinPool`
- `Executors` 工厂类

---

## 核心AQS设计思想

### 状态管理
```java
// AQS内部使用volatile int state表示同步状态
private volatile int state;

// 子类通过getState()/setState()/compareAndSetState()访问
```

### 两种模式
| 模式 | 方法 | 用途 |
|------|------|------|
| 独占模式 | `tryAcquire`/`tryRelease` | ReentrantLock |
| 共享模式 | `tryAcquireShared`/`tryReleaseShared` | Semaphore, CountDownLatch |

### 等待队列
AQS使用CLH队列变体管理等待线程：
- 节点状态`CANCELLED`/`SIGNAL`/`CONDITION`/`PROPAGATE`
- `head`指向已获取锁的节点
- `tail`指向队尾

---

## 思维导图

```
JUC
├── Atomic原子类
│   ├── CAS原理
│   ├── Unsafe类
│   └── 字段偏移量
├── Locks锁
│   ├── AQS队列
│   ├── CLH队列
│   └── 等待通知机制
├── Collections集合
│   ├── 分段锁
│   ├── 无锁算法
│   └── 弱一致性迭代
└── Executors执行器
    ├── 线程池模型
    ├── 任务队列
    └── 拒绝策略
```

---

## 学习资源

### JDK8源码位置
- `rt.jar` → `java.util.concurrent`
- `java.util.concurrent.atomic`
- `java.util.concurrent.locks`

### 推荐阅读顺序
1. `AtomicInteger` - 最简单的源码，理解CAS
2. `ReentrantLock` - AQS标准用法
3. `Semaphore` - 共享模式AQS
4. `CountDownLatch` - 倒计时器
5. `ConcurrentHashMap` - 高并发集合
6. `ThreadPoolExecutor` - 线程池核心
