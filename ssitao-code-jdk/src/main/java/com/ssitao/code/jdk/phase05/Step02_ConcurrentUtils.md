# 步骤2：并发工具类 - JDK并发利器

---

## 2.1 并发工具类概述

```
JDK并发包(java.util.concurrent)核心组件：

同步器：
  - CountDownLatch    - 倒计时门栓
  - CyclicBarrier     - 循环栅栏
  - Semaphore        - 信号量
  - Exchanger        - 交换器

并发容器：
  - ConcurrentHashMap
  - CopyOnWriteArrayList
  - BlockingQueue

原子类：
  - AtomicInteger
  - AtomicLong
  - AtomicReference
```

---

## 2.2 CountDownLatch

### 2.2.1 基本概念

```java
// CountDownLatch：倒计时门栓
// 概念：初始化一个计数器，当计数器减到0时，等待的线程被唤醒
// 特点：一次性，不能重置

// 场景：
//  - 主线程等待多个子任务完成
//  - 多个线程等待某个初始化完成
```

### 2.2.2 使用示例

```java
// 示例：主线程等待子任务完成
class Worker implements Runnable {
    private CountDownLatch latch;
    private String name;

    public Worker(CountDownLatch latch, String name) {
        this.latch = latch;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            System.out.println(name + " 开始工作");
            Thread.sleep((long) (Math.random() * 1000));
            System.out.println(name + " 完成");
        } finally {
            latch.countDown();  // 计数器减1
        }
    }
}

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        // 启动3个工作线程
        for (int i = 1; i <= 3; i++) {
            new Thread(new Worker(latch, "Worker-" + i)).start();
        }

        // 主线程等待
        latch.await();  // 阻塞，直到计数器为0
        System.out.println("所有Worker完成，主线程继续");

        // 输出示例：
        // Worker-1 开始工作
        // Worker-2 开始工作
        // Worker-3 开始工作
        // Worker-2 完成
        // Worker-3 完成
        // Worker-1 完成
        // 所有Worker完成，主线程继续
    }
}
```

### 2.2.3 带超时的await

```java
// await(long timeout, TimeUnit unit)
// 如果超时，返回false

boolean completed = latch.await(5, TimeUnit.SECONDS);
if (completed) {
    System.out.println("所有任务完成");
} else {
    System.out.println("超时，还有任务未完成");
}
```

---

## 2.3 CyclicBarrier

### 2.3.1 基本概念

```java
// CyclicBarrier：循环栅栏
// 概念：一组线程互相等待，等所有人都到达后，一起继续执行
// 特点：可循环使用，可以传入一个Runnable

// 场景：
//  - 多线程计算，最后汇总结果
//  - 并行计算后合并
```

### 2.3.2 使用示例

```java
// 示例：多人同时到达后开始游戏

class Player implements Runnable {
    private CyclicBarrier barrier;
    private String name;

    public Player(CyclicBarrier barrier, String name) {
        this.barrier = barrier;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            System.out.println(name + " 到达，等待其他人...");
            barrier.await();  // 等待所有人
            System.out.println(name + " 开始游戏！");
        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("所有人到齐，游戏开始！");
        });

        for (int i = 1; i <= 3; i++) {
            new Thread(new Player(barrier, "Player-" + i)).start();
        }

        // 输出示例：
        // Player-1 到达，等待其他人...
        // Player-2 到达，等待其他人...
        // Player-3 到达，等待其他人...
        // 所有人到齐，游戏开始！
        // Player-2 开始游戏！
        // Player-1 开始游戏！
        // Player-3 开始游戏！
    }
}
```

### 2.3.3 CyclicBarrier vs CountDownLatch

| 特性 | CyclicBarrier | CountDownLatch |
|------|---------------|----------------|
| 用途 | 互相等待 | 主线程等待子任务 |
| 可重置 | 是 | 否 |
| 参与者 | 互相等待 | 等待一个信号 |
| 典型场景 | 并行计算合并 | 多个子任务完成后汇总 |

---

## 2.4 Semaphore

### 2.4.1 基本概念

```java
// Semaphore：信号量
// 概念：控制同时访问某个资源的线程数量
// 原理：计数器 + 许可机制

// 场景：
//  - 限流（同时N个请求）
//  - 连接池大小控制
//  - 资源访问控制
```

### 2.4.2 使用示例

```java
// 示例：限流访问

class Connection {
    private int id;
    public Connection(int id) { this.id = id; }
    @Override
    public String toString() { return "Connection-" + id; }
}

class ConnectionPool {
    private Semaphore semaphore;
    private List<Connection> connections = new ArrayList<>();

    public ConnectionPool(int poolSize) {
        semaphore = new Semaphore(poolSize, true);  // true=公平
        for (int i = 0; i < poolSize; i++) {
            connections.add(new Connection(i));
        }
    }

    public Connection getConnection() throws InterruptedException {
        semaphore.acquire();  // 获取许可
        Connection conn;
        synchronized (this) {
            conn = connections.remove(connections.size() - 1);
        }
        return conn;
    }

    public void releaseConnection(Connection conn) {
        synchronized (this) {
            connections.add(conn);
        }
        semaphore.release();  // 释放许可
    }
}

// 使用
ConnectionPool pool = new ConnectionPool(3);
for (int i = 0; i < 10; i++) {
    final int id = i;
    new Thread(() -> {
        try {
            Connection conn = pool.getConnection();
            System.out.println("Thread " + id + " using " + conn);
            Thread.sleep(1000);
            pool.releaseConnection(conn);
            System.out.println("Thread " + id + " released " + conn);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }).start();
}
```

### 2.4.3 其他acquire方法

```java
Semaphore semaphore = new Semaphore(3);

// 获取1个许可（阻塞）
semaphore.acquire();

// 获取多个许可（阻塞）
semaphore.acquire(2);

// 尝试获取（非阻塞）
if (semaphore.tryAcquire()) {
    // 成功获取
} else {
    // 获取失败，继续其他工作
}

// 带超时的尝试获取
if (semaphore.tryAcquire(5, TimeUnit.SECONDS)) {
    // 成功
}
```

---

## 2.5 Exchanger

### 2.5.1 基本概念

```java
// Exchanger：交换器
// 概念：两个线程交换数据
// 场景：数据同步、遗传算法等
```

### 2.5.2 使用示例

```java
// 示例：两个线程交换数据

class ExchangerDemo {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        new Thread(() -> {
            String data = "线程1的数据";
            try {
                System.out.println("线程1 准备交换");
                String received = exchanger.exchange(data);
                System.out.println("线程1 收到: " + received);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            String data = "线程2的数据";
            try {
                Thread.sleep(1000);  // 晚一点开始
                System.out.println("线程2 准备交换");
                String received = exchanger.exchange(data);
                System.out.println("线程2 收到: " + received);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // 输出：
        // 线程1 准备交换
        // 线程2 准备交换
        // 线程2 收到: 线程1的数据
        // 线程1 收到: 线程2的数据
    }
}
```

---

## 2.6 Phaser

### 2.6.1 基本概念

```java
// Phaser：阶段同步器（JDK 7+）
// 概念：类似CyclicBarrier，但可以分阶段，支持动态注册

// 场景：
//  - 多阶段任务
//  - 动态控制参与者数量
```

### 2.6.2 使用示例

```java
// 示例：分阶段处理任务

class PhaserDemo {
    public static void main(String[] args) throws InterruptedException {
        Phaser phaser = new Phaser(3);  // 3个参与者

        for (int i = 1; i <= 3; i++) {
            final int id = i;
            new Thread(() -> {
                // 阶段1
                System.out.println("阶段1 - 线程" + id + " 执行中");
                Thread.sleep((long) (Math.random() * 1000));
                phaser.arriveAndAwaitAdvance();  // 到达并等待

                // 阶段2
                System.out.println("阶段2 - 线程" + id + " 执行中");
                Thread.sleep((long) (Math.random() * 1000));
                phaser.arriveAndAwaitAdvance();  // 到达并等待

                // 阶段3
                System.out.println("阶段3 - 线程" + id + " 执行中");
                phaser.arriveAndDeregister();  // 完成并注销
            }).start();
        }

        // 等待所有阶段完成
        phaser.awaitAdvance(phaser.getPhase());
        System.out.println("所有阶段完成");
    }
}
```

---

## 2.7 并发容器

### 2.7.1 ConcurrentHashMap

```java
// ConcurrentHashMap：线程安全的HashMap

// 1. 初始化
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// 2. 基本操作
map.put("a", 1);
map.get("a");  // 1
map.remove("a");

// 3. 原子操作
map.putIfAbsent("a", 1);    // 不存在才插入
map.replace("a", 1, 2);     // 原子替换
map.compute("a", (k, v) -> v == null ? 1 : v + 1);  // JDK 8
map.merge("a", 1, Integer::sum);  // JDK 8

// 4. 批量操作（优化）
map.forEach((k, v) -> System.out.println(k + ":" + v));
map.entrySet().stream().forEach(...);

// 5. 与Hashtable/synchronizedMap对比
// Hashtable: 全表锁，性能差
// synchronizedMap: 同步整个Map，性能差
// ConcurrentHashMap: 分段锁，高并发性能好
```

### 2.7.2 CopyOnWriteArrayList

```java
// CopyOnWriteArrayList：写时复制的列表
// 读操作不加锁，写操作复制整个数组

CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

// 适用场景：
//  - 读多写少的并发场景
//  - 遍历操作远多于修改操作

// 注意：
//  - 内存开销大（每次写都复制）
//  - 不保证数据实时一致性
//  - 迭代器不支持remove

// 示例
List<String> readOnlyList = new CopyOnWriteArrayList<>();
readOnlyList.add("a");
for (String s : readOnlyList) {  // 安全遍历
    System.out.println(s);
}
```

### 2.7.3 BlockingQueue

```java
// BlockingQueue：阻塞队列
// 队列为空时取元素会阻塞
// 队列满时存元素会阻塞

// 1. 实现类
// - ArrayBlockingQueue：数组实现，有界
// - LinkedBlockingQueue：链表实现，可有界可无界
// - PriorityBlockingQueue：优先级队列
// - DelayQueue：延迟队列
// - SynchronousQueue：同步队列

// 2. 常用方法
BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);

// 插入
queue.put("a");       // 阻塞直到有空位
queue.offer("a");     // 非阻塞，立即返回
queue.offer("a", 5, TimeUnit.SECONDS);  // 超时版本

// 取出
String s = queue.take();       // 阻塞直到有元素
String s2 = queue.poll();      // 非阻塞
String s3 = queue.poll(5, TimeUnit.SECONDS);  // 超时版本

// 查看
queue.peek();  // 不移除
```

### 2.7.4 并发容器对比

| 容器 | 线程安全方式 | 适用场景 |
|------|-------------|----------|
| ConcurrentHashMap | 分段锁/CAS | 高并发读写的Map |
| CopyOnWriteArrayList | 写时复制 | 读多写少的List |
| ArrayBlockingQueue | ReentrantLock | 有界队列，生产者-消费者 |
| LinkedBlockingQueue | ReentrantLock | 可选有界/无界队列 |
| ConcurrentLinkedQueue | CAS | 无界队列，高并发 |

---

## 2.8 原子类

### 2.8.1 原子整数

```java
// AtomicInteger：原子整数
AtomicInteger counter = new AtomicInteger(0);

// 基本操作
counter.incrementAndGet();    // ++i
counter.decrementAndGet();    // --i
counter.getAndIncrement();    // i++
counter.getAndAdd(5);         // +=5
counter.addAndGet(5);         // 先加后返回

// CAS操作
counter.compareAndSet(0, 1);  // 如果当前值是0，则设为1

// 简单运算
int value = counter.get();
int newValue = value + 1;
if (!counter.compareAndSet(value, newValue)) {
    // 失败，重试
}

// 更新函数（JDK 8+）
counter.updateAndGet(x -> x / 2);  // 除2
counter.accumulateAndGet(5, Integer::sum);  // 累加
```

### 2.8.2 原子引用

```java
// AtomicReference：原子引用
AtomicReference<User> userRef = new AtomicReference<>();

User user = new User("Alice", 25);
userRef.set(user);

// CAS
userRef.compareAndSet(user, new User("Bob", 30));

// 获取
User current = userRef.get();

// 原子更新引用内的字段（JDK 8+）
userRef.updateAndGet(u -> new User(u.getName(), u.getAge() + 1));
```

### 2.8.3 原子数组

```java
// AtomicIntegerArray：原子整数数组
AtomicIntegerArray array = new AtomicIntegerArray(10);

array.get(0);              // 获取索引0
array.set(0, 5);           // 设置索引0为5
array.incrementAndGet(0);   // 索引0++
array.compareAndSet(0, 5, 6);  // CAS
```

---

## 2.9 练习题

```java
// 1. 用CountDownLatch实现：主线程等待10个子任务完成

// 2. 用CyclicBarrier实现：5个人到齐后开始吃饭

// 3. 用Semaphore实现：最多3个线程同时访问资源

// 4. 说明ConcurrentHashMap和Hashtable的区别

// 5. 用原子类实现一个线程安全的计数器
```

---

## 2.10 参考答案

```java
// 1. CountDownLatch等待10个子任务
CountDownLatch latch = new CountDownLatch(10);
for (int i = 0; i < 10; i++) {
    new Thread(() -> {
        // 子任务
        latch.countDown();
    }).start();
}
latch.await();

// 2. 5个人到齐后开始吃饭
CyclicBarrier barrier = new CyclicBarrier(5);
for (int i = 0; i < 5; i++) {
    new Thread(() -> {
        System.out.println("人" + i + "到达");
        barrier.await();  // 等待其他人
        System.out.println("开始吃饭");
    }).start();
}

// 3. Semaphore限流
Semaphore semaphore = new Semaphore(3);
for (int i = 0; i < 10; i++) {
    new Thread(() -> {
        try {
            semaphore.acquire();
            // 访问资源
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }).start();
}

// 4. ConcurrentHashMap vs Hashtable
// ConcurrentHashMap: 分段锁，粒度细，并发高
// Hashtable: 全表锁，粒度粗，并发低

// 5. 线程安全计数器
class SafeCounter {
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public int get() {
        return count.get();
    }
}
```

---

[返回目录](./README.md) | [下一步：线程安全与锁](./Step03_ThreadSafety.md)
