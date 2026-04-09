package com.concurrency.juc.collections;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CopyOnWriteArrayList 源码分析
 *
 * 核心设计:
 * 1. 使用 volatile Object[] array 存储数据
 * 2. 所有修改操作(copyOnWrite)创建新数组
 * 3. 使用 ReentrantLock 保证原子性
 *
 * 特点:
 * - 读操作无锁，性能高
 * - 写操作复制原数组，适合读多写少场景
 * - 迭代器快照技术，弱一致性
 * - 无法实时看到新添加元素
 */
public class CopyOnWriteArrayListSourceAnalysis {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== CopyOnWriteArrayList 源码分析 ===\n");

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

        // 1. 核心数据结构
        System.out.println("1. 核心数据结构:");
        System.out.println("   private transient volatile Object[] array;");
        System.out.println("   // volatile保证可见性");
        System.out.println("   // Lock锁保证修改原子性");
        System.out.println();

        // 2. add操作
        System.out.println("2. add() 源码:");
        System.out.println("   public boolean add(E e) {");
        System.out.println("       final ReentrantLock lock = this.lock;");
        System.out.println("       lock.lock();");
        System.out.println("       try {");
        System.out.println("           Object[] elements = getArray();");
        System.out.println("           int len = elements.length;");
        System.out.println("           Object[] newElements = Arrays.copyOf(elements, len + 1);");
        System.out.println("           newElements[len] = e;");
        System.out.println("           setArray(newElements);");
        System.out.println("           return true;");
        System.out.println("       } finally {");
        System.out.println("           lock.unlock();");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();

        // 3. get操作
        System.out.println("3. get() 源码 (无锁):");
        System.out.println("   private E getAt(int index) {");
        System.out.println("       return (E) getArray()[index];");
        System.out.println("   }");
        System.out.println();
        System.out.println("   // 读操作无锁，可能读到旧数据");
        System.out.println("   // 但volatile保证一定能读到最新setArray");
        System.out.println();

        // 4. 迭代器
        System.out.println("4. 迭代器 (快照技术):");
        System.out.println("   // 创建迭代器时保存当前数组快照");
        System.out.println("   private static class COWIterator<E> implements Iterator<E> {");
        System.out.println("       private final Object[] snapshot;");
        System.out.println("       private int cursor;");
        System.out.println();
        System.out.println("       COWIterator(Object[] array, int initialCursor) {");
        System.out.println("           this.snapshot = array;");
        System.out.println("           this.cursor = initialCursor;");
        System.out.println("       }");
        System.out.println("       // remove() 不支持，抛出UnsupportedOperationException");
        System.out.println("   }");
        System.out.println();

        // 5. 演示弱一致性
        System.out.println("5. 迭代器弱一致性演示:");

        CopyOnWriteArrayList<Integer> cowList = new CopyOnWriteArrayList<>();
        cowList.add(1);
        cowList.add(2);
        cowList.add(3);

        System.out.println("   原始列表: " + cowList);

        Iterator<Integer> iter = cowList.iterator();

        System.out.println("   创建迭代器后，添加新元素...");
        cowList.add(4);
        cowList.add(5);

        System.out.println("   列表现在: " + cowList);
        System.out.println("   迭代器遍历: ");

        StringBuilder sb = new StringBuilder("   [");
        while (iter.hasNext()) {
            sb.append(iter.next()).append(", ");
        }
        System.out.println(sb.substring(0, sb.length() - 2) + "]");
        System.out.println("   (迭代器看到的是创建时的快照，不包含4,5)");
        System.out.println();

        // 6. 并发性能测试
        System.out.println("6. 并发性能测试 (读多写少):");

        CopyOnWriteArrayList<Integer> perfList = new CopyOnWriteArrayList<>();
        int readThreads = 10;
        int writeThreads = 2;
        int readCount = 10000;
        int writeCount = 500;

        CountDownLatch latch = new CountDownLatch(readThreads + writeThreads);
        ExecutorService executor = Executors.newFixedThreadPool(readThreads + writeThreads);

        long start = System.nanoTime();

        // 读线程
        for (int i = 0; i < readThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < readCount; j++) {
                    perfList.get(0);
                    perfList.size();
                    perfList.contains(1);
                }
                latch.countDown();
            });
        }

        // 写线程
        for (int i = 0; i < writeThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < writeCount; j++) {
                    perfList.add(j);
                }
                latch.countDown();
            });
        }

        latch.await();
        long duration = System.nanoTime() - start;
        executor.shutdown();

        System.out.println("   读线程: " + readThreads + ", 写线程: " + writeThreads);
        System.out.println("   总读操作: " + (readThreads * readCount));
        System.out.println("   总写操作: " + (writeThreads * writeCount));
        System.out.println("   耗时: " + duration / 1_000_000 + "ms");
    }
}
