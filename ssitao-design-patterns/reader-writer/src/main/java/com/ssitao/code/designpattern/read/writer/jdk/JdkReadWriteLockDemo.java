package com.ssitao.code.designpattern.read.writer.jdk;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * JDK ReentrantReadWriteLock 详解
 *
 * ReentrantReadWriteLock 特性：
 * 1. 读锁可重入 - 同一个线程可以多次获取读锁
 * 2. 写锁可重入 - 同一个线程可以多次获取写锁
 * 3. 锁降级 - 写锁可以降级为读锁，但读锁不能升级为写锁
 * 4. 公平和非公平 - 支持公平和非公平模式
 *
 * 核心方法：
 * - readLock(): 获取读锁
 * - writeLock(): 获取写锁
 * - getReadLockCount(): 获取当前有多少线程持有读锁
 * - isWriteLocked(): 检查是否有线程持有写锁
 *
 * @author ssitao
 */
public class JdkReadWriteLockDemo {

    public static void main(String[] args) {
        System.out.println("=== JDK ReentrantReadWriteLock 详解 ===\n");

        // 示例1：读写锁基本特性
        System.out.println("1. 读写锁基本特性");
        basicFeaturesDemo();

        // 示例2：缓存实现示例
        System.out.println("\n2. 缓存实现示例");
        cacheDemo();

        // 示例3：锁降级示例
        System.out.println("\n3. 锁降级示例");
        lockDowngradeDemo();

        // 示例4：读多写少场景性能对比
        System.out.println("\n4. 读多写少场景");
        readHeavyDemo();
    }

    /**
     * 读写锁基本特性演示
     */
    private static void basicFeaturesDemo() {
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

        // 1. 读锁重入
        System.out.println("--- 读锁重入 ---");
        rwLock.readLock().lock();
        System.out.println("第一次获取读锁，持有计数: " + rwLock.getReadLockCount());

        rwLock.readLock().lock();
        System.out.println("第二次获取读锁，持有计数: " + rwLock.getReadLockCount());

        rwLock.readLock().unlock();
        System.out.println("释放一次读锁，持有计数: " + rwLock.getReadLockCount());

        rwLock.readLock().unlock();
        System.out.println("完全释放读锁，持有计数: " + rwLock.getReadLockCount());

        // 2. 写锁重入
        System.out.println("\n--- 写锁重入 ---");
        rwLock.writeLock().lock();
        System.out.println("获取写锁，写锁持有: " + rwLock.isWriteLockedByCurrentThread());

        rwLock.writeLock().lock();
        System.out.println("再次获取写锁，写锁持有: " + rwLock.isWriteLockedByCurrentThread());

        rwLock.writeLock().unlock();
        System.out.println("释放一次写锁，写锁仍被持有");

        rwLock.writeLock().unlock();
        System.out.println("完全释放写锁");
    }

    /**
     * 使用读写锁实现缓存
     * 演示如何利用读写锁提升缓存读取性能
     */
    private static void cacheDemo() {
        Cache<String, String> cache = new Cache<>();

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 多个线程读取缓存
        for (int i = 0; i < 5; i++) {
            final int id = i;
            executor.submit(() -> {
                String result = cache.get("key1");
                System.out.println("读者-" + id + " 读取: " + result);
            });
        }

        // 一个线程更新缓存
        executor.submit(() -> {
            cache.put("key1", "新值");
            System.out.println("写者 更新缓存");
        });

        executor.shutdown();
        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 锁降级示例
     * 写锁降级为读锁，允许在持有写锁的同时获取读锁
     */
    private static void lockDowngradeDemo() {
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        String data = "原始数据";

        // 锁降级流程
        rwLock.writeLock().lock();
        try {
            System.out.println("获取写锁，更新数据...");
            data = "更新后的数据";

            // 在释放写锁前获取读锁 - 锁降级
            rwLock.readLock().lock();
            try {
                System.out.println("降级为读锁，读取数据: " + data);
                // 此时其他线程无法获取写锁，但可以获取读锁
            } finally {
                rwLock.readLock().unlock();
            }
        } finally {
            rwLock.writeLock().unlock();
        }

        System.out.println("完全释放锁");
    }

    /**
     * 读多写少场景演示
     * 展示读写锁在读多写少场景下的性能优势
     */
    private static void readHeavyDemo() {
        Counter counter = new Counter();
        ExecutorService executor = Executors.newFixedThreadPool(20);

        long startTime = System.currentTimeMillis();

        // 95% 读操作，5% 写操作
        for (int i = 0; i < 100; i++) {
            final int id = i;
            if (id % 20 == 0) {
                // 5% 写操作
                executor.submit(() -> {
                    counter.increment();
                });
            } else {
                // 95% 读操作
                executor.submit(() -> {
                    counter.get();
                });
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
    }
}

/**
 * 缓存实现类
 * 使用读写锁保护，支持并发读和独占写
 */
class Cache<K, V> {
    private final Map<K, V> cache = new HashMap<>();
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * 读取缓存 - 多个线程可以同时读
     */
    public V get(K key) {
        rwLock.readLock().lock();
        try {
            // 模拟缓存未命中，需要从数据源加载
            if (!cache.containsKey(key)) {
                // 注意：这里不能直接获取写锁，会造成死锁
                // 实际应用中可以使用 release->acquire write 的模式
                return null;
            }
            return cache.get(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 写入缓存 - 需要独占锁
     */
    public void put(K key, V value) {
        rwLock.writeLock().lock();
        try {
            cache.put(key, value);
            System.out.println("缓存已更新: " + key + " = " + value);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * 清除缓存
     */
    public void clear() {
        rwLock.writeLock().lock();
        try {
            cache.clear();
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}

/**
 * 计数器 - 使用读写锁保护
 */
class Counter {
    private int count = 0;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * 获取计数值 - 读操作，可并发
     */
    public int get() {
        rwLock.readLock().lock();
        try {
            return count;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 增加计数值 - 写操作，独占
     */
    public void increment() {
        rwLock.writeLock().lock();
        try {
            count++;
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
