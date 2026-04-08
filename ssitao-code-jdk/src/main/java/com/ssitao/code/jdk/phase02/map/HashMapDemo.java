package com.ssitao.code.jdk.phase02.map;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap 示例代码
 * 演示HashMap的常用操作和底层原理
 */
public class HashMapDemo {

    public static void main(String[] args) {
        System.out.println("=== HashMap Demo ===\n");

        // 1. 基本操作
        HashMap<String, Integer> map = new HashMap<>();
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("orange", 3);
        map.put("apple", 10);  // 更新apple的值
        System.out.println("HashMap: " + map);
        System.out.println("Size: " + map.size());

        // 2. 获取
        System.out.println("\n--- 获取操作 ---");
        System.out.println("get('apple'): " + map.get("apple"));
        System.out.println("get('grape'): " + map.get("grape"));
        System.out.println("getOrDefault('grape', 0): " + map.getOrDefault("grape", 0));

        // 3. 判断
        System.out.println("\n--- 判断操作 ---");
        System.out.println("containsKey('apple'): " + map.containsKey("apple"));
        System.out.println("containsValue(10): " + map.containsValue(10));
        System.out.println("isEmpty(): " + map.isEmpty());

        // 4. 删除
        System.out.println("\n--- 删除操作 ---");
        map.remove("banana");
        System.out.println("After remove('banana'): " + map);
        map.remove("apple", 1);  // 仅当value为1时删除
        System.out.println("After remove('apple', 1) - no effect (apple=10): " + map);

        // 5. 遍历方式
        System.out.println("\n--- 遍历方式 ---");
        HashMap<String, Integer> map2 = new HashMap<>();
        map2.put("a", 1);
        map2.put("b", 2);
        map2.put("c", 3);

        // 5.1 遍历keySet
        System.out.print("keySet: ");
        for (String key : map2.keySet()) {
            System.out.print(key + " ");
        }
        System.out.println();

        // 5.2 遍历values
        System.out.print("values: ");
        for (Integer value : map2.values()) {
            System.out.print(value + " ");
        }
        System.out.println();

        // 5.3 遍历entrySet (推荐)
        System.out.print("entrySet: ");
        for (Map.Entry<String, Integer> entry : map2.entrySet()) {
            System.out.print(entry.getKey() + "=" + entry.getValue() + " ");
        }
        System.out.println();

        // 5.4 forEach (JDK 8+)
        System.out.print("forEach: ");
        map2.forEach((k, v) -> System.out.print(k + "=" + v + " "));
        System.out.println();

        // 6. JDK 8+ compute方法
        System.out.println("\n--- JDK 8+ compute方法 ---");
        HashMap<String, Integer> computeMap = new HashMap<>();
        computeMap.put("a", 1);
        computeMap.put("b", 2);

        computeMap.compute("a", (k, v) -> v == null ? 1 : v + 10);  // a的value+10
        System.out.println("After compute('a', +10): " + computeMap);

        computeMap.computeIfAbsent("c", k -> k.length());  // c不存在才计算
        System.out.println("After computeIfAbsent('c', len): " + computeMap);

        computeMap.computeIfPresent("a", (k, v) -> v + 1);  // a存在才计算
        System.out.println("After computeIfPresent('a', +1): " + computeMap);

        // 7. merge方法
        System.out.println("\n--- merge方法 ---");
        HashMap<String, Integer> mergeMap = new HashMap<>();
        mergeMap.put("a", 1);
        mergeMap.merge("a", 2, Integer::sum);  // a存在，1+2=3
        System.out.println("After merge('a', 2, sum): " + mergeMap);

        // 8. 性能测试
        System.out.println("\n--- 性能测试 ---");
        int n = 100000;
        HashMap<Integer, Integer> perfMap = new HashMap<>();

        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            perfMap.put(i, i);
        }
        long putTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            perfMap.get(i);
        }
        long getTime = System.nanoTime() - start;

        System.out.println("Put " + n + " entries: " + putTime / 1_000_000 + "ms");
        System.out.println("Get " + n + " entries: " + getTime / 1_000_000 + "ms");

        // 9. null键和null值
        System.out.println("\n--- null支持 ---");
        HashMap<String, Integer> nullMap = new HashMap<>();
        nullMap.put(null, 1);       // 一个null键
        nullMap.put("a", null);    // 多个null值
        System.out.println("nullMap: " + nullMap);

        // 10. 容量和负载因子
        System.out.println("\n--- 容量和负载因子 ---");
        HashMap<String, Integer> capacityMap = new HashMap<>(32, 0.5f);
        System.out.println("Initial capacity: 32, load factor: 0.5");
        System.out.println("Default: capacity=16, loadFactor=0.75");
        System.out.println("扩容时容量翻倍");
    }
}
