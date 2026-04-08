package com.ssitao.code.jdk.phase02.set;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * LinkedHashSet 示例代码
 * 演示LinkedHashSet保持插入顺序的特性
 */
public class LinkedHashSetDemo {

    public static void main(String[] args) {
        System.out.println("=== LinkedHashSet Demo ===\n");

        // 1. 基本操作
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add("c");
        set.add("a");
        set.add("b");
        set.add("a");  // 重复元素
        System.out.println("LinkedHashSet: " + set);
        System.out.println("Iteration order: c, a, b (insertion order maintained)");

        // 2. HashSet vs LinkedHashSet
        System.out.println("\n--- HashSet vs LinkedHashSet ---");
        System.out.print("HashSet iteration: ");
        java.util.Set<String> hashSet = new java.util.HashSet<>(Arrays.asList("c", "a", "b"));
        for (String s : hashSet) {
            System.out.print(s + " ");
        }
        System.out.println("\n(Note: Order is not guaranteed in HashSet)");

        System.out.print("LinkedHashSet iteration: ");
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>(Arrays.asList("c", "a", "b"));
        for (String s : linkedHashSet) {
            System.out.print(s + " ");
        }
        System.out.println("\n(Note: Order is guaranteed in LinkedHashSet)");

        // 3. 去重且保持顺序
        System.out.println("\n--- 去重且保持顺序 ---");
        java.util.List<String> list = Arrays.asList("apple", "banana", "apple", "orange", "banana", "grape");
        Set<String> uniqueOrdered = new LinkedHashSet<>(list);
        System.out.println("Original: " + list);
        System.out.println("After removing duplicates (preserving order): " + uniqueOrdered);

        // 4. 实现LRU缓存
        System.out.println("\n--- LRU缓存实现 ---");
        LinkedHashMapLRUCache<String, String> cache = new LinkedHashMapLRUCache<>(3);
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");
        System.out.println("Initial cache: " + cache);
        cache.get("a");  // 访问a，会将a移到尾部
        System.out.println("After accessing 'a': " + cache);
        cache.put("d", "4");  // 添加d，会移除最老的b
        System.out.println("After adding 'd': " + cache);
        System.out.println("Expected order: c, a, d (b was evicted)");

        // 5. 性能比较
        System.out.println("\n--- 性能比较 ---");
        int n = 100000;

        long start = System.nanoTime();
        java.util.HashSet<Integer> hashSetPerf = new java.util.HashSet<>();
        for (int i = 0; i < n; i++) {
            hashSetPerf.add(i);
        }
        long hashSetAddTime = System.nanoTime() - start;

        start = System.nanoTime();
        LinkedHashSet<Integer> linkedHashSetPerf = new LinkedHashSet<>();
        for (int i = 0; i < n; i++) {
            linkedHashSetPerf.add(i);
        }
        long linkedHashSetAddTime = System.nanoTime() - start;

        System.out.println("HashSet add time: " + hashSetAddTime / 1_000_000 + "ms");
        System.out.println("LinkedHashSet add time: " + linkedHashSetAddTime / 1_000_000 + "ms");
        System.out.println("LinkedHashSet is slightly slower due to maintaining order");

        // 6. 使用场景
        System.out.println("\n--- 使用场景 ---");
        System.out.println("1. 需要去重且保持插入顺序");
        System.out.println("2. 实现LRU缓存");
        System.out.println("3. 遍历时需要确定性顺序");
        System.out.println("4. 记录访问/操作顺序");
    }

    // LRU缓存实现
    static class LinkedHashMapLRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;

        LinkedHashMapLRUCache(int capacity) {
            super(capacity, 0.75f, true);  // accessOrder = true
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
            return size() > capacity;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("{");
            for (java.util.Map.Entry<K, V> entry : entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append(", ");
            }
            if (!isEmpty()) {
                sb.setLength(sb.length() - 2);
            }
            sb.append("}");
            return sb.toString();
        }
    }
}
