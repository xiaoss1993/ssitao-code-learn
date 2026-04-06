package com.ssitao.code.effectivejava.ch01.item07;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Item 7: Demonstrate WeakHashMap for memory-sensitive caching
 *
 * WeakHashMap uses WeakReference for keys - entries are removed
 * when the key is no longer strongly reachable
 */
public class WeakHashMapCache {

    // ==================== Problem: Memory Leak with Regular Map ====================
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

        // Problem: No way to evict entries automatically
    }

    // ==================== Solution: WeakHashMap ====================
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

        // Entries automatically evicted when key is GC'd
    }

    // ==================== LRU Cache with LinkedHashMap ====================
    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int maxCapacity;

        public LRUCache(int capacity) {
            super(capacity, 0.75f, true);  // accessOrder = true for LRU
            this.maxCapacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxCapacity;
        }

        public static void main(String[] args) {
            System.out.println("=== LRU Cache Demo ===");
            LRUCache<String, String> lru = new LRUCache<>(3);
            lru.put("A", "1");
            lru.put("B", "2");
            lru.put("C", "3");
            System.out.println("Initial: " + lru);

            // Access A, making it recently used
            lru.get("A");
            System.out.println("After accessing A: " + lru);

            // Add D, should evict B (least recently used)
            lru.put("D", "4");
            System.out.println("After adding D: " + lru);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== WeakHashMap vs Regular HashMap ===\n");

        // Regular cache - memory leak potential
        System.out.println("--- Regular HashMap (memory leak risk) ---");
        RegularCache regularCache = new RegularCache();

        // Create a key that's only reachable through the cache
        String key1 = new String("key1");
        regularCache.put(key1, new byte[1024]);
        System.out.println("Regular cache size: " + regularCache.size());

        // Clear strong reference
        key1 = null;

        // GC - cache entry still exists!
        System.gc();
        System.out.println("After GC, regular cache size: " + regularCache.size());
        System.out.println("Problem: Entry not removed - memory leak!\n");

        // WeakHashMap cache - automatically cleaned
        System.out.println("--- WeakHashMap (auto-cleanup) ---");
        WeakCache weakCache = new WeakCache();

        String key2 = new String("key2");
        weakCache.put(key2, new byte[1024]);
        System.out.println("Weak cache size: " + weakCache.size());

        // Clear strong reference
        key2 = null;

        // GC - entry will be removed
        System.gc();
        System.out.println("After GC, weak cache size: " + weakCache.size());
        System.out.println("Success: Entry automatically removed!\n");

        // Demonstrate the difference
        System.out.println("--- Key Difference ---");
        System.out.println("WeakHashMap: Good for caches where entries should be");
        System.out.println("             removed when keys are no longer used elsewhere");
        System.out.println("");
        System.out.println("Regular Map: Keys are strongly held, preventing GC");

        // Run LRU demo
        System.out.println("\n--- LRU Cache Alternative ---");
        LRUCache.main(args);
    }
}
