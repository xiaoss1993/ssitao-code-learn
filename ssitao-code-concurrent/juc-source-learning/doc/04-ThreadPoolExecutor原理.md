# ThreadPoolExecutor 原理详解

## 1. 概述

ThreadPoolExecutor 是 Java 线程池的核心实现，通过线程复用机制提高了系统性能。

### 核心优势

- **减少资源消耗**：避免频繁创建销毁线程
- **提高响应速度**：任务到达可直接执行
- **统一管理**：便于监控和调优

---

## 2. 核心参数

```java
public ThreadPoolExecutor(
    int corePoolSize,              // 核心线程数
    int maximumPoolSize,           // 最大线程数
    long keepAliveTime,            // 空闲线程存活时间
    TimeUnit unit,                 // 时间单位
    BlockingQueue<Runnable> workQueue,  // 任务队列
    ThreadFactory threadFactory,   // 线程工厂
    RejectedExecutionHandler handler    // 拒绝策略
)
```

### 参数关系

```
                        任务提交
                            │
                            ▼
                    ┌───────────────┐
                    │ workerCount <  │──No──► 进入任务队列
                    │ corePoolSize?  │
                    └───────┬────────┘
                            │Yes
                            ▼
                    ┌───────────────┐
                    │ 创建核心线程   │
                    │ 执行任务       │
                    └───────────────┘

        队列满 + workerCount >= corePoolSize
                            │
                            ▼
                    ┌───────────────┐
                    │ workerCount <  │──No──► 执行拒绝策略
                    │ maximumPoolSize│
                    └───────┬────────┘
                            │Yes
                            ▼
                    ┌───────────────┐
                    │ 创建临时线程   │
                    │ 执行任务       │
                    └───────────────┘
```

---

## 3. 状态管理

### 线程池状态

```java
// ctl 包含两部分信息:
// 高3位: 线程池状态
// 低29位: worker数量

private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

// 状态常量
private static final int COUNT_BITS = Integer.SIZE - 3;  // 29
private static final int CAPACITY   = (1 << COUNT_BITS) - 1;  // (1<<29)-1

private static final int RUNNING    = -1 << COUNT_BITS;  // 111... = 接收新任务，处理队列
private static final int SHUTDOWN   =  0 << COUNT_BITS;  // 000... = 不接收新任务，处理队列
private static final int STOP       =  1 << COUNT_BITS;  // 001... = 不接收新任务，不处理队列
private static final int TIDYING    =  2 << COUNT_BITS;  // 010... = 任务终止，准备调用terminated()
private static final int TERMINATED =  3 << COUNT_BITS;  // 011... = terminated()执行完毕

// 状态转换
RUNNING -> SHUTDOWN -> STOP -> TIDYING -> TERMINATED
```

### 状态转换图

```
RUNNING:    正常运行，接受新任务
    │
    ▼ (shutdown())
SHUTDOWN:   不接受新任务，执行队列中的任务
    │
    ▼ (shutdownNow())
STOP:       不接受新任务，不执行队列任务，中断运行中任务
    │
    ▼ (所有任务终止)
TIDYING:    所有任务已终止，workerCount=0
    │
    ▼ (terminated()执行完毕)
TERMINATED: 完全终止
```

---

## 4. Worker 类

### Worker 继承 AQS

```java
private final class Worker extends AbstractQueuedSynchronizer {
    final Thread thread;             // 执行任务的线程
    Runnable firstTask;             // 第一个任务
    long completedTasks;             // 完成的任务数

    Worker(Runnable firstTask) {
        setState(-1);  // 禁止中断，直到任务开始
        this.firstTask = firstTask;
        this.thread = getThreadFactory().newThread(this);
    }

    // 实现AQS的tryAcquire，作为互斥锁
    protected boolean tryAcquire(int unused) {
        if (compareAndSetState(0, 1)) {
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }
}
```

### Worker.run() 循环

```java
final void runWorker(Worker w) {
    Thread wt = Thread.currentThread();
    Runnable task = w.firstTask;
    w.firstTask = null;

    while (task != null || (task = getTask()) != null) {
        w.lock();

        // 确保中断状态正确
        if ((runStateAtLeast(ctl.get(), STOP) ||
             (Thread.interrupted() && runStateAtLeast(ctl.get(), STOP))) &&
            !wt.isInterrupted())
            wt.interrupt();

        try {
            beforeExecute(wt, task);
            task.run();  // 执行任务
            afterExecute(task, null);
        } catch (Throwable ex) {
            afterExecute(task, ex);
            throw ex;
        } finally {
            task = null;
            w.unlock();
        }
    }
    processWorkerExit(w, completedAbruptly);
}
```

---

## 5. execute 流程

```java
public void execute(Runnable command) {
    if (command == null)
        throw new NullPointerException();

    int c = ctl.get();

    // 1. 检查worker数量
    if (workerCountOf(c) < corePoolSize) {
        // 添加核心线程
        if (addWorker(command, true))
            return;
        c = ctl.get();
    }

    // 2. 尝试加入队列
    if (isRunning(c) && workQueue.offer(command)) {
        int recheck = ctl.get();
        // 再次检查状态
        if (!isRunning(recheck) && remove(command))
            reject(command);  // 拒绝
        else if (workerCountOf(recheck) == 0)
            addWorker(null, false);  // 可能需要添加一个线程
    }
    // 3. 尝试添加非核心线程
    else if (!addWorker(command, false))
        reject(command);  // 拒绝
}
```

---

## 6. 任务获取

```java
private Runnable getTask() {
    boolean timedOut = false;

    for (;;) {
        int c = ctl.get();
        int rs = runStateOf(c);

        // 检查是否需要退出
        if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
            decrementWorkerCount();
            return null;
        }

        int wc = workerCountOf(c);

        // 是否需要超时控制
        boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;

        if ((wc > maximumPoolSize || (timed && timedOut))
            && (wc > 1 || workQueue.isEmpty())) {
            if (compareAndDecrementWorkerCount(c))
                return null;
            continue;
        }

        try {
            Runnable r = timed ?
                workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                workQueue.take();  // 阻塞获取
            if (r != null)
                return r;
            timedOut = true;
        } catch (InterruptedException retry) {
            timedOut = false;
        }
    }
}
```

---

## 7. 拒绝策略

### 四种策略

| 策略 | 行为 |
|------|------|
| AbortPolicy | 抛出RejectedExecutionException |
| DiscardPolicy | 静默丢弃 |
| CallerRunsPolicy | 由调用线程执行 |
| DiscardOldestPolicy | 丢弃最旧任务 |

### 接口定义

```java
public interface RejectedExecutionHandler {
    void rejectedExecution(Runnable r, ThreadPoolExecutor executor);
}
```

### 自定义拒绝策略示例

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    2, 4, 60, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(2),
    r -> {
        // 记录日志或保存到文件
        System.out.println("Task rejected: " + r);
    }
);
```

---

## 8. 关闭线程池

```java
// 不接收新任务，等待队列任务完成
public void shutdown() {
    advanceRunState(SHUTDOWN);
    interruptIdleWorkers();
    onShutdown();
}

// 不接收新任务，不执行队列任务
public List<Runnable> shutdownNow() {
    advanceRunState(STOP);
    interruptWorkers();
    tasks = drainQueue();
    return tasks;
}
```

---

## 9. 常用线程池

### Executors 工厂方法

```java
// 单线程池
ExecutorService single = Executors.newSingleThreadExecutor();

// 固定大小线程池
ExecutorService fixed = Executors.newFixedThreadPool(4);

// 缓存线程池
ExecutorService cached = Executors.newCachedThreadPool();

// 调度线程池
ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(4);
```

### 建议

> 阿里编程规范：不使用 Executors 创建线程池，应使用 ThreadPoolExecutor 明确参数。

```java
// 推荐方式
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    2,                      // corePoolSize
    10,                     // maximumPoolSize
    60L,                    // keepAliveTime
    TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(100),  // 有界队列
    new ThreadFactoryBuilder().setNameFormat("pool-%d").build(),
    new ThreadPoolExecutor.AbortPolicy()
);
```
