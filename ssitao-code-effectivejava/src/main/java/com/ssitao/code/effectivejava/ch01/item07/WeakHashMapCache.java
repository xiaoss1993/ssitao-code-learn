package com.ssitao.code.effectivejava.ch01.item07;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 条目7：演示WeakHashMap用于内存敏感的缓存
 *
 * WeakHashMap使用WeakReference作为key - 当key不再强可达时，
 * 条目会自动被移除。这解决了普通HashMap可能导致的内存泄漏问题。
 *
 * 对比：
 * - 普通Map：key被强引用持有，即使外部已不再使用，GC也无法回收
 * - WeakHashMap：key只持有弱引用，当key不可达时自动移除条目
 */
public class WeakHashMapCache {

    // ==================== 问题：普通Map的内存泄漏 ====================
    /**
     * 普通缓存 - 可能导致内存泄漏
     * 问题：没有自动驱逐条目的机制
     */
    static class RegularCache {
        private final Map<String, byte[]> cache = new HashMap<>();

        public byte[] get(String key) {
            return cache.get(key);
        }

        public void put(String key, byte[] value) {
            cache.put(key, value);
        }

        public int size() {
            return cache.size();
        }

        // 问题：无法自动驱逐条目
    }

    // ==================== 解决方案：WeakHashMap ====================
    /**
     * 弱引用缓存 - 自动清理
     * 条目在key被GC时自动移除
     */
    static class WeakCache {
        private final Map<String, byte[]> cache = new WeakHashMap<>();

        public byte[] get(String key) {
            return cache.get(key);
        }

        public void put(String key, byte[] value) {
            cache.put(key, value);
        }

        public int size() {
            return cache.size();
        }

        // 条目在key被GC时自动移除
    }

    // ==================== LRU缓存：使用LinkedHashMap ====================
    /**
     * LRU（最近最少使用）缓存
     * 通过LinkedHashMap实现自动容量管理
     */
    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int maxCapacity;

        public LRUCache(int capacity) {
            super(capacity, 0.75f, true);  // accessOrder=true表示按访问顺序排序
            this.maxCapacity = capacity;
        }

        /**
         * 覆写此方法，当条目数超过容量时移除最老的条目
         */
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxCapacity;
        }

        public static void main(String[] args) {
            System.out.println("=== LRU缓存示例 ===");
            LRUCache<String, String> lru = new LRUCache<>(3);
            lru.put("A", "1");
            lru.put("B", "2");
            lru.put("C", "3");
            System.out.println("初始状态: " + lru);

            // 访问A，使其成为最近使用的
            lru.get("A");
            System.out.println("访问A后: " + lru);

            // 添加D，应该驱逐B（最久未使用的）
            lru.put("D", "4");
            System.out.println("添加D后: " + lru);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== WeakHashMap vs 普通HashMap ===\n");

        // 普通缓存 - 内存泄漏风险
        System.out.println("--- 普通HashMap（内存泄漏风险） ---");
        RegularCache regularCache = new RegularCache();

        // 创建一个只在缓存中可达的key
        String key1 = new String("key1");
        regularCache.put(key1, new byte[1024]);
        System.out.println("普通缓存大小: " + regularCache.size());

        // 取消强引用
        key1 = null;

        // GC - 但缓存条目仍然存在！
        System.gc();
        System.out.println("GC后，普通缓存大小: " + regularCache.size());
        System.out.println("问题：条目未被移除 - 内存泄漏！\n");

        // WeakHashMap缓存 - 自动清理
        System.out.println("--- WeakHashMap（自动清理） ---");
        WeakCache weakCache = new WeakCache();

        String key2 = new String("key2");
        weakCache.put(key2, new byte[1024]);
        System.out.println("弱缓存大小: " + weakCache.size());

        // 取消强引用
        key2 = null;

        // GC - 条目将被移除
        System.gc();
        System.out.println("GC后，弱缓存大小: " + weakCache.size());
        System.out.println("成功：条目自动移除！\n");

        // 演示关键区别
        System.out.println("--- 关键区别 ---");
        System.out.println("WeakHashMap: 适合缓存，key不再使用时条目应被移除");
        System.out.println("");
        System.out.println("普通Map: key被强引用持有，阻止GC回收");

        // 运行LRU示例
        System.out.println("\n--- LRU缓存替代方案 ---");
        LRUCache.main(args);
    }
}
