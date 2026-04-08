# 步骤1：线程基础 - 多线程编程入门

---

## 1.1 线程概述

### 1.1.1 进程与线程

```java
// 进程（Process）
// - 独立运行的程序
// - 拥有独立的内存空间
// - 是操作系统分配资源的基本单位

// 线程（Thread）
// - 进程内的执行单元
// - 共享进程的内存空间
// - 是CPU调度的基本单位

// 关系：一个进程可以包含多个线程
// main线程是程序的入口
```

### 1.1.2 线程的创建方式

```java
// 方式1：继承Thread类
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("MyThread running");
    }
}

// 使用
MyThread t1 = new MyThread();
t1.start();  // 注意是start()，不是run()

// 方式2：实现Runnable接口（推荐）
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("MyRunnable running");
    }
}

// 使用
Thread t2 = new Thread(new MyRunnable());
t2.start();

// 方式3：使用Lambda（JDK 8+）
Thread t3 = new Thread(() -> System.out.println("Lambda thread"));
t3.start();

// 方式4：实现Callable接口（带返回值）
class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return 42;
    }
}

// 使用需要FutureTask
FutureTask<Integer> future = new FutureTask<>(new MyCallable());
new Thread(future).start();
Integer result = future.get();  // 获取返回值
```

### 1.1.3 start() vs run()

```java
// run()：普通方法调用，在主线程执行
// start()：启动新线程，JVM调用run()

Thread t = new Thread(() -> System.out.println("Running in new thread"));
t.run();    // 主线程执行，输出 "Running in new thread"
t.start();  // 新线程执行，输出 "Running in new thread"

// 直接调用run()不会创建新线程
```

---

## 1.2 线程生命周期

### 1.2.1 线程状态

```java
// 线程的6种状态
public enum Thread.State {
    NEW,          // 创建但未启动
    RUNNABLE,     // 可运行状态（可能正在运行）
    BLOCKED,      // 被阻塞（等待获取锁）
    WAITING,      // 无限等待
    TIMED_WAITING, // 限时等待
    TERMINATED    // 已终止
}
```

### 1.2.2 状态转换图

```
                    start()
    NEW ──────────────────→ RUNNABLE
                              │
                              ↓
                    ┌─────────┴─────────┐
                    ↓                   ↓
               run()完成            需要获取锁
              TERMINATED              BLOCKED
                                         │
                                         ↓
                    ┌────────────────────┼────────────────────┐
                    ↓                    ↓                    ↓
                wait()              sleep()               join()
              notify()              timeout                timeout
                    ↓                    ↓                    ↓
               WAITING           TIMED_WAITING         TIMED_WAITING
                    │                    │                    │
                    └────────────────────┴────────────────────┘
                                         ↓
                                    获得锁后
                                      进入
                                   RUNNABLE
```

### 1.2.3 线程优先级

```java
// 线程优先级：1-10，默认5
Thread t1 = new Thread(() -> System.out.println("Low priority"));
Thread t2 = new Thread(() -> System.out.println("High priority"));

t1.setPriority(Thread.MIN_PRIORITY);  // 1
t2.setPriority(Thread.MAX_PRIORITY);  // 10

// 注意：优先级只是提示，不保证执行顺序
```

---

## 1.3 常用方法

### 1.3.1 sleep()

```java
// sleep：让当前线程休眠，不释放锁
// 指定时间后自动唤醒

try {
    Thread.sleep(1000);  // 休眠1秒
} catch (InterruptedException e) {
    e.printStackTrace();
}

// 静态方法：Thread.sleep()
Thread.sleep(1000);
```

### 1.3.2 yield()

```java
// yield：让出CPU时间片，回到RUNNABLE状态
// 只是一个提示，不能保证其他线程一定执行

Thread.yield();  // 静态方法

// 用途：让优先级更高的线程有机会执行
```

### 1.3.3 join()

```java
// join：等待指定线程执行完成

Thread t = new Thread(() -> {
    try {
        Thread.sleep(2000);
        System.out.println("Thread finished");
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
});
t.start();

// 主线程等待t执行完成
t.join();  // 无限等待
// t.join(5000);  // 最多等待5秒
System.out.println("Main continues");

// 输出顺序：
// Thread finished  (2秒后)
// Main continues
```

### 1.3.4 interrupt()

```java
// interrupt：中断线程

Thread t = new Thread(() -> {
    while (!Thread.currentThread().isInterrupted()) {
        // 业务逻辑
    }
});
t.start();

// 中断线程
t.interrupt();  // 设置中断标志为true

// 注意：sleep/wait/join被中断会抛出InterruptedException

// 正确处理中断
Thread t2 = new Thread(() -> {
    try {
        while (!Thread.interrupted()) {
            // 业务逻辑
        }
    } catch (InterruptedException e) {
        // 线程被中断，优雅退出
        Thread.currentThread().interrupt();  // 恢复中断状态
    }
});
```

---

## 1.4 守护线程

### 1.4.1 守护线程概念

```java
// 守护线程（Daemon Thread）
// 当所有非守护线程结束时，JVM会自动退出
// 守护线程会被强制终止

// 典型的守护线程：GC线程、finalizer线程

Thread daemon = new Thread(() -> {
    while (true) {
        System.out.println("Daemon running");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            break;
        }
    }
});
daemon.setDaemon(true);  // 设置为守护线程
daemon.start();

// 如果main线程结束，daemon也会被终止
```

### 1.4.2 用户线程 vs 守护线程

```java
// 用户线程（User Thread）
// - 必须执行完成才能结束
// - 是"工作线程"

// 守护线程（Daemon Thread）
// - 为用户线程提供服务
// - 不能依赖守护线程的清理工作

// 设置时机：start()之前
thread.setDaemon(true);
thread.start();

// 注意：pool-1-thread-1可能是守护线程，不能依赖它执行清理工作
```

---

## 1.5 线程同步基础

### 1.5.1 synchronized关键字

```java
// synchronized：保证原子性和可见性

// 1. 同步方法
class Counter {
    private int count = 0;

    public synchronized void increment() {  // 锁的是this
        count++;
    }

    public synchronized int getCount() {    // 锁的是this
        return count;
    }
}

// 2. 同步代码块（更灵活）
class Counter2 {
    private int count = 0;
    private final Object lock = new Object();

    public void increment() {
        synchronized (lock) {  // 指定锁对象
            count++;
        }
    }

    public void increment2() {
        synchronized (this) {  // 锁this
            count++;
        }
    }
}

// 3. 静态同步方法（锁的是Class对象）
class StaticCounter {
    private static int count = 0;

    public static synchronized void increment() {  // 锁StaticCounter.class
        count++;
    }
}
```

### 1.5.2 synchronized的特性

```java
// 可重入性
class Parent {
    public synchronized void method1() {
        method2();  // 可以调用
    }

    public synchronized void method2() {
        // 同一个锁，可以进入
    }
}

// 不可中断
// 一旦获取锁，必须等待释放
```

### 1.5.3 同步的局限性

```java
// synchronized的缺点
// 1. 无法设置超时
// 2. 不能干搓（必须等待）
// 3. 非公平锁（不能指定）

// 替代方案：Lock接口（JDK 5+）
// - ReentrantLock：可重入锁
// - ReentrantReadWriteLock：读写锁
// - StampedLock：JDK 8的乐观读锁
```

---

## 1.6 wait/notify模式

### 1.6.1 基本用法

```java
// wait/notify是Object的方法
// 必须在synchronized块中使用

class BlockingQueue {
    private Object[] items = new Object[10];
    private int count = 0;

    public synchronized void put(Object item) throws InterruptedException {
        while (count == items.length) {
            wait();  // 队列满，等待
        }
        items[count++] = item;
        notify();  // 通知消费者
    }

    public synchronized Object take() throws InterruptedException {
        while (count == 0) {
            wait();  // 队列空，等待
        }
        Object item = items[--count];
        notify();  // 通知生产者
        return item;
    }
}
```

### 1.6.2 wait/notifyAll

```java
// wait()：释放锁，进入WAITING状态
// notify()：唤醒一个等待该锁的线程
// notifyAll()：唤醒所有等待该锁的线程

// 使用notifyAll()更安全
// 原因：notify()只能唤醒一个，可能唤醒错误类型的线程

synchronized (lock) {
    while (condition) {  // 用while，不是if
        lock.wait();
    }
    // 处理业务
}

// 唤醒时
synchronized (lock) {
    lock.notifyAll();  // 或notify()
}
```

---

## 1.7 ThreadLocal

### 1.7.1 基本用法

```java
// ThreadLocal：线程本地存储
// 每个线程都有独立的变量副本

class ThreadLocalDemo {
    private static ThreadLocal<Integer> counter = new ThreadLocal<>();

    public static void main(String[] args) {
        // 主线程设置
        counter.set(100);
        System.out.println("Main: " + counter.get());  // 100

        // 新线程访问（没有值）
        new Thread(() -> {
            System.out.println("Thread2 initial: " + counter.get());  // null
            counter.set(200);
            System.out.println("Thread2 after: " + counter.get());  // 200
        }).start();

        // 主线程再次访问（不受影响）
        System.out.println("Main again: " + counter.get());  // 100
    }
}
```

### 1.7.2 实际应用场景

```java
// 1. 数据库连接管理
class DBConnectionManager {
    private static ThreadLocal<Connection> connectionHolder =
        ThreadLocal.withInitial(() -> createConnection());

    public static Connection getConnection() {
        return connectionHolder.get();
    }
}

// 2. Web请求上下文
class RequestContext {
    private static ThreadLocal<RequestContext> contextHolder =
        ThreadLocal.withInitial(() -> new RequestContext());

    private String userId;
    private String traceId;

    public static RequestContext getContext() {
        return contextHolder.get();
    }
}

// 3. SimpleDateFormat（线程不安全）
class DateFormatter {
    private static final ThreadLocal<SimpleDateFormat> formatter =
        ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    public static String format(Date date) {
        return formatter.get().format(date);
    }
}
```

### 1.7.3 内存泄漏风险

```java
// ThreadLocal的内存泄漏
// ThreadLocalMap的Entry继承WeakReference<ThreadLocal>
// 如果ThreadLocal被回收，Entry.key变为null
// 但value不会自动回收

// 正确做法：用完调用remove()
ThreadLocal<Object> tl = new ThreadLocal<>();
try {
    tl.set(obj);
    // 使用
} finally {
    tl.remove();  // 清理
}
```

---

## 1.8 练习题

```java
// 1. 创建3个线程，分别打印1-10

// 2. 实现一个阻塞队列（用wait/notify）

// 3. 以下代码是否有问题？
class Counter {
    private int count = 0;

    public void increment() {
        synchronized (this) {
            count++;
        }
    }

    public int getCount() {
        return count;  // 没有synchronized
    }
}

// 4. 说明sleep和wait的区别

// 5. 用ThreadLocal实现每个线程独立的ID生成器
```

---

## 1.9 参考答案

```java
// 1. 三个线程分别打印1-10
for (int i = 0; i < 3; i++) {
    final int threadId = i;
    new Thread(() -> {
        for (int j = 1; j <= 10; j++) {
            System.out.println("Thread " + threadId + ": " + j);
        }
    }).start();
}

// 2. 阻塞队列实现
class BlockingQueue<T> {
    private Object[] items;
    private int count = 0;
    private int putIndex = 0;
    private int takeIndex = 0;
    private final Object lock = new Object();

    public BlockingQueue(int capacity) {
        items = new Object[capacity];
    }

    public void put(T item) throws InterruptedException {
        synchronized (lock) {
            while (count == items.length) {
                lock.wait();
            }
            items[putIndex] = item;
            putIndex = (putIndex + 1) % items.length;
            count++;
            lock.notify();
        }
    }

    public T take() throws InterruptedException {
        synchronized (lock) {
            while (count == 0) {
                lock.wait();
            }
            @SuppressWarnings("unchecked")
            T item = (T) items[takeIndex];
            takeIndex = (takeIndex + 1) % items.length;
            count--;
            lock.notify();
            return item;
        }
    }
}

// 3. 问题分析
// getCount()没有同步，可能读取到过期数据
// 解决方案：加synchronized或用volatile

// 4. sleep vs wait
// sleep: 不释放锁，超时自动唤醒
// wait:  释放锁，需要notify/notifyAll唤醒

// 5. ThreadLocal ID生成器
class IdGenerator {
    private static final ThreadLocal<Integer> idHolder =
        ThreadLocal.withInitial(() -> 0);

    public static int getNextId() {
        return idHolder.get()++;
    }
}
```

---

[返回目录](./README.md) | [下一步：并发工具类](./Step02_ConcurrentUtils.md)
