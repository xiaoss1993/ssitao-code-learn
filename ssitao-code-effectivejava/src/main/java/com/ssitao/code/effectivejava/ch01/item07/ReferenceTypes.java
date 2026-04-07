package com.ssitao.code.effectivejava.ch01.item07;

import java.lang.ref.WeakReference;
import java.lang.ref.SoftReference;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 条目7：演示Java中的不同引用类型
 *
 * Java引用类型从强到弱分为：
 * 1. 强引用(Strong Reference)：最常见，GC不会回收强可达对象
 * 2. 软引用(Soft Reference)：内存不足时回收，适合缓存
 * 3. 弱引用(Weak Reference)：下次GC时回收，即使内存充足
 * 4. 虚引用(Phantom Reference)：始终返回null，用于对象销毁前的清理工作
 */
public class ReferenceTypes {

    // ==================== 1. 强引用 ====================
    // 普通对象引用，只要强引用存在，GC就不会回收
    static void strongReferenceDemo() {
        System.out.println("=== 强引用示例 ===");
        Object obj = new Object();  // 强引用
        System.out.println("GC前: " + obj);

        // 取消强引用，使对象可被回收
        obj = null;
        System.out.println("obj = null后，但GC不一定立即执行");

        // 建议执行GC
        System.gc();
        System.out.println("已请求GC（可能不会立即执行）");
    }

    // ==================== 2. 软引用 ====================
    // 内存不足时回收，适合实现内存敏感型缓存
    static void softReferenceDemo() {
        System.out.println("\n=== 软引用示例 ===");

        // 软引用指向一个大对象
        SoftReference<byte[]> softRef = new SoftReference<>(new byte[1024 * 1024 * 10]); // 10MB

        System.out.println("软引用是否存在: " + (softRef.get() != null));

        // 使对象可被GC回收
        // 实践中，软引用会在OutOfMemoryError之前被清除
        System.gc();

        System.out.println("GC后软引用: " + (softRef.get() != null));
        System.out.println("适用场景：内存敏感型缓存");
    }

    // ==================== 3. 弱引用 ====================
    // 下次GC时回收，即使内存充足
    static void weakReferenceDemo() {
        System.out.println("\n=== 弱引用示例 ===");

        // WeakHashMap示例 - 当key被GC后，条目自动移除
        WeakHashMap<String, String> cache = new WeakHashMap<>();

        String key = new String("importantKey");  // 可被GC回收
        cache.put(key, "importantValue");

        System.out.println("GC前: cache.size() = " + cache.size());
        System.out.println("key仍然存活: " + (key != null));

        // 取消强引用
        key = null;

        // 请求GC - key将被立即回收
        System.gc();

        System.out.println("GC后: cache.size() = " + cache.size());
        System.out.println("适用场景：规范映射（如WeakHashMap）、短期缓存");
    }

    // ==================== 4. 自定义WeakCache实现 ====================
    /**
     * 简单的弱引用缓存
     */
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
                // 条目已被GC，移除
                cache.remove(key);
            }
            return value;
        }

        public int size() {
            // 先清理空引用
            cache.entrySet().removeIf(e -> e.getValue().get() == null);
            return cache.size();
        }
    }

    static void weakCacheDemo() {
        System.out.println("\n=== WeakCache示例 ===");

        SimpleWeakCache<String, byte[]> cache = new SimpleWeakCache<>();

        // 创建只在缓存中可达的值
        byte[] value = new byte[1024];
        cache.put("key1", value);

        System.out.println("清空前缓存大小: " + cache.size());

        // 取消强引用
        value = null;

        // GC将回收value
        System.gc();

        System.out.println("GC后缓存大小: " + cache.size());
    }

    // ==================== 5. 虚引用 ====================
    // 始终返回null，用于对象销毁前的清理工作
    static void phantomReferenceDemo() {
        System.out.println("\n=== 虚引用示例 ===");

        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        byte[] bigArray = new byte[1024 * 1024]; // 1MB

        PhantomReference<Object> phantomRef = new PhantomReference<>(bigArray, queue);

        System.out.println("虚引用已创建");
        System.out.println("phantomRef.get()始终返回: " + phantomRef.get());
        System.out.println("bigArray仍然存活: " + (bigArray != null));

        // 取消强引用
        bigArray = null;

        // GC - 虚引用会被终结但不会立即清除
        System.gc();

        System.out.println("适用场景：对象销毁前的清理工作（关闭文件、释放原生资源）");
    }

    // ==================== 6. 引用队列示例 ====================
    static void referenceQueueDemo() {
        System.out.println("\n=== 引用队列示例 ===");

        ReferenceQueue<byte[]> queue = new ReferenceQueue<>();
        byte[] data = new byte[1024];
        WeakReference<byte[]> ref = new WeakReference<>(data, queue);

        // 取消强引用
        data = null;

        // GC后检查队列
        System.gc();

        try {
            Thread.sleep(100);  // 给GC时间完成终结
        } catch (InterruptedException e) {
        }

        // 检查引用是否已进入队列
        System.out.println("引用已在队列中: " + (queue.poll() != null));
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Java引用类型示例");
        System.out.println("========================================\n");

        strongReferenceDemo();
        softReferenceDemo();
        weakReferenceDemo();
        weakCacheDemo();
        phantomReferenceDemo();
        referenceQueueDemo();

        System.out.println("\n========================================");
        System.out.println("总结：");
        System.out.println("----------------------------------------");
        System.out.println("| 类型    | GC行为                           |");
        System.out.println("|---------|----------------------------------|");
        System.out.println("| 强引用  | 只要可达，永不回收                |");
        System.out.println("| 软引用  | 内存不足时回收                    |");
        System.out.println("| 弱引用  | 下次GC时回收                      |");
        System.out.println("| 虚引用  | 始终返回null，用于清理            |");
        System.out.println("========================================");
    }
}
