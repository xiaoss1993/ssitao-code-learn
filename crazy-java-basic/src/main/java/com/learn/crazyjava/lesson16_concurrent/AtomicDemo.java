package com.learn.crazyjava.lesson16_concurrent;

import java.util.concurrent.atomic.*;

/**
 * 第16课：并发编程 - 原子变量
 */
public class AtomicDemo {
    private AtomicInteger counter = new AtomicInteger(0);
    private LongAdder longAdder = new LongAdder();

    public void incrementCounter() {
        counter.incrementAndGet();
    }

    public void incrementLongAdder() {
        longAdder.increment();
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicDemo demo = new AtomicDemo();

        // 测试AtomicInteger
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> demo.incrementCounter()).start();
        }
        Thread.sleep(1000);
        System.out.println("AtomicInteger: " + demo.counter.get());

        // 测试LongAdder
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> demo.incrementLongAdder()).start();
        }
        Thread.sleep(1000);
        System.out.println("LongAdder: " + demo.longAdder.sum());

        // AtomicReference
        AtomicReference<String> ref = new AtomicReference<>("initial");
        ref.compareAndSet("initial", "updated");
        System.out.println("AtomicReference: " + ref.get());
    }
}
