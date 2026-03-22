# Java 7 并发编程实战手册 - 项目总结

## 项目概述

本项目是《Java 7 并发编程实战手册》的配套示例代码库，涵盖了Java并发编程的各个方面，共包含 **7个章节**，**50+个示例**，全面讲解了Java并发API的核心知识。

---

## 章节结构总览

| 章节 | 主题 | 示例数量 |
|------|------|----------|
| 第1章 | 线程基础 | 12个 |
| 第2章 | 同步机制 | 8个 |
| 第3章 | 并发控制 | 7个 |
| 第4章 | 线程执行器 | 11个 |
| 第5章 | Fork/Join框架 | 5个 |
| 第6章 | 并发集合 | 8个 |
| 第7章 | 高级定制 | 10个 |

---

## 第1章：线程基础

### 主题内容
- **线程的创建和运行**：`Thread`类和`Runnable`接口的使用
- **线程信息的获取和设置**：获取线程ID、名称、优先级等
- **线程的中断**：使用`interrupt()`方法中断线程
- **线程中断的控制**：通过中断标志控制线程行为
- **线程的休眠和恢复**：`Thread.sleep()`和定时唤醒
- **等待线程的终止**：`join()`方法的使用
- **守护线程的创建和运行**：设置线程为守护线程
- **线程中不可控异常的处理**：实现`UncaughtExceptionHandler`
- **线程局部变量的使用**：`ThreadLocal`的使用
- **线程分组**：`ThreadGroup`的管理
- **线程组中不可控制异常的处理**：组级别的异常处理
- **使用工厂类创建线程**：自定义`ThreadFactory`

### 核心API
```java
// 线程创建
Thread thread = new Thread(new RunnableTask());
thread.start();

// 线程中断
thread.interrupt();

// 等待线程终止
thread.join();

// 守护线程
thread.setDaemon(true);

// 线程局部变量
ThreadLocal<Integer> local = new ThreadLocal<>();
```

---

## 第2章：同步机制

### 主题内容
- **synchronized实现同步方法-问题**：分析同步方法的问题
- **synchronized问题解决**：正确的同步实现方式
- **使用非依赖属性实现同步**：避免死锁
- **在同步代码中使用条件**：`wait()/notify()/notifyAll()`
- **用锁实现同步**：`ReentrantLock`的使用
- **使用读写锁实现同步数据访问**：`ReentrantReadWriteLock`
- **修改锁的公平性**：公平锁与非公平锁
- **在锁中使用多条件**：`Condition`实现多路等待

### 核心API
```java
// synchronized同步块
synchronized (object) {
    // 临界区代码
}

// ReentrantLock锁
Lock lock = new ReentrantLock();
lock.lock();
try {
    // 临界区
} finally {
    lock.unlock();
}

// 读写锁
ReadWriteLock rwLock = new ReentrantReadWriteLock();
rwLock.readLock().lock();  // 读操作
rwLock.writeLock().lock(); // 写操作

// 条件变量
Condition condition = lock.newCondition();
condition.await();
condition.signal();
```

---

## 第3章：并发控制

### 主题内容
- **资源并发控制访问**：信号量`Semaphore`控制并发数
- **资源的多副本的并发访问控制**：多资源池管理
- **等待多个并发事件的完成**：`CountDownLatch`
- **在集合点的同步**：使用`CyclicBarrier`
- **并发阶段任务的运行**：`Phaser`实现分阶段任务
- **并发阶段任务中的阶段切换**：`Phaser`阶段转换
- **并发任务间的数据交换**：`Exchanger`数据交换

### 核心API
```java
// 信号量
Semaphore semaphore = new Semaphore(3);
semaphore.acquire();
semaphore.release();

// CountDownLatch
CountDownLatch latch = new CountDownLatch(3);
latch.countDown();
latch.await();

// CyclicBarrier
CyclicBarrier barrier = new CyclicBarrier(3);
barrier.await();

// Phaser
Phaser phaser = new Phaser(3);
phaser.arriveAndAwaitAdvance();

// Exchanger
Exchanger<List<String>> exchanger = new Exchanger<>();
exchanger.exchange(data);
```

---

## 第4章：线程执行器

### 主题内容
- **创建线程执行器**：`ExecutorService`基础使用
- **创建固定大小的线程执行器**：`Executors.newFixedThreadPool()`
- **在执行器中执行任务并返回结果**：`Callable`与`Future`
- **运行多个任务并处理第一个结果**：`invokeAny()`
- **运行多个任务并且处理所有结果**：`invokeAll()`
- **在执行器中延时执行任务**：`ScheduledExecutorService`
- **在执行器中周期性执行任务**：定时任务执行
- **在执行器中取消任务**：任务取消机制
- **在执行器中控制任务的完成**：`CompletionService`
- **在执行器中分离任务的启动与结果的处理**：异步结果处理
- **处理在执行器中被拒绝的任务**：自定义拒绝策略

### 核心API
```java
// 创建执行器
ExecutorService executor = Executors.newCachedThreadPool();

// 提交任务
Future<T> future = executor.submit(Callable<T> task);
executor.execute(Runnable task);

// 获取结果
T result = future.get();
T result = future.get(long timeout, TimeUnit unit);

// 关闭执行器
executor.shutdown();
executor.shutdownNow();

// 定时执行
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
```

---

## 第5章：Fork/Join框架

### 主题内容
- **创建Fork-Join线程池**：`ForkJoinPool`的使用
- **合并任务的结果**：`RecursiveTask`返回结果
- **异步运行任务**：`ForkJoinPool.execute()`异步执行
- **在任务中抛出异常**：异常处理机制
- **取消任务**：任务取消

### 核心API
```java
// 创建ForkJoinPool
ForkJoinPool pool = new ForkJoinPool();

// 提交任务
pool.execute(task);
pool.invoke(task);

// 自定义递归任务
class MyTask extends RecursiveTask<Integer> {
    @Override
    protected Integer compute() {
        if (problemSize < THRESHOLD) {
            return solveDirectly();
        }
        MyTask subtask1 = new MyTask(...);
        MyTask subtask2 = new MyTask(...);
        subtask1.fork();
        subtask2.fork();
        return subtask1.join() + subtask2.join();
    }
}
```

---

## 第6章：并发集合

### 主题内容
- **使用非阻塞式线程安全列表**：`ConcurrentLinkedDeque`
- **使用阻塞式线程安全列表**：`LinkedBlockingDeque`
- **使用按优先级排序的阻塞式线程安全列表**：`PriorityBlockingQueue`
- **使用带有延迟元素的线程安全列表**：`DelayQueue`
- **使用线程安全可遍历映射**：`ConcurrentNavigableMap`
- **生成并发随机数**：`ThreadLocalRandom`
- **使用原子变量**：`AtomicInteger`, `AtomicLong`, `AtomicReference`
- **使用原子数组**：`AtomicIntegerArray`, `AtomicLongArray`

### 核心API
```java
// 并发列表
ConcurrentLinkedDeque<E> list = new ConcurrentLinkedDeque<>();
LinkedBlockingDeque<E> blockingList = new LinkedBlockingDeque<>();

// 优先级队列
PriorityBlockingQueue<E> pq = new PriorityBlockingQueue<>();

// 延迟队列
DelayQueue<Delayed> delayQueue = new DelayQueue<>();

// 并发映射
ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
ConcurrentNavigableMap<K, V> navMap = new ConcurrentSkipListMap<>();

// 原子变量
AtomicInteger atomicInt = new AtomicInteger();
atomicInt.incrementAndGet();
atomicInt.compareAndSet(expected, update);

// 原子数组
AtomicIntegerArray array = new AtomicIntegerArray(size);
array.getAndIncrement(index);
```

---

## 第7章：高级定制

### 主题内容
- **定制ThreadPoolExecutor类**：扩展线程池功能
- **基于优先级的Executor类**：优先级任务调度
- **实现ThreadFactory接口生成定制线程**：自定义线程工厂
- **在Executor对象中使用ThreadFactory**：应用线程工厂
- **定制运行在线程池中的任务**：扩展`RunnableAdapter`
- **通过ThreadFactory为Fork-Join框架生成定制线程**：Fork/Join线程定制
- **定制运行在Fork-Join框架中的任务**：自定义RecursiveTask
- **实现定制Lock类**：基于AQS实现自定义锁
- **实现基于优先级的传输队列**：`PriorityBlockingQueue`实现
- **实现自己的原子对象**：自定义原子操作

### 核心API
```java
// 自定义ThreadFactory
class MyThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("Custom-Thread-" + counter.incrementAndGet());
        thread.setPriority(Thread.NORM_PRIORITY);
        return thread;
    }
}

// 自定义AQS锁
class MyAbstractQueuedSynchronizer extends AbstractQueuedSynchronizer {
    @Override
    protected boolean tryAcquire(int arg) {
        // 自定义获取逻辑
    }
    @Override
    protected boolean tryRelease(int arg) {
        // 自定义释放逻辑
    }
}

// 自定义RejectedExecutionHandler
class MyRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // 自定义拒绝策略
    }
}
```

---

## 项目结构

```
java-concurrency/
├── 01-02~01-13  # 第1章：线程基础
├── 02-02~02-09  # 第2章：同步机制
├── 03-02~03-08  # 第3章：并发控制
├── 04-02~04-12  # 第4章：线程执行器
├── 05-02~05-06  # 第5章：Fork/Join框架
├── 06-02~06-09  # 第6章：并发集合
├── 07-02~07-11  # 第7章：高级定制
└── Template/    # IDEA项目模板
```

每个示例目录结构：
```
章节目录/
├── src/com/concurrency/
│   ├── core/         # 主程序入口
│   │   └── Main.java
│   └── task/         # 任务类
│       ├── Task.java
│       └── ...
└── 章节名.iml        # IDEA模块文件
```

---

## 学习路径建议

1. **入门**：从第1章开始，理解线程创建、运行、基础API
2. **进阶**：第2章同步机制是核心，需要深入理解
3. **实战**：第3-4章掌握并发控制和执行器框架
4. **高级**：第5-7章涉及Fork/Join框架和高级定制

---

## 关键知识点速查

| 场景 | 推荐方案 |
|------|----------|
| 线程同步 | `synchronized` / `ReentrantLock` |
| 线程间通信 | `wait/notify` / `Condition` |
| 资源限制 | `Semaphore` |
| 任务协调 | `CountDownLatch` / `CyclicBarrier` / `Phaser` |
| 异步执行 | `ExecutorService` |
| 定时任务 | `ScheduledExecutorService` |
| 分治任务 | `ForkJoinPool` |
| 线程安全集合 | `ConcurrentHashMap` / `ConcurrentLinkedDeque` |
| 原子操作 | `AtomicInteger` / `AtomicReference` |

---

*本项目为《Java 7 并发编程实战手册》配套代码，适合Java并发编程学习和参考。*
