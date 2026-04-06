package com.ssitao.code.effectivejava.ch01.item07;

import java.lang.ref.WeakReference;
import java.lang.ref.SoftReference;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Item 7: Demonstrate different reference types in Java
 *
 * Strong Reference, Soft Reference, Weak Reference, Phantom Reference
 */
public class ReferenceTypes {

    // ==================== 1. Strong Reference ====================
    // Normal object reference, never GC'd while strongly reachable
    static void strongReferenceDemo() {
        System.out.println("=== Strong Reference Demo ===");
        Object obj = new Object();  // Strong reference
        System.out.println("Before GC: " + obj);

        // Make eligible for GC
        obj = null;
        System.out.println("After obj = null, but GC not forced");

        // Suggest GC
        System.gc();
        System.out.println("GC suggested (may not run immediately)");
    }

    // ==================== 2. Soft Reference ====================
    // GC'd when memory is low, good for cache
    static void softReferenceDemo() {
        System.out.println("\n=== Soft Reference Demo ===");

        // Soft reference to a large object
        SoftReference<byte[]> softRef = new SoftReference<>(new byte[1024 * 1024 * 10]); // 10MB

        System.out.println("Soft reference exists: " + (softRef.get() != null));

        // Make object eligible for GC
        // In practice, soft references are cleared before OutOfMemoryError
        System.gc();

        System.out.println("Soft reference after GC: " + (softRef.get() != null));
        System.out.println("Use case: Memory-sensitive cache");
    }

    // ==================== 3. Weak Reference ====================
    // GC'd on next GC cycle, even if memory is sufficient
    static void weakReferenceDemo() {
        System.out.println("\n=== Weak Reference Demo ===");

        // WeakHashMap example - entries removed when key is GC'd
        WeakHashMap<String, String> cache = new WeakHashMap<>();

        String key = new String("importantKey");  // GC-eligible
        cache.put(key, "importantValue");

        System.out.println("Before GC: cache.size() = " + cache.size());
        System.out.println("key is still alive: " + (key != null));

        // Clear strong reference
        key = null;

        // Request GC - key will be collected immediately
        System.gc();

        System.out.println("After GC: cache.size() = " + cache.size());
        System.out.println("Use case: Canonicalized mappings (caches with short lifespan)");
    }

    // ==================== 4. Custom WeakHashMap Implementation ====================
    static class SimpleWeakCache<K, V> {
        private final Map<K, WeakReference<V>> cache = new HashMap<>();

        public void put(K key, V value) {
            cache.put(key, new WeakReference<>(value));
        }

        public V get(K key) {
            WeakReference<V> ref = cache.get(key);
            if (ref == null) {
                return null;
            }
            V value = ref.get();
            if (value == null) {
                // Entry was GC'd, remove from cache
                cache.remove(key);
            }
            return value;
        }

        public int size() {
            // Clean up null references first
            cache.entrySet().removeIf(e -> e.getValue().get() == null);
            return cache.size();
        }
    }

    static void weakCacheDemo() {
        System.out.println("\n=== WeakCache Demo ===");

        SimpleWeakCache<String, byte[]> cache = new SimpleWeakCache<>();

        // Create value that's only reachable through cache
        byte[] value = new byte[1024];
        cache.put("key1", value);

        System.out.println("Cache size before clear: " + cache.size());

        // Clear the strong reference
        value = null;

        // GC will collect the value
        System.gc();

        System.out.println("Cache size after GC: " + cache.size());
    }

    // ==================== 5. Phantom Reference ====================
    // Always returns null, used for pre-mortem cleanup
    static void phantomReferenceDemo() {
        System.out.println("\n=== Phantom Reference Demo ===");

        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        byte[] bigArray = new byte[1024 * 1024]; // 1MB

        PhantomReference<Object> phantomRef = new PhantomReference<>(bigArray, queue);

        System.out.println("Phantom reference created");
        System.out.println("phantomRef.get() always returns: " + phantomRef.get());
        System.out.println("bigArray is still alive: " + (bigArray != null));

        // Clear strong reference
        bigArray = null;

        // GC - phantom references are finalized but not cleared immediately
        System.gc();

        System.out.println("Use case: Pre-mortem cleanup actions (close files, release native resources)");
    }

    // ==================== 6. Reference Queue Demo ====================
    static void referenceQueueDemo() {
        System.out.println("\n=== Reference Queue Demo ===");

        ReferenceQueue<byte[]> queue = new ReferenceQueue<>();
        byte[] data = new byte[1024];
        WeakReference<byte[]> ref = new WeakReference<>(data, queue);

        // Clear strong reference
        data = null;

        // Poll the queue after GC
        System.gc();

        try {
            Thread.sleep(100);  // Give GC time to finalize
        } catch (InterruptedException e) {
        }

        // Check if reference was enqueued
        System.out.println("Reference in queue: " + (queue.poll() != null));
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Java Reference Types Demo");
        System.out.println("========================================\n");

        strongReferenceDemo();
        softReferenceDemo();
        weakReferenceDemo();
        weakCacheDemo();
        phantomReferenceDemo();
        referenceQueueDemo();

        System.out.println("\n========================================");
        System.out.println("Summary:");
        System.out.println("----------------------------------------");
        System.out.println("| Type    | GC Behavior                    |");
        System.out.println("|---------|-------------------------------|");
        System.out.println("| Strong  | Never collected while reachable |");
        System.out.println("| Soft    | Collected when memory is low   |");
        System.out.println("| Weak    | Collected on next GC cycle    |");
        System.out.println("| Phantom | Always returns null, cleanup  |");
        System.out.println("========================================");
    }
}
