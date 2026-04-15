# 第16课：并发编程

## 核心概念

### 16.1 并发工具类
- ExecutorService：线程池
- CountDownLatch：倒计时门闩
- CyclicBarrier：循环屏障
- Semaphore：信号量
- Phaser：阶段同步器

### 16.2 并发集合
- ConcurrentHashMap
- CopyOnWriteArrayList
- BlockingQueue
- ConcurrentLinkedQueue

### 16.3 原子变量
- AtomicInteger
- AtomicLong
- AtomicReference

### 16.4 Fork/Join框架
- 分治思想
- RecursiveTask/RecursiveAction

## 代码示例

### 示例1：ExecutorService详解
```java
import java.util.concurrent.*;

public class ExecutorDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. FixedThreadPool - 固定大小线程池
        ExecutorService fixedPool = Executors.newFixedThreadPool(4);

        // 2. CachedThreadPool - 缓存线程池
        ExecutorService cachedPool = Executors.newCachedThreadPool();

        // 3. SingleThreadExecutor - 单线程池
        ExecutorService singlePool = Executors.newSingleThreadExecutor();

        // 4. ScheduledThreadPool - 定时任务
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(2);

        // 提交任务
        Future<?> future = fixedPool.submit(() -> {
            System.out.println("任务执行中...");
        });

        // 获取结果
        String result = fixedPool.submit(() -> "任务结果", () -> "默认值").get();

        // Callable带返回值
        Future<Integer> intFuture = fixedPool.submit(() -> {
            Thread.sleep(1000);
            return 42;
        });
        System.out.println(intFuture.get());  // 阻塞等待

        // 关闭线程池
        fixedPool.shutdown();
        if (!fixedPool.awaitTermination(10, TimeUnit.SECONDS)) {
            fixedPool.shutdownNow();
        }
    }
}
```

### 示例2：CountDownLatch
```java
import java.util.concurrent.*;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        int workerCount = 3;
        CountDownLatch latch = new CountDownLatch(workerCount);

        // 启动3个工人
        for (int i = 0; i < workerCount; i++) {
            final int workerId = i;
            new Thread(() -> {
                try {
                    System.out.println("工人" + workerId + "开始工作");
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println("工人" + workerId + "完成工作");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();  // 工作完成，计数减1
                }
            }).start();
        }

        // 等待所有工人完成
        latch.await();
        System.out.println("所有工人都完成了，监工开始检查");
    }
}
```

### 示例3：CyclicBarrier
```java
import java.util.concurrent.*;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        int partyCount = 3;
        CyclicBarrier barrier = new CyclicBarrier(partyCount, () -> {
            System.out.println("所有人都到了，比赛开始！");
        });

        for (int i = 0; i < partyCount; i++) {
            final int runnerId = i;
            new Thread(() -> {
                try {
                    System.out.println("选手" + runnerId + "准备好了");
                    barrier.await();  // 等待其他人
                    System.out.println("选手" + runnerId + "起跑！");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```

### 示例4：Semaphore
```java
import java.util.concurrent.*;

public class SemaphoreDemo {
    public static void main(String[] args) {
        int permits = 2;  // 同时最多2个许可
        Semaphore semaphore = new Semaphore(permits);

        for (int i = 0; i < 5; i++) {
            final int carId = i;
            new Thread(() -> {
                try {
                    semaphore.acquire();  // 获取许可
                    System.out.println("车辆" + carId + "进入停车场");
                    Thread.sleep((long) (Math.random() * 5000));
                    System.out.println("车辆" + carId + "离开停车场");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release();  // 释放许可
                }
            }).start();
        }
    }
}
```

### 示例5：ConcurrentHashMap
```java
import java.util.concurrent.*;

public class ConcurrentMapDemo {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // 并发写入
        for (int i = 0; i < 1000; i++) {
            final int id = i;
            new Thread(() -> {
                map.put("key" + id, id);
            }).start();
        }

        Thread.sleep(1000);
        System.out.println("Map大小：" + map.size());

        // 原子操作
        map.putIfAbsent("test", 0);
        map.computeIfAbsent("counter", k -> 0);
        map.merge("counter", 1, Integer::sum);

        // 安全遍历
        map.forEach(1, (k, v) ->
            System.out.println(k + ":" + v)
        );
    }
}
```

### 示例6：原子变量
```java
import java.util.concurrent.atomic.*;

public class AtomicDemo {
    public static void main(String[] args) throws InterruptedException {
        // AtomicInteger
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> counter.incrementAndGet()).start();
        }
        Thread.sleep(1000);
        System.out.println("计数器：" + counter.get());  // 1000

        // AtomicReference
        AtomicReference<String> ref = new AtomicReference<>("initial");
        ref.compareAndSet("initial", "updated");
        System.out.println(ref.get());  // updated

        // AtomicIntegerArray
        AtomicIntegerArray array = new AtomicIntegerArray(10);
        array.getAndIncrement(0);
        System.out.println(array.get(0));  // 1

        // LongAdder（JDK 8+）- 高并发场景性能更好
        LongAdder longAdder = new LongAdder();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> longAdder.increment()).start();
        }
        Thread.sleep(1000);
        System.out.println("LongAdder：" + longAdder.sum());
    }
}
```

### 示例7：Fork/Join框架
```java
import java.util.concurrent.*;
import java.util.Arrays;
import java.util.List;

public class ForkJoinDemo {
    static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 1000;
        private List<Long> data;

        SumTask(List<Long> data) {
            this.data = data;
        }

        @Override
        protected Long compute() {
            if (data.size() <= THRESHOLD) {
                return data.stream().mapToLong(Long::longValue).sum();
            }

            int mid = data.size() / 2;
            List<Long> left = data.subList(0, mid);
            List<Long> right = data.subList(mid, data.size());

            SumTask leftTask = new SumTask(left);
            SumTask rightTask = new SumTask(right);

            leftTask.fork();
            rightTask.fork();

            return leftTask.join() + rightTask.join();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();

        List<Long> data = Arrays.asList(
            1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L
        );

        SumTask task = new SumTask(data);
        Future<Long> future = pool.submit(task);

        System.out.println("总和：" + future.get());  // 55
        pool.shutdown();
    }
}
```

### 示例8：CompletableFuture
```java
import java.util.concurrent.*;

public class CompletableFutureDemo {
    public static void main(String[] args) throws Exception {
        // 创建异步任务
        CompletableFuture<String> future = CompletableFuture
            .supplyAsync(() -> {
                System.out.println("异步任务执行");
                return "Hello";
            });

        // thenApply - 转换结果
        CompletableFuture<Integer> lengthFuture = future
            .thenApply(String::length);

        // thenCompose - 链式调用
        CompletableFuture<String> composedFuture = future
            .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));

        // thenCombine - 组合两个Future
        CompletableFuture<String> combined = future
            .thenCombine(
                CompletableFuture.completedFuture(" Java"),
                (s1, s2) -> s1 + s2
            );

        // 异常处理
        CompletableFuture<Integer> errorFuture = CompletableFuture
            .supplyAsync(() -> {
                if (true) throw new RuntimeException("出错了");
                return 10;
            })
            .exceptionally(ex -> {
                System.out.println("异常：" + ex.getMessage());
                return 0;
            });

        // 等待结果
        System.out.println(lengthFuture.get());   // 5
        System.out.println(composedFuture.get()); // Hello World
        System.out.println(combined.get());       // Hello Java
        System.out.println(errorFuture.get());    // 0
    }
}
```

## 并发容器对比

| 容器 | 特点 | 适用场景 |
|------|------|----------|
| ConcurrentHashMap | 分段锁，高并发 | 大量读写 |
| CopyOnWriteArrayList | 读不加锁，写复制 | 读多写少 |
| BlockingQueue | 阻塞操作 | 生产者-消费者 |
| ConcurrentLinkedQueue | 无锁队列 | 高并发队列 |

## 常见面试题

1. **synchronized和ReentrantLock的区别？**
   - synchronized是关键字，Lock是接口
   - ReentrantLock可中断、可超时
   - ReentrantLock可公平锁

2. **ConcurrentHashMap如何保证线程安全？**
   - JDK7：Segment分段锁
   - JDK8：CAS+synchronized

3. **什么是线程安全？**
   - 多个线程访问时，始终表现正确

## 练习题

1. 使用CompletableFuture实现异步编排
2. 实现一个简单的线程池
3. 用Phaser实现多阶段任务

## 要点总结

- ExecutorService管理线程池
- 并发容器替代同步容器
- 原子变量保证原子操作
- Fork/Join处理分治任务
- CompletableFuture简化异步编程
