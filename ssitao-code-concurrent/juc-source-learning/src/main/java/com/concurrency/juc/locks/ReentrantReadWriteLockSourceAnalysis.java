package com.concurrency.juc.locks;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock 源码分析
 *
 * 设计原理:
 * 1. 内部类 Sync 继承 AQS
 * 2. 使用 state 的高16位表示读锁，低16位表示写锁
 * 3. 读锁是共享模式，写锁是独占模式
 *
 * 特点:
 * - 读读不互斥
 * - 读写互斥
 * - 写写互斥
 * - 支持锁重入
 * - 支持锁降级
 */
public class ReentrantReadWriteLockSourceAnalysis {

    public static void main(String[] args) {
        System.out.println("=== ReentrantReadWriteLock 源码分析 ===\n");

        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

        // 1. 状态设计
        System.out.println("1. 状态设计:");
        System.out.println("   // state 高16位 = 读锁计数");
        System.out.println("   // state 低16位 = 写锁计数");
        System.out.println("   static final int SHARED_SHIFT   = 16;");
        System.out.println("   static final int SHARED_UNIT    = (1 << SHARED_SHIFT);  // 65536");
        System.out.println("   static final int MAX_COUNT     = (1 << SHARED_SHIFT) - 1;  // 65535");
        System.out.println("   static final int EXCLUSIVE_MASK = (1 << SHARED_SHIFT) - 1;  // 65535");
        System.out.println();

        // 2. 获取读锁
        System.out.println("2. readLock().lock() 源码:");
        System.out.println("   public void lock() {");
        System.out.println("       sync.acquireShared(1);");
        System.out.println("   }");
        System.out.println();
        System.out.println("   protected final int tryAcquireShared(int unused) {");
        System.out.println("       for (;;) {");
        System.out.println("           int c = getState();");
        System.out.println("           int nextc = c + SHARED_UNIT;  // +65536");
        System.out.println("           if (nextc < 0) throw new Error();");
        System.out.println("           if (compareAndSetState(c, nextc)) {");
        System.out.println("               // 成功获取读锁");
        System.out.println("               return 1;");
        System.out.println("           }");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();

        // 3. 获取写锁
        System.out.println("3. writeLock().lock() 源码:");
        System.out.println("   public void lock() {");
        System.out.println("       sync.acquire(1);");
        System.out.println("   }");
        System.out.println();
        System.out.println("   protected final boolean tryAcquire(int acquires) {");
        System.out.println("       Thread current = Thread.currentThread();");
        System.out.println("       int c = getState();");
        System.out.println("       int w = c & EXCLUSIVE_MASK;  // 写锁计数");
        System.out.println("       if (c != 0) {");
        System.out.println("           // 有读锁或写锁");
        System.out.println("           if (w == 0 || current != getExclusiveOwnerThread())");
        System.out.println("               return false;  // 读锁存在或有其他写锁");
        System.out.println("           if (w + acquires > MAX_COUNT)");
        System.out.println("               throw new Error();");
        System.out.println("       }");
        System.out.println("       if (compareAndSetState(c, c + acquires)) {");
        System.out.println("           setExclusiveOwnerThread(current);");
        System.out.println("           return true;");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();

        // 4. 演示读写锁
        System.out.println("4. 读写锁演示:");

        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        // 写锁降级为读锁
        System.out.println("\n   4.1 锁降级 (写锁 -> 读锁):");
        lock.writeLock().lock();
        System.out.println("       获取写锁, state = " + getState(lock));
        lock.readLock().lock();
        System.out.println("       获取读锁(降级), state = " + getState(lock));
        lock.writeLock().unlock();
        System.out.println("       释放写锁, state = " + getState(lock));
        lock.readLock().unlock();
        System.out.println("       释放读锁, state = " + getState(lock));

        // 演示读锁共享
        System.out.println("\n   4.2 读锁共享:");
        lock.readLock().lock();
        System.out.println("       线程1 获取读锁, state = " + getState(lock));

        new Thread(() -> {
            lock.readLock().lock();
            System.out.println("       线程2 获取读锁(共享), state = " + getState(lock));
            lock.readLock().unlock();
        }).start();

        try { Thread.sleep(100); } catch (InterruptedException e) {}

        lock.readLock().unlock();
        System.out.println("       线程1 释放读锁, state = " + getState(lock));

        // 演示写锁互斥
        System.out.println("\n   4.3 写锁互斥:");
        lock.writeLock().lock();
        System.out.println("       线程1 获取写锁, state = " + getState(lock));

        new Thread(() -> {
            lock.readLock().lock();
            System.out.println("       线程2 尝试获取读锁 - 被阻塞");
        }).start();

        try { Thread.sleep(100); } catch (InterruptedException e) {}

        lock.writeLock().unlock();
        System.out.println("       线程1 释放写锁");
    }

    private static int getState(ReentrantReadWriteLock lock) {
        try {
            java.lang.reflect.Field field = ReentrantReadWriteLock.class.getDeclaredField("sync");
            field.setAccessible(true);
            Object sync = field.get(lock);
            field = sync.getClass().getSuperclass().getDeclaredField("state");
            field.setAccessible(true);
            return ((Number) field.get(sync)).intValue();
        } catch (Exception e) {
            return -1;
        }
    }
}
