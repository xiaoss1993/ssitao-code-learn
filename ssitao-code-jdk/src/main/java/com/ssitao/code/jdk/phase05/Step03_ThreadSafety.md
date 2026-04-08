# 步骤3：线程安全与锁 - 深入理解并发

---

## 3.1 线程安全问题

### 3.1.1 可见性、原子性、有序性

```java
// 并发三要素

// 1. 可见性(Visibility)
//    一个线程对共享变量的修改，其他线程能立即看到

// 2. 原子性(Atomicity)
//    一个操作要么全部完成，要么全部不完成

// 3. 有序性(Order)
//    程序执行的顺序按照代码顺序执行（happens-before原则）
```

### 3.1.2 常见线程安全问题

```java
// 问题1：可见性问题
class VisibilityProblem {
    private boolean flag = false;

    // 线程1
    public void writer() {
        flag = true;  // 可能对线程2不可见
    }

    // 线程2
    public void reader() {
        while (!flag) {  // 可能一直循环
            Thread.sleep(100);
        }
        System.out.println("Flag is true");
    }
}

// 问题2：非原子性问题
class AtomicProblem {
    private int counter = 0;

    // 不是原子操作（读取-修改-写入）
    public void increment() {
        counter++;  // 不是线程安全的
    }
}

// 问题3：竞态条件
class RaceCondition {
    private int value = 0;

    // 先检查后执行
    public void checkThenAct() {
        if (value > 0) {  // 检查
            // 在检查和执行之间，其他线程可能修改了value
            value = value * 2;  // 执行
        }
    }
}
```

---

## 3.2 volatile关键字

### 3.2.1 volatile的作用

```java
// volatile保证：
// 1. 可见性：写操作立即刷新到主内存，读操作从主内存读取
// 2. 有序性：禁止指令重排序

// volatile不能保证原子性！
class VolatileDemo {
    private volatile boolean flag = false;

    // 线程1：写
    public void writer() {
        flag = true;  // 立即刷新到主内存
    }

    // 线程2：读
    public void reader() {
        while (flag) {  // 总是读取最新值
            // do something
        }
    }
}
```

### 3.2.2 volatile的适用场景

```java
// 适合volatile的场景：
// 1. 状态标志
private volatile boolean shutdown;

public void shutdown() {
    shutdown = true;
}

public void run() {
    while (!shutdown) {
        // do work
    }
}

// 2. 双重检查锁定（单例模式）
class Singleton {
    private static volatile Singleton instance;

    public static Singleton getInstance() {
        if (instance == null) {  // 第一次检查
            synchronized (Singleton.class) {
                if (instance == null) {  // 第二次检查
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

// 3. 观察者模式中的状态变化
private volatile int status;
```

### 3.2.3 volatile vs synchronized

| 特性 | volatile | synchronized |
|------|----------|--------------|
| 原子性 | 否 | 是 |
| 可见性 | 是 | 是 |
| 有序性 | 是（部分） | 是（完全） |
| 性能 | 高 | 低 |

---

## 3.3 synchronized深入

### 3.3.1 锁的对象

```java
// synchronized的锁对象可以是：
// 1. this（当前对象）
// 2. 类对象（Class对象）
// 3. 任意Object

class LockDemo {
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    // 锁this
    public synchronized void method1() {
        // 锁住的是this
    }

    // 锁指定对象
    public void method2() {
        synchronized (lock1) {
            // 只锁lock1
        }
    }

    // 锁Class对象
    public static synchronized void staticMethod() {
        // 锁住的是Singleton.class
    }
}
```

### 3.3.2 同步原理

```java
// synchronized的原理：
// 1. 每个对象有一个Monitor
// 2. 进入synchronized块时，尝试获取Monitor
// 3. 退出时，释放Monitor

// JVM通过对象头的Mark Word实现
// Mark Word包含：
// - 无锁状态
// - 偏向锁状态
// - 轻量级锁状态
// - 重量级锁状态
```

### 3.3.3 锁的优化（JDK 6+）

```java
// JDK 6引入的锁优化：

// 1. 偏向锁
//    第一次获取锁时，记录线程ID
//    同一线程再次获取时，无需竞争
//    -XX:+UseBiasedLocking

// 2. 轻量级锁
//    多个线程交替获取锁，不竞争
//    使用CAS避免线程阻塞

// 3. 自旋锁
//    忙等待，不阻塞线程
//    默认10次，可通过-XX:PreBlockSpin调整

// 4. 锁消除
//    JIT编译器分析，确认无竞争后消除锁

// 5. 锁粗化
//    多个小锁合并为一个大锁
```

---

## 3.4 Lock接口

### 3.4.1 ReentrantLock

```java
// ReentrantLock：可重入锁
// 比synchronized更灵活

ReentrantLock lock = new ReentrantLock();

lock.lock();  // 加锁
try {
    // 业务逻辑
} finally {
    lock.unlock();  // 释放锁（必须在finally中）
}

// tryLock() - 非阻塞获取
if (lock.tryLock()) {
    try {
        // 获得锁
    } finally {
        lock.unlock();
    }
} else {
    // 未获得锁
}

// tryLock(timeout) - 超时获取
if (lock.tryLock(5, TimeUnit.SECONDS)) {
    try {
        // 获得锁
    } finally {
        lock.unlock();
    }
} else {
    // 超时
}
```

### 3.4.2 ReentrantReadWriteLock

```java
// ReentrantReadWriteLock：读写锁
// 读操作可以并发，写操作独占

ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
Lock readLock = rwLock.readLock();
Lock writeLock = rwLock.writeLock();

// 读操作
readLock.lock();
try {
    // 可以多个线程同时读
    int value = sharedData;
} finally {
    readLock.unlock();
}

// 写操作
writeLock.lock();
try {
    // 独占，写的时候不能读
    sharedData = newValue;
} finally {
    writeLock.unlock();
}
```

### 3.4.3 StampedLock（JDK 8）

```java
// StampedLock：戳锁，提供乐观读

StampedLock lock = new StampedLock();

// 写锁
long stamp = lock.writeLock();
try {
    // 独占写
} finally {
    lock.unlockWrite(stamp);
}

// 乐观读（不阻塞，写操作可能导致数据不一致）
long stamp = lock.tryOptimisticRead();
int value = sharedData;  // 读取
if (!lock.validate(stamp)) {  // 检查是否有写操作
    // 有写操作，升级为悲观读
    stamp = lock.readLock();
    try {
        value = sharedData;
    } finally {
        lock.unlockRead(stamp);
    }
}

// 读锁
stamp = lock.readLock();
try {
    int value = sharedData;
} finally {
    lock.unlockRead(stamp);
}
```

### 3.4.4 Lock vs synchronized

| 特性 | Lock | synchronized |
|------|------|--------------|
| 尝试获取 | tryLock() | 不支持 |
| 超时 | tryLock(timeout) | 不支持 |
| 公平锁 | ReentrantLock(true) | 不支持 |
| 打断 | lockInterruptibly() | 不支持 |
| 条件变量 | newCondition() | wait/notify |
| 释放方式 | finally中unlock | 自动释放 |

---

## 3.5 条件变量

### 3.5.1 Condition接口

```java
// Condition：条件变量，配合Lock使用
// 相当于Object的wait/notify

ReentrantLock lock = new ReentrantLock();
Condition condition = lock.newCondition();

// 等待
condition.await();  // 相当于wait()

// signal
condition.signal();  // 相当于notify()

// signalAll
condition.signalAll();  // 相当于notifyAll()
```

### 3.5.2 生产者-消费者

```java
// 使用Condition实现生产者-消费者

class BoundedBuffer<T> {
    private final Object[] items;
    private int count, putIndex, takeIndex;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public BoundedBuffer(int capacity) {
        items = new Object[capacity];
    }

    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await();  // 队列满，等待
            }
            items[putIndex] = item;
            putIndex = (putIndex + 1) % items.length;
            count++;
            notEmpty.signal();  // 通知消费者
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();  // 队列空，等待
            }
            @SuppressWarnings("unchecked")
            T item = (T) items[takeIndex];
            takeIndex = (takeIndex + 1) % items.length;
            count--;
            notFull.signal();  // 通知生产者
            return item;
        } finally {
            lock.unlock();
        }
    }
}
```

---

## 3.6 线程安全的单例模式

### 3.6.1 各种实现对比

```java
// 1. 懒汉式（线程不安全）
class Singleton1 {
    private static Singleton1 instance;
    public static Singleton1 getInstance() {
        if (instance == null) {
            instance = new Singleton1();
        }
        return instance;
    }
}

// 2. 懒汉式（synchronized）
class Singleton2 {
    private static Singleton2 instance;
    public static synchronized Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }
}

// 3. 饿汉式（类加载时初始化）
class Singleton3 {
    private static final Singleton3 instance = new Singleton3();
    public static Singleton3 getInstance() {
        return instance;
    }
}

// 4. 双重检查锁定（推荐）
class Singleton4 {
    private static volatile Singleton4 instance;
    public static Singleton4 getInstance() {
        if (instance == null) {
            synchronized (Singleton4.class) {
                if (instance == null) {
                    instance = new Singleton4();
                }
            }
        }
        return instance;
    }
}

// 5. 静态内部类（推荐）
class Singleton5 {
    private static class Holder {
        static final Singleton5 instance = new Singleton5();
    }
    public static Singleton5 getInstance() {
        return Holder.instance;
    }
}

// 6. 枚举（最佳）
enum Singleton6 {
    INSTANCE;
    public void method() { }
}
```

---

## 3.7 线程池

### 3.7.1 Executors工厂方法

```java
// 线程池创建方式

// 1. FixedThreadPool - 固定大小
ExecutorService pool1 = Executors.newFixedThreadPool(4);

// 2. CachedThreadPool - 可扩展
ExecutorService pool2 = Executors.newCachedThreadPool();

// 3. SingleThreadExecutor - 单线程
ExecutorService pool3 = Executors.newSingleThreadExecutor();

// 4. ScheduledThreadPool - 定时任务
ScheduledExecutorService pool4 = Executors.newScheduledThreadPool(2);
pool4.scheduleAtFixedRate(() -> System.out.println("task"), 1, 5, TimeUnit.SECONDS);

// 5. WorkStealingPool - 工作窃取（JDK 8）
ExecutorService pool5 = Executors.newWorkStealingPool();
```

### 3.7.2 ThreadPoolExecutor

```java
// 手动创建线程池

ThreadPoolExecutor executor = new ThreadPoolExecutor(
    2,                      // corePoolSize：核心线程数
    4,                      // maximumPoolSize：最大线程数
    60,                     // keepAliveTime：空闲线程存活时间
    TimeUnit.SECONDS,       // 时间单位
    new LinkedBlockingQueue<>(100),  // 任务队列
    new ThreadFactory() {   // 线程工厂
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("my-pool-" + t.getId());
            return t;
        }
    },
    new ThreadPoolExecutor.AbortPolicy()  // 拒绝策略
);

// 拒绝策略：
// AbortPolicy - 抛异常（默认）
// CallerRunsPolicy - 由调用线程执行
// DiscardPolicy - 丢弃
// DiscardOldestPolicy - 丢弃最老的

// 提交任务
executor.execute(() -> System.out.println("task"));
Future<?> future = executor.submit(() -> "result");

// 关闭
executor.shutdown();      // 优雅关闭，不再接受新任务
executor.shutdownNow();  // 强制关闭

// 等待所有任务完成
executor.awaitTermination(60, TimeUnit.SECONDS);
```

### 3.7.3 线程池参数配置

```java
// CPU密集型任务：线程数 = CPU核心数 + 1
int cpuCount = Runtime.getRuntime().availableProcessors();
ExecutorService cpuPool = new ThreadPoolExecutor(
    cpuCount + 1, cpuCount + 1, 0L, TimeUnit.MILLISECONDS,
    new LinkedBlockingQueue<>(100)
);

// IO密集型任务：线程数 = CPU核心数 * 2 或更多
int ioThreads = cpuCount * 2;
ExecutorService ioPool = new ThreadPoolExecutor(
    ioThreads, ioThreads, 0L, TimeUnit.MILLISECONDS,
    new LinkedBlockingQueue<>(100)
);
```

---

## 3.8 练习题

```java
// 1. 说明volatile和synchronized的区别

// 2. 用ReentrantLock实现一个简单的信号量

// 3. 说明为什么单例模式要用双重检查锁定

// 4. 用线程池实现一个任务调度器

// 5. 分析以下代码的线程安全问题
class Counter {
    private int count = 0;

    public synchronized void inc() {
        count++;
    }

    public int getCount() {
        return count;  // 需要synchronized吗？
    }
}
```

---

## 3.9 参考答案

```java
// 1. volatile vs synchronized
// volatile: 保证可见性和有序性，不保证原子性
// synchronized: 保证可见性、原子性和有序性

// 2. 用ReentrantLock实现信号量
class Semaphore {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private int permits;

    public Semaphore(int permits) {
        this.permits = permits;
    }

    public void acquire() throws InterruptedException {
        lock.lock();
        try {
            while (permits <= 0) {
                condition.await();
            }
            permits--;
        } finally {
            lock.unlock();
        }
    }

    public void release() {
        lock.lock();
        try {
            permits++;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}

// 3. 双重检查的原因
// 第一次检查：避免不必要的竞争
// synchronized：保证原子性和可见性
// 第二次检查：防止多次实例化
// volatile：防止指令重排序

// 4. 任务调度器（简化版）
class TaskScheduler {
    private final ScheduledExecutorService executor;

    public TaskScheduler(int poolSize) {
        executor = Executors.newScheduledThreadPool(poolSize);
    }

    public ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        return executor.schedule(task, delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return executor.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public void shutdown() {
        executor.shutdown();
    }
}

// 5. 问题分析
// getCount()需要synchronized吗？
// 答：在这个例子中，inc()是synchronized的，并且int是32位，
//     JVM保证对int的读写是原子的，所以getCount()可以不用synchronized。
//     但为了绝对安全，建议加上synchronized或改为：
//     public int getCount() { return count; }
//     因为Java规范不保证32位基本类型的读写原子性（虽然实际通常原子）。
```

---

[返回目录](./README.md)

## 第五阶段总结

### 核心知识点

| 步骤 | 主题 | 核心概念 |
|------|------|----------|
| 1 | 线程基础 | Thread生命周期、创建方式、wait/notify、ThreadLocal |
| 2 | 并发工具 | CountDownLatch、CyclicBarrier、Semaphore、原子类 |
| 3 | 线程安全 | volatile、synchronized、Lock、线程池 |
