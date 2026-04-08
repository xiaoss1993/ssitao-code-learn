package com.ssitao.code.jdk.phase05.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

/**
 * 线程基础示例
 */
public class ThreadDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Thread Demo ===\n");

        // 1. 线程创建方式
        demonstrateThreadCreation();

        // 2. 线程状态
        demonstrateThreadState();

        // 3. 线程同步
        demonstrateSynchronization();

        // 4. 生产者-消费者
        demonstrateProducerConsumer();

        // 5. ThreadLocal
        demonstrateThreadLocal();
    }

    private static void demonstrateThreadCreation() throws Exception {
        System.out.println("--- Thread Creation ---");

        // 方式1：继承Thread
        Thread t1 = new Thread(() -> {
            System.out.println("Thread 1 running");
        });
        t1.start();

        // 方式2：实现Runnable
        Thread t2 = new Thread(() -> {
            System.out.println("Thread 2 running");
        }, "MyThread");
        t2.start();

        // 方式3：带返回值的Callable
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            Thread.sleep(100);
            return 42;
        });
        System.out.println("Callable result: " + future.get());

        Thread.sleep(200);
    }

    private static void demonstrateThreadState() throws Exception {
        System.out.println("\n--- Thread State ---");

        Thread.State state = Thread.currentThread().getState();
        System.out.println("Main thread state: " + state);

        Thread t = new Thread(() -> {
            System.out.println("Running state: " + Thread.currentThread().getState());
        });

        System.out.println("Before start: " + t.getState());
        t.start();
        Thread.sleep(50);
        System.out.println("After start: " + t.getState());
        t.join();
        System.out.println("After join: " + t.getState());
    }

    private static void demonstrateSynchronization() throws Exception {
        System.out.println("\n--- Synchronization ---");

        Counter counter = new Counter();

        // 启动10个线程，每个线程增加1000次
        int threadCount = 10;
        int incrementsPerThread = 1000;

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
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

        System.out.println("Expected: " + (threadCount * incrementsPerThread));
        System.out.println("Actual: " + counter.getCount());
    }

    private static void demonstrateProducerConsumer() throws Exception {
        System.out.println("\n--- Producer-Consumer ---");

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        // 生产者
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    queue.put(i);
                    System.out.println("Produced: " + i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");

        // 消费者
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Integer value = queue.take();
                    if (value == 10) {
                        queue.put(value);  // 放回去，让生产者结束
                        break;
                    }
                    System.out.println("Consumed: " + value);
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        System.out.println("Producer-Consumer finished");
    }

    private static void demonstrateThreadLocal() throws Exception {
        System.out.println("\n--- ThreadLocal ---");

        ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);

        // 主线程
        counter.set(100);
        System.out.println("Main thread: " + counter.get());

        // 其他线程
        Thread t1 = new Thread(() -> {
            System.out.println("Thread-1 initial: " + counter.get());
            counter.set(200);
            System.out.println("Thread-1 after: " + counter.get());
        });

        Thread t2 = new Thread(() -> {
            System.out.println("Thread-2 initial: " + counter.get());
            counter.set(300);
            System.out.println("Thread-2 after: " + counter.get());
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // 主线程不受影响
        System.out.println("Main thread again: " + counter.get());
    }

    // 线程不安全的计数器
    static class UnsafeCounter {
        private int count = 0;

        public void increment() {
            count++;  // 不是原子操作
        }

        public int getCount() {
            return count;
        }
    }

    // 线程安全的计数器
    static class Counter {
        private final AtomicInteger count = new AtomicInteger(0);

        public void increment() {
            count.incrementAndGet();
        }

        public int getCount() {
            return count.get();
        }
    }
}
