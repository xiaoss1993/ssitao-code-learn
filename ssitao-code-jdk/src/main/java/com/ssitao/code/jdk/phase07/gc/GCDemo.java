package com.ssitao.code.jdk.phase07.gc;

import java.lang.ref.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * GC演示 - 展示各种引用和GC行为
 */
public class GCDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== GC Demo ===\n");

        // 1. 强引用
        demonstrateStrongReference();

        // 2. 软引用
        demonstrateSoftReference();

        // 3. 弱引用
        demonstrateWeakReference();

        // 4. 虚引用
        demonstratePhantomReference();

        // 5. 对象晋升
        demonstrateObjectPromotion();

        // 6. GC日志参数说明
        demonstrateGCParmas();
    }

    private static void demonstrateStrongReference() {
        System.out.println("--- Strong Reference ---");

        // 普通对象创建
        for (int i = 0; i < 3; i++) {
            Object obj = new Object();
            System.out.println("Created Object: " + obj);
            // obj超出作用域，但对象仍被GC ROOT引用持有
        }

        // 显式置null，加速GC
        Object strong = new Object();
        System.out.println("Strong reference: " + strong);
        strong = null;  // 断开引用
        System.gc();    // 建议GC，但不保证立即执行
        System.runFinalization();

        System.out.println("After GC\n");
    }

    private static void demonstrateSoftReference() {
        System.out.println("--- Soft Reference ---");

        // 软引用列表
        List<SoftReference<byte[]>> softRefs = new ArrayList<>();

        // 创建软引用
        for (int i = 0; i < 5; i++) {
            byte[] data = new byte[10 * 1024 * 1024];  // 10MB
            SoftReference<byte[]> ref = new SoftReference<>(data);
            softRefs.add(ref);
            System.out.println("Created SoftReference #" + i + ", accessible: " + ref.get());
        }

        // 尝试获取对象
        System.out.println("\nTrying to get objects:");
        for (int i = 0; i < softRefs.size(); i++) {
            SoftReference<byte[]> ref = softRefs.get(i);
            System.out.println("Ref #" + i + ": " + (ref.get() != null ? "Alive" : "GC'd"));
        }

        // 建议GC（内存充足时通常不会回收软引用）
        System.gc();
        System.runFinalization();

        System.out.println("\nAfter GC:");
        for (int i = 0; i < softRefs.size(); i++) {
            SoftReference<byte[]> ref = softRefs.get(i);
            System.out.println("Ref #" + i + ": " + (ref.get() != null ? "Alive" : "GC'd"));
        }

        System.out.println();
    }

    private static void demonstrateWeakReference() {
        System.out.println("--- Weak Reference ---");

        // 创建弱引用
        WeakReference<String> weakRef = new WeakReference<>(new String("Weak String"));
        System.out.println("Before GC: " + weakRef.get());

        // 强引用断开
        String strong = weakRef.get();
        System.out.println("Strong ref: " + strong);

        // 第一次GC - 可能被回收（取决于GC实现）
        System.gc();
        System.runFinalization();
        System.out.println("After GC (strong ref still exists): " + weakRef.get());

        // 断开强引用
        strong = null;

        // 第二次GC - 弱引用对象必然被回收
        System.gc();
        System.runFinalization();
        System.out.println("After GC (no strong ref): " + weakRef.get());

        System.out.println();

        // WeakHashMap示例 - 自动清理过期条目
        System.out.println("WeakHashMap Demo:");
        WeakHashMap<String, String> cache = new WeakHashMap<>();
        String key = new String("important");
        cache.put(key, "value");
        System.out.println("Before: " + cache.get(key));

        key = null;  // 断开强引用
        System.gc();
        System.runFinalization();
        System.out.println("After GC: " + cache.get(key));

        System.out.println();
    }

    private static void demonstratePhantomReference() throws Exception {
        System.out.println("--- Phantom Reference ---");

        // 引用队列
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        List<PhantomReference<byte[]>> phantomRefs = new ArrayList<>();

        // 创建虚引用
        for (int i = 0; i < 3; i++) {
            byte[] data = new byte[1024 * 1024];  // 1MB
            PhantomReference<byte[]> ref = new PhantomReference<>(data, queue);
            phantomRefs.add(ref);
            data = null;  // 断开强引用
        }

        System.out.println("Before GC:");
        for (PhantomReference<byte[]> ref : phantomRefs) {
            System.out.println("  Pending: " + ref.isEnqueued());
        }

        // 触发GC
        System.gc();
        Thread.sleep(100);
        System.runFinalization();

        System.out.println("\nAfter GC:");
        for (PhantomReference<byte[]> ref : phantomRefs) {
            System.out.println("  Pending: " + ref.isEnqueued());
        }

        // 处理引用队列
        Reference<?> ref;
        while ((ref = queue.poll()) != null) {
            System.out.println("Cleaned up: " + ref);
        }

        System.out.println();
    }

    private static void demonstrateObjectPromotion() {
        System.out.println("--- Object Promotion ---");

        // 模拟对象在Survivor区之间的移动
        // 和晋升到老年代

        // 实际中难以直接演示，但可以说明原理
        // 对象头中有age字段记录经历GC的次数
        // 每次Minor GC后，存活对象从Eden复制到Survivor
        // 年龄+1，达到阈值后晋升老年代

        System.out.println("Objects in Survivor space:");
        System.out.println("  - Age 0: just allocated");
        System.out.println("  - Age 1-14: in Survivor space, copying between S0 and S1");
        System.out.println("  - Age 15+: promoted to Old generation");
        System.out.println();
        System.out.println("Promotion threshold can be set with:");
        System.out.println("  -XX:MaxTenuringThreshold=N");
        System.out.println();

        // 示例：短期对象 vs 长期对象
        List<Object> shortLived = new ArrayList<>();
        List<Object> longLived = new ArrayList<>();
        Object anchor = new Object();  // 长期对象

        for (int i = 0; i < 100; i++) {
            shortLived.add(new Object());  // 短期对象
        }
        longLived.add(anchor);  // 长期对象

        System.out.println("Short-lived objects: will be collected in Minor GC");
        System.out.println("Long-lived object (anchor): will eventually be promoted to Old");
        System.out.println();
    }

    private static void demonstrateGCParmas() {
        System.out.println("--- GC Parameters ---");

        System.out.println("常用GC参数：");
        System.out.println();
        System.out.println("收集器选择：");
        System.out.println("  -XX:+UseSerialGC        # Serial收集器（单线程）");
        System.out.println("  -XX:+UseParallelGC      # Parallel收集器（吞吐量优先）");
        System.out.println("  -XX:+UseConcMarkSweepGC # CMS收集器（低停顿）");
        System.out.println("  -XX:+UseG1GC           # G1收集器（分区算法）");
        System.out.println("  -XX:+UseZGC            # ZGC（JDK 11+，超低停顿）");
        System.out.println();
        System.out.println("内存参数：");
        System.out.println("  -Xms256m               # 初始堆大小");
        System.out.println("  -Xmx1024m              # 最大堆大小");
        System.out.println("  -Xmn128m               # 新生代大小");
        System.out.println("  -XX:NewRatio=2         # 老年代/新生代比例");
        System.out.println("  -XX:SurvivorRatio=8    # Eden/Survivor比例");
        System.out.println();
        System.out.println("GC调优：");
        System.out.println("  -XX:MaxGCPauseMillis=200  # 最大GC停顿时间目标");
        System.out.println("  -XX:GCTimeRatio=19        # 吞吐量目标");
        System.out.println("  -XX:+UseAdaptiveSizePolicy # 自适应大小调节");
        System.out.println();
        System.out.println("OOM处理：");
        System.out.println("  -XX:+HeapDumpOnOutOfMemoryError");
        System.out.println("  -XX:HeapDumpPath=/path/to/dump");
        System.out.println();
        System.out.println("日志：");
        System.out.println("  -Xlog:gc*:file=gc.log    # 打印GC日志");
        System.out.println();

        // 打印当前JVM配置
        System.out.println("当前JVM配置：");
        System.out.println("  Max Heap (Xmx): " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB");
        System.out.println("  Total Heap: " + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "MB");
        System.out.println("  Free Heap: " + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "MB");
        System.out.println("  Available Processors: " + Runtime.getRuntime().availableProcessors());
    }
}
