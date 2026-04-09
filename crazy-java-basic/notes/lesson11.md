# 第11课：多线程

## 核心概念

### 11.1 线程创建
1. 继承Thread类
2. 实现Runnable接口
3. 实现Callable接口（带返回值）

### 11.2 线程状态
```
NEW → RUNNABLE → BLOCKED/WAITING → TERMINATED
```

### 11.3 线程同步
- synchronized：同步方法/代码块
- Lock接口：ReentrantLock等
- volatile：保证可见性

### 11.4 线程通信
- wait()/notify()/notifyAll()
- await()/signal()/signalAll()

## 代码示例

### 示例1：线程创建方式
```java
// 方式1：继承Thread
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("继承Thread创建线程");
    }
}

// 方式2：实现Runnable
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("实现Runnable创建线程");
    }
}

// 方式3：实现Callable
class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
        return sum;
    }
}

public class ThreadCreateDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 方式1
        Thread t1 = new MyThread();
        t1.start();

        // 方式2
        Thread t2 = new Thread(new MyRunnable());
        t2.start();

        // 方式3（带返回值）
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(new MyCallable());
        System.out.println("计算结果：" + future.get());
        executor.shutdown();
    }
}
```

### 示例2：线程同步
```java
public class SyncDemo {
    private int count = 0;

    // 同步方法
    public synchronized void increment() {
        count++;
    }

    public void doTask() {
        // 同步代码块
        synchronized (this) {
            count++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SyncDemo demo = new SyncDemo();
        Thread[] threads = new Thread[1000];

        for (int i = 0; i < 1000; i++) {
            threads[i] = new Thread(() -> demo.increment());
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("count = " + demo.count);  // 应该是1000
    }
}
```

### 示例3：Lock接口
```java
import java.util.concurrent.locks.*;

public class LockDemo {
    private final ReentrantLock lock = new ReentrantLock();
    private int count = 0;

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();  // 必须在finally中释放
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo demo = new LockDemo();
        CountDownLatch latch = new CountDownLatch(1000);

        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                demo.increment();
                latch.countDown();
            }).start();
        }

        latch.await();
        System.out.println("count = " + demo.count);
    }
}
```

### 示例4：生产者-消费者
```java
import java.util.*;

public class ProducerConsumerDemo {
    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();
        int maxSize = 5;

        Thread producer = new Thread(() -> {
            int value = 0;
            while (true) {
                synchronized (queue) {
                    while (queue.size() >= maxSize) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.offer(value);
                    System.out.println("生产：" + value++);
                    queue.notifyAll();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int value = queue.poll();
                    System.out.println("消费：" + value);
                    queue.notifyAll();
                }
            }
        });

        producer.start();
        consumer.start();
    }
}
```

### 示例5：线程池
```java
import java.util.concurrent.*;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // 提交任务
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executor.execute(() -> {
                System.out.println("任务" + taskId + "由" +
                    Thread.currentThread().getName() + "执行");
            });
        }

        executor.shutdown();

        // 使用ThreadPoolExecutor
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            2,  // corePoolSize
            4,  // maximumPoolSize
            60, // keepAliveTime
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
```

### 示例6：volatile保证可见性
```java
public class VolatileDemo {
    // volatile保证可见性：一个线程修改，其他线程立即可见
    private volatile boolean flag = false;

    public void writer() {
        flag = true;  // 写入主内存
    }

    public void reader() {
        if (flag) {  // 从主内存读取
            System.out.println("读取到flag=true");
        }
    }
}
```

## 线程安全类

| 类 | 说明 |
|----|------|
| ConcurrentHashMap | 高效并发Map |
| CopyOnWriteArrayList | 写时复制List |
| BlockingQueue | 阻塞队列 |
| AtomicInteger | 原子操作Integer |

## 常见面试题

1. **synchronized和Lock的区别？**
   - synchronized是关键字，Lock是接口
   - synchronized自动释放，Lock需手动
   - Lock支持公平锁、tryLock

2. **wait()和sleep()的区别？**
   - wait()释放锁，sleep()不释放
   - wait()用于同步块中，sleep()可在任意位置

3. **什么是死锁？如何避免？**
   - 多个线程相互等待对方持有的锁
   - 按顺序获取锁、使用超时

## 练习题

1. 实现一个线程安全的计数器
2. 用线程池实现任务调度
3. 用wait/notify实现一个简易信号灯

## 要点总结

- 线程创建有三种方式
- 同步保证线程安全
- Lock比synchronized更灵活
- 线程池管理线程生命周期
- volatile保证可见性
