package com.concurrency.juc.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * AQS (AbstractQueuedSynchronizer) 源码分析
 *
 * AQS是JUC Locks的核心基础框架
 *
 * 1. 核心数据结构 - CLH队列 (Craig, Landin, Hagersten)
 *    - 变体双向链表实现
 *    - head指向已获取锁的节点
 *    - tail指向队尾
 *
 * 2. 节点状态
 *    - CANCELLED(1): 取消等待
 *    - SIGNAL(-1): 后继节点需要被唤醒
 *    - CONDITION(-2): 节点在条件队列等待
 *    - PROPAGATE(-3): 共享模式下向后传播
 *    - 0: 默认状态
 *
 * 3. 两种模式
 *    - 独占模式: 获取锁 - 释放锁
 *    - 共享模式: 获取共享锁 - 释放共享锁
 */
public class AbstractQueuedSynchronizerSourceAnalysis {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== AQS 源码分析 ===\n");

        // 1. AQS核心状态
        System.out.println("1. AQS 核心状态:");
        System.out.println("   private volatile int state;");
        System.out.println("   protected final int getState() { return state; }");
        System.out.println("   protected final void setState(int newState) { state = newState; }");
        System.out.println("   protected final boolean compareAndSetState(int expect, int update) {");
        System.out.println("       return unsafe.compareAndSwapInt(this, stateOffset, expect, update);");
        System.out.println("   }");
        System.out.println();

        // 2. 独占模式获取流程
        System.out.println("2. acquire() 独占模式获取流程:");
        System.out.println("   public final void acquire(int arg) {");
        System.out.println("       if (!tryAcquire(arg) &&");
        System.out.println("           acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) {");
        System.out.println("           selfInterrupt();");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();
        System.out.println("   // 流程: 尝试获取 -> 入队 -> 阻塞等待 -> 被唤醒 -> 获取锁");
        System.out.println();

        // 3. 独占模式释放流程
        System.out.println("3. release() 独占模式释放流程:");
        System.out.println("   public final boolean release(int arg) {");
        System.out.println("       if (tryRelease(arg)) {");
        System.out.println("           Node h = head;");
        System.out.println("           if (h != null && h.waitStatus != 0) {");
        System.out.println("               unparkSuccessor(h);  // 唤醒后继节点");
        System.out.println("           }");
        System.out.println("           return true;");
        System.out.println("       }");
        System.out.println("       return false;");
        System.out.println("   }");
        System.out.println();

        // 4. 共享模式获取流程
        System.out.println("4. acquireShared() 共享模式获取流程:");
        System.out.println("   public final void acquireShared(int arg) {");
        System.out.println("       if (tryAcquireShared(arg) < 0) {");
        System.out.println("           doAcquireShared(arg);  // 入队并阻塞");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();

        // 5. 节点入队过程
        System.out.println("5. addWaiter() 节点入队:");
        System.out.println("   private Node addWaiter(Node mode) {");
        System.out.println("       Node node = new Node(Thread.currentThread(), mode);");
        System.out.println("       Node pred = tail;");
        System.out.println("       if (pred != null) {");
        System.out.println("           node.prev = pred;");
        System.out.println("           if (compareAndSetTail(pred, node)) {");
        System.out.println("               pred.next = node;");
        System.out.println("               return node;");
        System.out.println("           }");
        System.out.println("       }");
        System.out.println("       enq(node);  // 自旋入队");
        System.out.println("       return node;");
        System.out.println("   }");
        System.out.println();

        // 6. 演示Condition
        System.out.println("6. Condition 原理:");
        System.out.println("   // ConditionObject 是AQS的内部类");
        System.out.println("   // 条件队列与阻塞队列分离");
        System.out.println("   await() -> 加入条件队列 -> 释放锁");
        System.out.println("   signal() -> 从条件队列移到阻塞队列");
        System.out.println();

        // 演示Condition
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Thread awaitThread = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("   线程开始await...");
                condition.await();
                System.out.println("   线程被signal唤醒");
            } catch (InterruptedException e) {
            } finally {
                lock.unlock();
            }
        }, "Await-Thread");

        awaitThread.start();
        Thread.sleep(100);

        lock.lock();
        try {
            System.out.println("   主线程发出signal");
            condition.signal();
        } finally {
            lock.unlock();
        }

        awaitThread.join();

        // 7. 使用自定义AQS实现互斥锁
        System.out.println("\n7. 自定义AQS互斥锁:");
        Mutex mutex = new Mutex();

        mutex.lock();
        System.out.println("   主线程获取锁");
        System.out.println("   主线程释放锁");
        mutex.unlock();
        System.out.println("   AQS互斥锁示例完成");
    }

    /**
     * 基于AQS的自定义互斥锁
     */
    static class Mutex {
        private final Sync sync = new Sync();

        public void lock() {
            sync.acquire(1);
        }

        public void unlock() {
            sync.release(1);
        }

        static class Sync extends AbstractQueuedSynchronizer {
            @Override
            protected boolean tryAcquire(int arg) {
                // 尝试获取锁，使用CAS设置状态
                return compareAndSetState(0, 1);
            }

            @Override
            protected boolean tryRelease(int arg) {
                setState(0);
                return true;
            }
        }
    }
}
