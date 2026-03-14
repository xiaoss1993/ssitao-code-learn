package com.ssitao.code.designpattern.read.writer.basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * 读写锁基础示例
 *
 * 读写锁的核心原理：
 * 1. 读操作可以被多个线程同时执行（并发读）
 * 2. 写操作需要独占锁，其他线程不能读或写
 * 3. 读写互斥：读锁和写锁不能同时持有
 *
 * 适用场景：
 * - 读多写少的场景（如配置信息、缓存数据）
 * - 需要同时保证数据一致性和并发性能
 *
 * @author ssitao
 */
public class BasicReaderWriterDemo {

    public static void main(String[] args) {
        System.out.println("=== 读写锁基础示例 ===\n");

        // 示例1：基本读写锁使用
        System.out.println("1. 基本读写锁使用");
        basicReadWriteLockDemo();

        // 示例2：读并发、写互斥演示
        System.out.println("\n2. 读并发、写互斥演示");
        concurrentReadExclusiveWriteDemo();
    }

    /**
     * 基本读写锁使用示例
     * 演示ReentrantReadWriteLock的基本API
     */
    private static void basicReadWriteLockDemo() {
        // 创建读写锁
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        // 获取读锁和写锁
        ReadLock readLock = readWriteLock.readLock();
        WriteLock writeLock = readWriteLock.writeLock();

        // 模拟数据
        String data = "初始数据";

        // 读操作 - 使用读锁
        readLock.lock();
        try {
            System.out.println("读取数据: " + data);
        } finally {
            readLock.unlock();
        }

        // 写操作 - 使用写锁
        writeLock.lock();
        try {
            data = "更新后的数据";
            System.out.println("写入数据: " + data);
        } finally {
            writeLock.unlock();
        }

        // 再次读取
        readLock.lock();
        try {
            System.out.println("再次读取: " + data);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 演示读并发、写互斥
     * 多个读者可以同时读取，但写者需要独占访问
     */
    private static void concurrentReadExclusiveWriteDemo() {
        // 创建共享数据容器
        SharedData sharedData = new SharedData();

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(8);

        // 启动5个读者 - 应该并发执行
        for (int i = 0; i < 5; i++) {
            final int readerId = i;
            executor.submit(() -> sharedData.read(readerId));
        }

        // 启动3个写者 - 应该串行执行
        for (int i = 0; i < 3; i++) {
            final int writerId = i;
            executor.submit(() -> sharedData.write(writerId, "数据-" + writerId));
        }

        // 关闭线程池
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 共享数据类 - 使用读写锁保护
 */
class SharedData {
    private String data;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public SharedData() {
        this.data = "初始值";
    }

    /**
     * 读操作 - 多个线程可以同时读
     */
    public void read(int readerId) {
        lock.readLock().lock();
        try {
            System.out.println("读者-" + readerId + " 开始读取...");
            Thread.sleep(100); // 模拟读取耗时
            System.out.println("读者-" + readerId + " 读取到: " + data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 写操作 - 需要独占锁
     */
    public void write(int writerId, String newData) {
        lock.writeLock().lock();
        try {
            System.out.println("写者-" + writerId + " 开始写入...");
            Thread.sleep(100); // 模拟写入耗时
            this.data = newData;
            System.out.println("写者-" + writerId + " 写入完成: " + newData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String getData() {
        lock.readLock().lock();
        try {
            return data;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setData(String data) {
        lock.writeLock().lock();
        try {
            this.data = data;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
