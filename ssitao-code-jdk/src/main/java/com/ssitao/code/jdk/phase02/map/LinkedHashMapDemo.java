package com.ssitao.code.jdk.phase02.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LinkedHashMap 示例代码
 * 演示LinkedHashMap保持插入顺序的特性
 */
public class LinkedHashMapDemo {

    public static void main(String[] args) {
        System.out.println("=== LinkedHashMap Demo ===\n");

        // 1. 基本操作
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("c", 3);
        map.put("a", 1);
        map.put("b", 2);
        map.put("a", 10);  // 更新a的值
        System.out.println("LinkedHashMap: " + map);
        System.out.println("Iteration order: c, a, b (insertion order maintained)");

        // 2. HashMap vs LinkedHashMap
        System.out.println("\n--- HashMap vs LinkedHashMap ---");
        java.util.HashMap<String, Integer> hashMap = new java.util.HashMap<>();
        hashMap.put("c", 3);
        hashMap.put("a", 1);
        hashMap.put("b", 2);
        System.out.print("HashMap iteration: ");
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            System.out.print(entry.getKey() + " ");
        }
        System.out.println("\n(Note: Order not guaranteed)");

        System.out.print("LinkedHashMap iteration: ");
        System.out.print("c ");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.print(entry.getKey() + " ");
        }
        System.out.println("\n(Note: Order guaranteed)");

        // 3. LRU缓存实现
        System.out.println("\n--- LRU缓存实现 ---");
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");
        System.out.println("Initial cache: " + cache);

        cache.get("a");  // 访问a
        System.out.println("After get('a'): " + cache);

        cache.put("d", "4");  // 添加d，会移除最老的b
        System.out.println("After put('d', '4'): " + cache);
        System.out.println("Expected: c -> a -> d (b was evicted)");

        // 4. 访问顺序模式
        System.out.println("\n--- 访问顺序模式 ---");
        LinkedHashMap<String, Integer> accessOrderMap = new LinkedHashMap<>(16, 0.75f, true);
        accessOrderMap.put("a", 1);
        accessOrderMap.put("b", 2);
        accessOrderMap.put("c", 3);
        accessOrderMap.put("d", 4);
        System.out.println("Initial: " + accessOrderMap);

        accessOrderMap.get("b");  // 访问b
        System.out.println("After get('b'): " + accessOrderMap);

        accessOrderMap.get("a");  // 访问a
        System.out.println("After get('a'): " + accessOrderMap);

        // 5. removeEldestEntry示例
        System.out.println("\n--- removeEldestEntry ---");
        System.out.println("当removeEldestEntry返回true时，最老的元素会被移除");
        System.out.println("可用于实现固定大小的缓存");

        // 6. 使用场景
        System.out.println("\n--- 使用场景 ---");
        System.out.println("1. 需要保持插入顺序");
        System.out.println("2. 实现LRU缓存（按访问顺序）");
        System.out.println("3. 记录操作历史");
        System.out.println("4. 遍历时需要确定性顺序");
    }

    // LRU缓存实现
    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;

        LRUCache(int capacity) {
            super(capacity, 0.75f, true);  // accessOrder = true
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("{");
            for (Map.Entry<K, V> entry : entrySet()) {
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
