package com.ssitao.code.jdk.phase05.util;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

/**
 * 线程安全与锁示例
 */
public class ThreadSafetyDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Thread Safety Demo ===\n");

        // 1. volatile示例
        demonstrateVolatile();

        // 2. ReentrantLock示例
        demonstrateReentrantLock();

        // 3. ReadWriteLock示例
        demonstrateReadWriteLock();

        // 4. StampedLock示例
        demonstrateStampedLock();

        // 5. 线程池示例
        demonstrateThreadPool();
    }

    private static void demonstrateVolatile() throws Exception {
        System.out.println("--- Volatile ---");

        VolatileFlag flag = new VolatileFlag();

        Thread reader = new Thread(() -> {
            int count = 0;
            while (!flag.isRunning()) {
                count++;
                Thread.yield();
            }
            System.out.println("Reader detected stop after " + count + " iterations");
        });

        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            flag.setRunning(true);
            System.out.println("Writer set running=true");
        });

        reader.start();
        writer.start();
        reader.join();
        writer.join();
        System.out.println();
    }

    private static void demonstrateReentrantLock() throws Exception {
        System.out.println("--- ReentrantLock ---");

        ReentrantLock lock = new ReentrantLock();
        Counter counter = new Counter(lock);

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    counter.increment();
                }
            });
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Final count: " + counter.getCount());
        System.out.println("Lock is fair: " + lock.isFair());
        System.out.println("Lock held by current thread: " + lock.isHeldByCurrentThread());
        System.out.println();
    }

    private static void demonstrateReadWriteLock() throws Exception {
        System.out.println("--- ReadWriteLock ---");

        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        SharedData data = new SharedData(rwLock);

        // 多个读线程
        for (int i = 0; i < 3; i++) {
            final int id = i;
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    String value = data.read();
                    System.out.println("Reader " + id + " read: " + value);
                }
            }, "Reader-" + i).start();
        }

        // 多个写线程
        for (int i = 0; i < 2; i++) {
            final int id = i;
            new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    data.write("Data-" + id + "-" + j);
                    System.out.println("Writer " + id + " wrote: Data-" + id + "-" + j);
                }
            }, "Writer-" + i).start();
        }

        Thread.sleep(3000);
        System.out.println();
    }

    private static void demonstrateStampedLock() throws Exception {
        System.out.println("--- StampedLock ---");

        StampedLock stampedLock = new StampedLock();
        Point point = new Point(stampedLock);

        // 乐观读
        System.out.println("Initial: " + point.getX() + ", " + point.getY());

        // 写操作
        point.move(10, 20);
        System.out.println("After move: " + point.getX() + ", " + point.getY());

        // 读操作
        double distance = point.distanceFromOrigin();
        System.out.println("Distance: " + distance);

        System.out.println();
    }

    private static void demonstrateThreadPool() throws Exception {
        System.out.println("--- Thread Pool ---");

        // 创建线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,                      // corePoolSize
            4,                      // maximumPoolSize
            60,                     // keepAliveTime
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            new ThreadFactory() {
                private int count = 0;
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "Pool-Thread-" + (++count));
                    return t;
                }
            },
            new ThreadPoolExecutor.AbortPolicy()
        );

        // 提交任务
        Future<?>[] futures = new Future[5];
        for (int i = 0; i < 5; i++) {
            final int id = i;
            futures[i] = executor.submit(() -> {
                System.out.println("Task " + id + " executing in " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return id * 10;
            });
        }

        // 获取结果
        for (int i = 0; i < 5; i++) {
            System.out.println("Task " + i + " result: " + futures[i].get());
        }

        // 关闭线程池
        executor.shutdown();
        if (executor.awaitTermination(10, TimeUnit.SECONDS)) {
            System.out.println("Thread pool terminated");
        }
    }

    // ===== 辅助类 =====

    static class VolatileFlag {
        private volatile boolean running = false;

        public boolean isRunning() {
            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
    }

    static class Counter {
        private int count = 0;
        private final ReentrantLock lock;

        public Counter(ReentrantLock lock) {
            this.lock = lock;
        }

        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }

        public int getCount() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }

    static class SharedData {
        private String data = "Initial";
        private final ReadWriteLock rwLock;

        public SharedData(ReadWriteLock rwLock) {
            this.rwLock = rwLock;
        }

        public String read() {
            rwLock.readLock().lock();
            try {
                Thread.sleep(100);  // 模拟读操作
                return data;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            } finally {
                rwLock.readLock().unlock();
            }
        }

        public void write(String data) {
            rwLock.writeLock().lock();
            try {
                Thread.sleep(200);  // 模拟写操作
                this.data = data;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                rwLock.writeLock().unlock();
            }
        }
    }

    static class Point {
        private int x, y;
        private final StampedLock stampedLock;

        public Point(StampedLock stampedLock) {
            this.stampedLock = stampedLock;
            this.x = 0;
            this.y = 0;
        }

        public void move(int dx, int dy) {
            long stamp = stampedLock.writeLock();
            try {
                x += dx;
                y += dy;
            } finally {
                stampedLock.unlockWrite(stamp);
            }
        }

        public double distanceFromOrigin() {
            long stamp = stampedLock.tryOptimisticRead();
            int dx = x;
            int dy = y;
            if (!stampedLock.validate(stamp)) {
                stamp = stampedLock.readLock();
                try {
                    dx = x;
                    dy = y;
                } finally {
                    stampedLock.unlockRead(stamp);
                }
            }
            return Math.sqrt(dx * dx + dy * dy);
        }

        public int getX() {
            long stamp = stampedLock.readLock();
            try {
                return x;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }

        public int getY() {
            long stamp = stampedLock.readLock();
            try {
                return y;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
    }
}
