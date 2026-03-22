# Java 并发编程实战 - 代码学习

本项目是《Java 7 并发编程实战手册》的配套示例代码，涵盖 Java 并发编程的核心知识点。

## 项目结构

```
thread-base/
├── src/main/java/com/ssitao/code/thread/
│   ├── code01/          # 线程创建与运行
│   ├── code02/          # 线程中断
│   ├── code03/          # 素数生成器（线程控制示例）
│   ├── code04/          # 文件搜索（Runnable与Thread）
│   ├── code05/          # 文件时钟（线程休眠）
│   ├── code06/          # 线程Join（等待线程终止）
│   ├── code07/          # 守护线程与事件处理
│   ├── code08/          # 线程异常处理（UncaughtExceptionHandler）
│   ├── code09/          # ThreadLocal线程局部变量
│   ├── code10/          # Callable与Future
│   ├── code11/          # ThreadGroup线程组
│   ├── code12/          # ThreadFactory线程工厂
│   ├── code13/          # synchronized同步方法（转账示例）
│   ├── code14/          # synchronized与Lock对比
│   ├── code15/          # synchronized影院售票（多锁示例）
│   ├── code16/          # Semaphore信号量（生产者消费者）
│   ├── code17/          # Lock锁（打印队列）
│   ├── code18/          # ReadWriteLock读写锁
│   ├── code19/          # Lock条件变量
│   ├── code20/          # CountDownLatch倒计时门栓
│   ├── code21/          # CyclicBarrier循环屏障
│   ├── code22/          # Semaphore多资源信号量
│   ├── code23/          # CountDownLatch视频会议
│   ├── code24/          # ForkJoin分治任务
│   ├── code25/          # Phaser多阶段同步（文件搜索）
│   ├── code26/          # Phaser动态注册（考试场景）
│   └── code27/          # Exchanger数据交换器
└── pom.xml
```

## 代码清单

### 第一部分：线程基础（code01-code12）

| 编号 | 主题 | 关键知识点 |
|------|------|-----------|
| code01 | 线程创建运行 | `Thread`, `Runnable` |
| code02 | 线程中断 | `interrupt()`, `isInterrupted()` |
| code03 | 素数生成器 | 线程控制示例 |
| code04 | 文件搜索 | `Runnable` vs `Thread` |
| code05 | 文件时钟 | `Thread.sleep()` |
| code06 | Join等待 | `join()` 等待线程终止 |
| code07 | 守护线程 | `setDaemon(true)` |
| code08 | 异常处理 | `UncaughtExceptionHandler` |
| code09 | 线程局部变量 | `ThreadLocal<T>` |
| code10 | Callable/Future | `Callable<T>`, `Future<T>` |
| code11 | 线程组 | `ThreadGroup` |
| code12 | 线程工厂 | `ThreadFactory` |

### 第二部分：同步机制（code13-code15）

| 编号 | 主题 | 关键知识点 |
|------|------|-----------|
| code13 | synchronized同步 | 银行转账示例 |
| code14 | Lock锁对比 | `ReentrantLock` |
| code15 | 多锁同步 | 影院售票问题 |

### 第三部分：并发工具（code16-code27）

| 编号 | 主题 | 关键知识点 |
|------|------|-----------|
| code16 | Semaphore | 信号量控制并发 |
| code17 | Lock打印队列 | `ReentrantLock` |
| code18 | ReadWriteLock | 读写锁 |
| code19 | Condition | 条件变量 |
| code20 | CountDownLatch | 倒计时门栓 |
| code21 | CyclicBarrier | 循环屏障 |
| code22 | 多资源信号量 | Semaphore多实例 |
| code23 | 视频会议 | CountDownLatch |
| code24 | ForkJoin | 分治任务框架 |
| code25 | Phaser基础 | 多阶段同步 |
| code26 | Phaser动态注册 | `register()`, `onAdvance()` |
| code27 | Exchanger | 数据交换器 |

## 核心API速查

### 线程基础
```java
// 创建线程
Thread thread = new Thread(new RunnableTask());
thread.start();

// 线程中断
thread.interrupt();
thread.isInterrupted();

// 等待线程终止
thread.join();

// 守护线程
thread.setDaemon(true);

// 线程局部变量
ThreadLocal<Integer> local = new ThreadLocal<>();
local.set(1);
local.get();
```

### 同步机制
```java
// synchronized
synchronized (object) {
    // 临界区
}

// ReentrantLock
Lock lock = new ReentrantLock();
lock.lock();
try {
    // 临界区
} finally {
    lock.unlock();
}

// 读写锁
ReadWriteLock rwLock = new ReentrantReadWriteLock();
rwLock.readLock().lock();
rwLock.writeLock().lock();

// 条件变量
Condition cond = lock.newCondition();
cond.await();
cond.signal();
```

### 并发工具
```java
// 信号量
Semaphore sem = new Semaphore(3);
sem.acquire();
sem.release();

// CountDownLatch
CountDownLatch latch = new CountDownLatch(3);
latch.countDown();
latch.await();

// CyclicBarrier
CyclicBarrier barrier = new CyclicBarrier(3);
barrier.await();

// Phaser
Phaser phaser = new Phaser(3);
phaser.register();
phaser.arriveAndAwaitAdvance();
phaser.arriveAndDeregister();

// Exchanger
Exchanger<List<String>> exchanger = new Exchanger<>();
exchanger.exchange(data);
```

### 执行器
```java
// 创建执行器
ExecutorService executor = Executors.newFixedThreadPool(5);

// 提交任务
Future<T> future = executor.submit(Callable<T> task);

// 获取结果
T result = future.get();

// 关闭
executor.shutdown();
```

### Fork/Join
```java
ForkJoinPool pool = new ForkJoinPool();
pool.execute(task);
```

### 原子变量
```java
AtomicInteger atomicInt = new AtomicInteger(0);
atomicInt.incrementAndGet();
atomicInt.compareAndSet(expected, update);
```

## 运行方式

```bash
# 编译
mvn compile

# 运行单个示例
mvn exec:java -Dexec.mainClass="com.ssitao.code.thread.code27.core.Main"

# 或直接运行主类
java -cp target/classes com.ssitao.code.thread.code27.core.Main
```

## 学习路径建议

1. **入门**: code01-code06（线程基础）
2. **核心**: code13-code15（同步机制）
3. **进阶**: code16-code23（并发工具）
4. **高级**: code24-code27（ForkJoin与高级同步）

## 参考资料

- 《Java 7 并发编程实战手册》
- `Java并发编程实战-总结.md` - 详细知识点总结