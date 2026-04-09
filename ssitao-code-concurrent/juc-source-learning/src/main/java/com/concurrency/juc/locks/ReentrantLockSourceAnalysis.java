package com.concurrency.juc.locks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantLock 源码分析 + 实际使用示例
 */
public class ReentrantLockSourceAnalysis {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ReentrantLock 源码分析 + 使用示例 ===\n");

        // 1. 基本使用示例
        System.out.println("【1. 基本互斥锁使用】");
        basicMutexUsage();

        // 2. 可重入示例
        System.out.println("\n【2. 可重入锁示例】");
        reentrantDemo();

        // 3. 公平锁 vs 非公平锁
        System.out.println("\n【3. 公平锁 vs 非公平锁】");
        fairVsNonfair();

        // 4. tryLock 超时示例
        System.out.println("\n【4. tryLock 超时示例】");
        tryLockTimeout();

        // 5. Condition 条件变量
        System.out.println("\n【5. Condition 条件变量示例】");
        conditionDemo();

        // 6. 银行转账案例
        System.out.println("\n【6. 银行转账案例】");
        bankTransfer();
    }

    /**
     * 1. 基本互斥锁使用
     */
    private static void basicMutexUsage() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Counter counter = new Counter();

        int threadCount = 10;
        int incrementsPerThread = 1000;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    lock.lock();
                    try {
                        counter.value++;
                    } finally {
                        lock.unlock();
                    }
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        System.out.println("  线程安全计数结果: " + counter.value);
        System.out.println("  期望结果: " + (threadCount * incrementsPerThread));
    }

    private static class Counter {
        int value = 0;
    }

    /**
     * 2. 可重入锁示例
     */
    private static void reentrantDemo() {
        ReentrantLock lock = new ReentrantLock();

        lock.lock();
        System.out.println("  外层获取锁, holdCount=" + lock.getHoldCount());

        lock.lock();
        System.out.println("  中层获取锁, holdCount=" + lock.getHoldCount());

        lock.lock();
        System.out.println("  内层获取锁, holdCount=" + lock.getHoldCount());

        lock.unlock();
        System.out.println("  内层释放锁, holdCount=" + lock.getHoldCount());

        lock.unlock();
        System.out.println("  中层释放锁, holdCount=" + lock.getHoldCount());

        lock.unlock();
        System.out.println("  外层释放锁, holdCount=" + lock.getHoldCount());
    }

    /**
     * 3. 公平锁 vs 非公平锁
     */
    private static void fairVsNonfair() throws InterruptedException {
        System.out.println("  测试方式: 线程先启动但后获取锁, 观察获取顺序");
        System.out.println();

        // 非公平锁
        testLockFairness(new ReentrantLock(false), "非公平锁", 3);

        Thread.sleep(100);

        // 公平锁
        testLockFairness(new ReentrantLock(true), "公平锁", 3);
    }

    private static void testLockFairness(ReentrantLock lock, String name, int threadCount)
            throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        String[] order = new String[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            final int sleepTime = (threadCount - i) * 10; // 后启动的先sleep完
            new Thread(() -> {
                try {
                    Thread.sleep(sleepTime); // 故意让启动顺序不同
                    startLatch.await();       // 等待同时开始
                    lock.lock();
                    order[index] = Thread.currentThread().getName();
                    lock.unlock();
                } catch (InterruptedException e) {
                } finally {
                    endLatch.countDown();
                }
            }, name + "-Thread-" + i).start();
        }

        Thread.sleep(50);
        startLatch.countDown(); // 全部同时开始抢锁
        endLatch.await();

        System.out.println("  " + name + " 获取顺序: " + java.util.Arrays.toString(order));
    }

    /**
     * 4. tryLock 超时示例
     */
    private static void tryLockTimeout() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        // 线程1 获取锁不释放
        lock.lock();
        System.out.println("  线程1 获取锁成功");

        // 线程2 尝试获取，2秒超时
        Thread thread2 = new Thread(() -> {
            try {
                boolean acquired = lock.tryLock(2, TimeUnit.SECONDS);
                if (acquired) {
                    System.out.println("  线程2 获取锁成功");
                    lock.unlock();
                } else {
                    System.out.println("  线程2 获取锁超时失败");
                }
            } catch (InterruptedException e) {
            }
        }, "TryLock-Thread");

        thread2.start();
        thread2.join();

        lock.unlock();
        System.out.println("  线程1 释放锁");
    }

    /**
     * 5. Condition 条件变量示例 - 生产者消费者
     */
    private static void conditionDemo() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        java.util.concurrent.locks.Condition notEmpty = lock.newCondition();
        java.util.concurrent.locks.Condition notFull = lock.newCondition();

        java.util.Queue<Integer> queue = new java.util.LinkedList<>();
        int capacity = 5;

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // 生产者
        for (int i = 0; i < 2; i++) {
            final int producerId = i;
            executor.submit(() -> {
                for (int j = 0; j < 5; j++) {
                    lock.lock();
                    try {
                        while (queue.size() >= capacity) {
                            System.out.println("  生产者" + producerId + " 队列满，等待...");
                            notFull.await();
                        }
                        int value = producerId * 100 + j;
                        queue.offer(value);
                        System.out.println("  生产者" + producerId + " 生产: " + value);
                        notEmpty.signal();
                    } catch (InterruptedException e) {
                    } finally {
                        lock.unlock();
                    }
                }
            });
        }

        // 消费者
        for (int i = 0; i < 2; i++) {
            final int consumerId = i;
            executor.submit(() -> {
                for (int j = 0; j < 5; j++) {
                    lock.lock();
                    try {
                        while (queue.isEmpty()) {
                            System.out.println("  消费者" + consumerId + " 队列空，等待...");
                            notEmpty.await();
                        }
                        int value = queue.poll();
                        System.out.println("  消费者" + consumerId + " 消费: " + value);
                        notFull.signal();
                    } catch (InterruptedException e) {
                    } finally {
                        lock.unlock();
                    }
                }
            });
        }

        Thread.sleep(1000);
        executor.shutdownNow();
    }

    /**
     * 6. 银行转账案例 - 演示死锁避免
     */
    private static void bankTransfer() throws InterruptedException {
        class Account {
            private int balance;
            private String name;
            Account(String name, int balance) {
                this.name = name;
                this.balance = balance;
            }
            public String getName() { return name; }
            public int getBalance() { return balance; }
        }

        Account a = new Account("账户A", 1000);
        Account b = new Account("账户B", 1000);

        ReentrantLock lockA = new ReentrantLock();
        ReentrantLock lockB = new ReentrantLock();

        // 转账： A -> B
        Runnable transferAB = () -> {
            lockA.lock();
            try {
                Thread.sleep(10); // 模拟操作延迟
                lockB.lock();
                try {
                    int amount = 100;
                    if (a.getBalance() >= amount) {
                        // 实际转账逻辑
                        System.out.println("  " + Thread.currentThread().getName() +
                                " 从 " + a.getName() + " 转账 " + amount + " 到 " + b.getName());
                    }
                } finally {
                    lockB.unlock();
                }
            } catch (InterruptedException e) {
            } finally {
                lockA.unlock();
            }
        };

        // 转账： B -> A
        Runnable transferBA = () -> {
            lockB.lock();
            try {
                Thread.sleep(10);
                lockA.lock();
                try {
                    int amount = 100;
                    if (b.getBalance() >= amount) {
                        System.out.println("  " + Thread.currentThread().getName() +
                                " 从 " + b.getName() + " 转账 " + amount + " 到 " + a.getName());
                    }
                } finally {
                    lockA.unlock();
                }
            } catch (InterruptedException e) {
            } finally {
                lockB.unlock();
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 5; i++) {
            executor.submit(transferAB, "TransferAB-" + i);
            executor.submit(transferBA, "TransferBA-" + i);
        }

        Thread.sleep(500);
        executor.shutdown();
    }
}
