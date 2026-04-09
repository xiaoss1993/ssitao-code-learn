package com.concurrency.juc.collections;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ConcurrentHashMap 源码分析 (JDK8) + 实际使用示例
 */
public class ConcurrentHashMapSourceAnalysis {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ConcurrentHashMap 源码分析 + 使用示例 ===\n");

        // 1. 基本操作
        System.out.println("【1. 基本操作】");
        basicOperations();

        // 2. 原子更新操作
        System.out.println("\n【2. 原子更新操作】");
        atomicOperations();

        // 3. 统计词频
        System.out.println("\n【3. 并发词频统计】");
        wordCount();

        // 4. 并发安全缓存
        System.out.println("\n【4. 并发安全缓存】");
        safeCache();

        // 5. 批量操作
        System.out.println("\n【5. 批量操作】");
        batchOperations();

        // 6. 并发性能测试
        System.out.println("\n【6. 并发性能测试】");
        performanceTest();
    }

    /**
     * 1. 基本操作
     */
    private static void basicOperations() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // put
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        // get
        System.out.println("  map.get(\"b\") = " + map.get("b"));

        // remove
        map.remove("b");
        System.out.println("  remove(\"b\") 后: " + map);

        // containsKey
        System.out.println("  containsKey(\"c\") = " + map.containsKey("c"));

        // size
        System.out.println("  size = " + map.size());

        // isEmpty
        System.out.println("  isEmpty = " + map.isEmpty());
    }

    /**
     * 2. 原子更新操作
     */
    private static void atomicOperations() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("a", 0);

        // 原子增加
        map.put("a", map.get("a") + 1); // 普通方式
        System.out.println("  普通方式: a=" + map.get("a"));

        // compute - 原子计算
        map.compute("a", (k, v) -> v == null ? 1 : v + 1);
        System.out.println("  compute后: a=" + map.get("a"));

        // computeIfAbsent - 不存在才计算
        map.computeIfAbsent("b", k -> 10);
        System.out.println("  computeIfAbsent(\"b\", k->10): b=" + map.get("b"));

        // computeIfPresent - 存在才计算
        map.computeIfPresent("b", (k, v) -> v + 5);
        System.out.println("  computeIfPresent(\"b\", +5): b=" + map.get("b"));

        // merge - 合并
        map.merge("b", 3, (oldVal, newVal) -> oldVal + newVal);
        System.out.println("  merge(\"b\", 3, +): b=" + map.get("b"));
    }

    /**
     * 3. 并发词频统计
     */
    private static void wordCount() throws InterruptedException {
        String[] words = {"apple", "banana", "apple", "orange", "banana", "apple"};
        ConcurrentHashMap<String, Integer> frequency = new ConcurrentHashMap<>();

        int threadCount = 3;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 每个线程统计一部分
        int chunkSize = words.length / threadCount;
        for (int t = 0; t < threadCount; t++) {
            final int start = t * chunkSize;
            final int end = (t == threadCount - 1) ? words.length : start + chunkSize;

            executor.submit(() -> {
                for (int i = start; i < end; i++) {
                    // 原子递增
                    frequency.compute(words[i], (k, v) -> v == null ? 1 : v + 1);
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        System.out.println("  词频统计结果: " + frequency);
        System.out.println("  apple出现次数: " + frequency.get("apple"));
    }

    /**
     * 4. 并发安全缓存
     */
    private static void safeCache() throws InterruptedException {
        ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

        // 模拟从数据库加载数据
        java.util.function.Function<String, String> loadFromDb = (String key) -> {
            System.out.println("    [DB查询] key=" + key);
            try {
                Thread.sleep(100); // 模拟数据库延迟
            } catch (InterruptedException e) {
            }
            return key.toUpperCase();
        };

        // 使用computeIfAbsent实现缓存
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int requestId = i;
            int finalI = i;
            executor.submit(() -> {
                String key = "key" + (finalI % 3); // 0,1,2,0,1 循环
                System.out.println("  请求" + requestId + " 查询 key=" + key);

                // 缓存命中则直接返回，否则从DB加载
                String value = cache.computeIfAbsent(key, loadFromDb);
                System.out.println("  请求" + requestId + " 结果: " + value);
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        System.out.println("  缓存内容: " + cache);
    }

    /**
     * 5. 批量操作
     */
    private static void batchOperations() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);

        System.out.println("  原map: " + map);

        // search - 搜索
        String found = map.search(1, (k, v) -> v > 2 ? k : null);
        System.out.println("  search(v>2): 找到 " + found);

        // reduce - 汇总
        Integer sum = map.reduce(1, (k, v) -> v, Integer::sum);
        System.out.println("  reduce求和: " + sum);

        // forEach - 遍历
        System.out.print("  forEach遍历: ");
        map.forEach((k, v) -> System.out.print(k + "=" + v + " "));
        System.out.println();

        // keys - 所有key
        System.out.println("  keySet: " + map.keySet());

        // values - 所有value
        System.out.println("  values: " + map.values());
    }

    /**
     * 6. 并发性能测试
     */
    private static void performanceTest() throws InterruptedException {
        int threadCount = 10;
        int opCount = 50000;

        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(threadCount);
        long start = System.nanoTime();

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int t = 0; t < threadCount; t++) {
            executor.submit(() -> {
                for (int i = 0; i < opCount; i++) {
                    String key = "key" + (i % 1000);
                    map.put(key + Thread.currentThread().getId(), i);
                    map.get(key);
                    map.containsKey(key);
                }
                latch.countDown();
            });
        }

        latch.await();
        long duration = System.nanoTime() - start;
        executor.shutdown();

        System.out.println("  线程数: " + threadCount);
        System.out.println("  每线程操作数: " + opCount);
        System.out.println("  总操作数: " + (threadCount * opCount * 3));
        System.out.println("  map大小: " + map.size());
        System.out.println("  耗时: " + duration / 1_000_000 + " ms");
        System.out.println("  QPS: " + (threadCount * opCount * 3 * 1_000_000_000L / duration));
    }
}
