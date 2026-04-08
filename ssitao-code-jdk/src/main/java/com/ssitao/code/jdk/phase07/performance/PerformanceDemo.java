package com.ssitao.code.jdk.phase07.performance;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 性能优化演示
 */
public class PerformanceDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Performance Demo ===\n");

        // 1. 内存管理演示
        demonstrateMemoryManagement();

        // 2. 对象分配优化
        demonstrateObjectAllocation();

        // 3. 字符串拼接优化
        demonstrateStringOptimization();

        // 4. 集合优化
        demonstrateCollectionOptimization();

        // 5. 并行处理
        demonstrateParallelProcessing();

        // 6. JVM信息
        demonstrateJVMInfo();
    }

    private static void demonstrateMemoryManagement() {
        System.out.println("--- Memory Management ---");

        Runtime rt = Runtime.getRuntime();

        // 强制GC前
        System.gc();
        long beforeMB = rt.totalMemory() - rt.freeMemory();
        System.out.println("After System.gc(), used: " + beforeMB / 1024 / 1024 + "MB");

        // 分配大量对象
        List<byte[]> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new byte[1024 * 1024]);  // 1MB * 100 = 100MB
        }

        long afterMB = rt.totalMemory() - rt.freeMemory();
        System.out.println("After allocation, used: " + afterMB / 1024 / 1024 + "MB");
        System.out.println("Total memory: " + rt.totalMemory() / 1024 / 1024 + "MB");
        System.out.println("Max memory: " + rt.maxMemory() / 1024 / 1024 + "MB");

        // 清理
        list.clear();
        list = null;

        // 建议GC
        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException e) { }

        long finalMB = rt.totalMemory() - rt.freeMemory();
        System.out.println("After cleanup, used: " + finalMB / 1024 / 1024 + "MB");

        System.out.println();
    }

    private static void demonstrateObjectAllocation() {
        System.out.println("--- Object Allocation Optimization ---");

        int iterations = 10_000_000;

        // 方式1：在循环内创建对象
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Point p = new Point(i, i);  // 每次创建新对象
        }
        long time1 = System.nanoTime() - start;
        System.out.println("Creating object in loop: " + time1 / 1_000_000 + "ms");

        // 方式2：复用对象（如果可接受）
        start = System.nanoTime();
        Point reused = new Point(0, 0);
        for (int i = 0; i < iterations; i++) {
            reused.x = i;  // 修改字段而非创建新对象
            reused.y = i;
        }
        long time2 = System.nanoTime() - start;
        System.out.println("Reusing object: " + time2 / 1_000_000 + "ms");

        System.out.println("Speedup: " + (double) time1 / time2 + "x");
        System.out.println();

        // 对象大小说明
        System.out.println("Object size tips:");
        System.out.println("  - Primitive fields are cheaper than Object references");
        System.out.println("  - Smaller objects = more objects fit in CPU cache");
        System.out.println("  - Consider primitive arrays vs ArrayList<Point>");
        System.out.println();
    }

    private static void demonstrateStringOptimization() {
        System.out.println("--- String Optimization ---");

        int iterations = 100_000;

        // 方式1：字符串+拼接（不推荐）
        long start = System.nanoTime();
        String result1 = "";
        for (int i = 0; i < iterations; i++) {
            result1 = result1 + "item" + i + ",";
        }
        long time1 = System.nanoTime() - start;
        System.out.println("String + concat: " + time1 / 1_000_000 + "ms");

        // 方式2：StringBuilder
        start = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("item").append(i).append(",");
        }
        String result2 = sb.toString();
        long time2 = System.nanoTime() - start;
        System.out.println("StringBuilder: " + time2 / 1_000_000 + "ms");

        // 方式3：StringJoiner
        start = System.nanoTime();
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < iterations; i++) {
            joiner.add("item" + i);
        }
        String result3 = joiner.toString();
        long time3 = System.nanoTime() - start;
        System.out.println("StringJoiner: " + time3 / 1_000_000 + "ms");

        System.out.println("Speedup vs concatenation: " + (double) time1 / time2 + "x");
        System.out.println();

        // String.intern()演示
        System.out.println("String intern():");
        String s1 = new String("hello");
        String s2 = new String("hello");
        System.out.println("s1 == s2: " + (s1 == s2));           // false
        System.out.println("s1.intern() == s2.intern(): " + (s1.intern() == s2.intern())); // true
        System.out.println();
    }

    private static void demonstrateCollectionOptimization() {
        System.out.println("--- Collection Optimization ---");

        // 预估容量
        int size = 1_000_000;

        // 不预估容量
        long start = System.nanoTime();
        ArrayList<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list1.add(i);
        }
        long time1 = System.nanoTime() - start;
        System.out.println("ArrayList without initial capacity: " + time1 / 1_000_000 + "ms");

        // 预估容量
        start = System.nanoTime();
        ArrayList<Integer> list2 = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list2.add(i);
        }
        long time2 = System.nanoTime() - start;
        System.out.println("ArrayList with initial capacity: " + time2 / 1_000_000 + "ms");

        System.out.println("Speedup: " + (double) time1 / time2 + "x");

        // HashMap容量
        System.out.println("\nHashMap optimization:");
        System.out.println("  Initial capacity = expectedSize / 0.75 + 1");
        System.out.println("  For " + size + " items: " + ((int)(size / 0.75f) + 1));
        System.out.println();

        // 常用集合选择
        System.out.println("Collection selection tips:");
        System.out.println("  - Random access: ArrayList > LinkedList");
        System.out.println("  - Frequent insert/delete middle: LinkedList or ArrayDeque");
        System.out.println("  - FIFO queue: ArrayDeque > LinkedList");
        System.out.println("  - LIFO stack: ArrayDeque > LinkedList");
        System.out.println("  - Thread-safe: ConcurrentHashMap, CopyOnWriteArrayList");
        System.out.println();
    }

    private static void demonstrateParallelProcessing() {
        System.out.println("--- Parallel Processing ---");

        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 10_000_000; i++) {
            data.add(i);
        }

        // 串行处理
        long start = System.nanoTime();
        long sum1 = data.stream()
            .mapToLong(Integer::intValue)
            .sum();
        long time1 = System.nanoTime() - start;
        System.out.println("Sequential stream: " + time1 / 1_000_000 + "ms, sum=" + sum1);

        // 并行处理
        start = System.nanoTime();
        long sum2 = data.parallelStream()
            .mapToLong(Integer::intValue)
            .sum();
        long time2 = System.nanoTime() - start;
        System.out.println("Parallel stream: " + time2 / 1_000_000 + "ms, sum=" + sum2);

        System.out.println("Speedup: " + (double) time1 / time2 + "x");
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println();

        // 并行处理注意事项
        System.out.println("Parallel stream tips:");
        System.out.println("  - Good for: CPU-intensive, large datasets");
        System.out.println("  - Avoid for: I/O, small datasets, order-dependent");
        System.out.println("  - watch out for: shared state, order of elements");
        System.out.println();
    }

    private static void demonstrateJVMInfo() {
        System.out.println("--- JVM Information ---");

        // MemoryMXBean
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeap = memoryBean.getNonHeapMemoryUsage();

        System.out.println("Heap Memory:");
        System.out.println("  Init: " + heap.getInit() / 1024 / 1024 + "MB");
        System.out.println("  Used: " + heap.getUsed() / 1024 / 1024 + "MB");
        System.out.println("  Max: " + heap.getMax() / 1024 / 1024 + "MB");
        System.out.println("  Committed: " + heap.getCommitted() / 1024 / 1024 + "MB");

        System.out.println("\nNon-Heap Memory:");
        System.out.println("  Init: " + nonHeap.getInit() / 1024 / 1024 + "MB");
        System.out.println("  Used: " + nonHeap.getUsed() / 1024 / 1024 + "MB");
        System.out.println("  Max: " + nonHeap.getMax() / 1024 / 1024 + "MB");

        // RuntimeMXBean
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        System.out.println("\nRuntime:");
        System.out.println("  JVM Name: " + runtimeBean.getVmName());
        System.out.println("  JVM Version: " + runtimeBean.getVmVersion());
        System.out.println("  Java Home: " + runtimeBean.getJavaHome());
        System.out.println("  Uptime: " + runtimeBean.getUptime() / 1000 + "s");

        // GarbageCollectorMXBeans
        System.out.println("\nGarbage Collectors:");
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            System.out.println("  " + gc.getName() + ":");
            System.out.println("    - Collection count: " + gc.getCollectionCount());
            System.out.println("    - Collection time: " + gc.getCollectionTime() + "ms");
            System.out.println("    - Memory pools: " + Arrays.toString(gc.getMemoryPoolNames()));
        }

        System.out.println("\nMemory Pools:");
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            MemoryUsage usage = pool.getUsage();
            if (usage != null) {
                System.out.println("  " + pool.getName() + ":");
                System.out.println("    - Used: " + usage.getUsed() / 1024 / 1024 + "MB");
                System.out.println("    - Max: " + usage.getMax() / 1024 / 1024 + "MB");
            }
        }
    }

    // 测试用类
    static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
