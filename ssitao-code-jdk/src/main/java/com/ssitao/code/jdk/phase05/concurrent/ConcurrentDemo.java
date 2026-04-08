package com.ssitao.code.jdk.phase05.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 并发工具类示例
 */
public class ConcurrentDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Concurrent Tools Demo ===\n");

        // 1. CountDownLatch
        demonstrateCountDownLatch();

        // 2. CyclicBarrier
        demonstrateCyclicBarrier();

        // 3. Semaphore
        demonstrateSemaphore();

        // 4. Exchanger
        demonstrateExchanger();

        // 5. 并发容器
        demonstrateConcurrentCollections();

        // 6. 原子类
        demonstrateAtomic();
    }

    private static void demonstrateCountDownLatch() throws Exception {
        System.out.println("--- CountDownLatch ---");

        int taskCount = 3;
        CountDownLatch latch = new CountDownLatch(taskCount);

        for (int i = 1; i <= taskCount; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    System.out.println("Task " + id + " starting");
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println("Task " + id + " completed");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            }, "Task-" + i).start();
        }

        System.out.println("Main thread waiting...");
        latch.await();
        System.out.println("All tasks completed, main thread continues");

        System.out.println();
    }

    private static void demonstrateCyclicBarrier() throws Exception {
        System.out.println("--- CyclicBarrier ---");

        int partySize = 3;
        CyclicBarrier barrier = new CyclicBarrier(partySize, () -> {
            System.out.println("Barrier action: All parties arrived!");
        });

        for (int i = 1; i <= partySize; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    System.out.println("Party " + id + " waiting at barrier");
                    barrier.await();
                    System.out.println("Party " + id + " proceeding");
                } catch (BrokenBarrierException | InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Party-" + i).start();
        }

        Thread.sleep(2000);
        System.out.println();
    }

    private static void demonstrateSemaphore() throws Exception {
        System.out.println("--- Semaphore ---");

        int permits = 2;
        Semaphore semaphore = new Semaphore(permits);

        for (int i = 1; i <= 5; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + id + " trying to acquire");
                    semaphore.acquire();
                    System.out.println("Thread " + id + " acquired, permits left: " + semaphore.availablePermits());
                    Thread.sleep(1000);
                    semaphore.release();
                    System.out.println("Thread " + id + " released, permits left: " + semaphore.availablePermits());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Thread-" + i).start();
        }

        Thread.sleep(6000);
        System.out.println();
    }

    private static void demonstrateExchanger() throws Exception {
        System.out.println("--- Exchanger ---");

        Exchanger<String> exchanger = new Exchanger<>();

        Thread t1 = new Thread(() -> {
            try {
                String data = "Data from Thread-1";
                System.out.println("Thread-1 sending: " + data);
                String received = exchanger.exchange(data);
                System.out.println("Thread-1 received: " + received);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(500);
                String data = "Data from Thread-2";
                System.out.println("Thread-2 sending: " + data);
                String received = exchanger.exchange(data);
                System.out.println("Thread-2 received: " + received);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Thread-2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println();
    }

    private static void demonstrateConcurrentCollections() {
        System.out.println("--- Concurrent Collections ---");

        // ConcurrentHashMap
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.putIfAbsent("a", 10);  // 不会覆盖
        System.out.println("ConcurrentHashMap: " + map);

        // 原子操作
        map.compute("a", (k, v) -> v == null ? 1 : v + 10);
        System.out.println("After compute: " + map);

        // CopyOnWriteArrayList
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("x");
        list.add("y");
        System.out.println("CopyOnWriteArrayList: " + list);

        // BlockingQueue
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);
        try {
            queue.put(1);
            queue.put(2);
            queue.offer(3, 1, TimeUnit.SECONDS);
            System.out.println("BlockingQueue: " + queue);
            System.out.println("Queue take: " + queue.take());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println();
    }

    private static void demonstrateAtomic() throws Exception {
        System.out.println("--- Atomic Classes ---");

        // AtomicInteger
        AtomicInteger atomicInt = new AtomicInteger(0);
        System.out.println("Initial: " + atomicInt.get());
        System.out.println("Increment: " + atomicInt.incrementAndGet());
        System.out.println("Add 5: " + atomicInt.addAndGet(5));
        System.out.println("Compare and set (5->20): " + atomicInt.compareAndSet(5, 20));
        System.out.println("After CAS: " + atomicInt.get());

        // AtomicReference
        AtomicReference<String> atomicRef = new AtomicReference<>("initial");
        System.out.println("\nAtomicReference: " + atomicRef.get());
        atomicRef.set("updated");
        System.out.println("After set: " + atomicRef.get());

        // AtomicLong
        AtomicLong atomicLong = new AtomicLong(100);
        System.out.println("\nAtomicLong: " + atomicLong.get());

        // 模拟计数器累加
        System.out.println("\n--- Simulated Counter Race ---");
        AtomicInteger counter = new AtomicInteger(0);
        int threadCount = 100;
        int incrementsPerThread = 1000;

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.incrementAndGet();
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
        System.out.println("Actual: " + counter.get());
    }
}
