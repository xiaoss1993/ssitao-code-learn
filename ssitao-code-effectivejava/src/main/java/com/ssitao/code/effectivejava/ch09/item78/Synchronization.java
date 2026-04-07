package com.ssitao.code.effectivejava.ch09.item78;

/**
 * 条目78：同步访问共享可变数据
 *
 * 同步的作用：
 * 1. 原子性：确保操作不可分割
 * 2. 可见性：确保一个线程的修改对其他线程可见
 *
 * 关键原则：
 * - 不要使用Thread.stop()
 * - 保持同步简短，避免死锁
 * - 使用volatile确保可见性（不保证原子性）
 */
public class Synchronization {

    // ==================== 错误示例 ====================
    /**
     * 不安全的计数器
     * 问题：increment()和get()不是原子操作
     */
    static class UnsafeCounter {
        private long count = 0;

        public void increment() {
            count++;  // 不是原子操作：read-modify-write
        }

        public long get() {
            return count;
        }
    }

    // ==================== 正确示例 ====================
    /**
     * 线程安全的计数器
     * 使用synchronized保证原子性和可见性
     */
    static class SafeCounter {
        private long count = 0;

        public synchronized void increment() {
            count++;
        }

        public synchronized long get() {
            return count;
        }
    }

    // ==================== volatile示例 ====================
    /**
     * 使用volatile
     * 适用场景：只有一个线程写入，其他线程读取
     * 注意：不保证increment()的原子性！
     */
    static class VolatileCounter {
        private volatile long count = 0;

        // 这个方法不是线程安全的！需要synchronized
        public void increment() {
            count++;  // 警告：不是原子操作
        }

        public long get() {
            return count;
        }
    }

    // ==================== 正确的volatile用法 ====================
    /**
     * 正确使用volatile的场景：状态标志
     */
    static class StopFlag {
        private volatile boolean stopped = false;

        public void stop() {
            stopped = true;
        }

        public boolean isStopped() {
            return stopped;
        }

        // 主线程定期检查标志
        public void demo() {
            Thread worker = new Thread(() -> {
                int count = 0;
                while (!stopped) {
                    count++;
                    // 做些工作
                }
                System.out.println("Worker stopped at count: " + count);
            });

            worker.start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            stop();
            System.out.println("Stop requested, stopped=" + stopped);
        }
    }

    // ==================== 原子变量示例 ====================
    /**
     * 使用AtomicLong - 比synchronized更高效
     */
    static class AtomicCounter {
        private final java.util.concurrent.atomic.AtomicLong count = new java.util.concurrent.atomic.AtomicLong(0);

        public void increment() {
            count.incrementAndGet();  // 原子操作
        }

        public long get() {
            return count.get();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 同步示例 ===\n");

        // 演示可见性问题
        final SafeCounter counter = new SafeCounter();

        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> counter.increment());
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("安全计数器最终值: " + counter.get());
        System.out.println("期望值: 1000");

        System.out.println("\n=== volatile适用场景 ===\n");

        // 状态标志适合用volatile
        StopFlag flag = new StopFlag();
        flag.demo();

        System.out.println("\n=== Atomic变量 ===\n");

        AtomicCounter atomicCounter = new AtomicCounter();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> atomicCounter.increment()).start();
        }

        Thread.sleep(100);
        System.out.println("原子计数器最终值: " + atomicCounter.get());
    }
}
