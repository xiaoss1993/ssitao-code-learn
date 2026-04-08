package com.ssitao.code.jdk.phase02.map;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * TreeMap 示例代码
 * 演示TreeMap的有序特性和导航操作
 */
public class TreeMapDemo {

    public static void main(String[] args) {
        System.out.println("=== TreeMap Demo ===\n");

        // 1. 基本操作
        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(5, "five");
        map.put(2, "two");
        map.put(8, "eight");
        map.put(1, "one");
        map.put(9, "nine");
        System.out.println("TreeMap (sorted by key): " + map);
        System.out.println("First key: " + map.firstKey());
        System.out.println("Last key: " + map.lastKey());

        // 2. 导航操作
        System.out.println("\n--- 导航操作 ---");
        System.out.println("lowerKey(5): " + map.lowerKey(5));      // < 5的最大key
        System.out.println("floorKey(5): " + map.floorKey(5));      // <= 5的最大key
        System.out.println("higherKey(5): " + map.higherKey(5));    // > 5的最小key
        System.out.println("ceilingKey(5): " + map.ceilingKey(5));  // >= 5的最小key

        // 3. 子Map操作
        System.out.println("\n--- 子Map操作 ---");
        System.out.println("subMap(2, 8): " + map.subMap(2, 8));          // [2, 8)
        System.out.println("subMap(2, true, 8, true): " + map.subMap(2, true, 8, true)); // [2, 8]
        System.out.println("headMap(5): " + map.headMap(5));              // < 5
        System.out.println("headMap(5, true): " + map.headMap(5, true));  // <= 5
        System.out.println("tailMap(5): " + map.tailMap(5));              // >= 5
        System.out.println("tailMap(5, false): " + map.tailMap(5, false)); // > 5

        // 4. 删除操作
        System.out.println("\n--- 删除操作 ---");
        TreeMap<Integer, String> map2 = new TreeMap<>();
        map2.putAll(map);
        System.out.println("Original: " + map2);
        System.out.println("pollFirstEntry(): " + map2.pollFirstEntry());  // 移除并返回第一个
        System.out.println("pollLastEntry(): " + map2.pollLastEntry());   // 移除并返回最后一个
        System.out.println("After polls: " + map2);

        // 5. 降序排列
        System.out.println("\n--- 降序排列 ---");
        TreeMap<Integer, String> descMap = new TreeMap<>(Comparator.reverseOrder());
        descMap.putAll(map);
        System.out.println("Descending: " + descMap);

        // 6. 自定义比较器 - 按字符串长度排序key
        System.out.println("\n--- 按key的字符串长度排序 ---");
        TreeMap<String, Integer> lengthMap = new TreeMap<>(Comparator.comparingInt(String::length));
        lengthMap.put("apple", 1);
        lengthMap.put("hi", 2);
        lengthMap.put("banana", 3);
        System.out.println("By key length: " + lengthMap);

        // 7. TreeMap vs HashMap
        System.out.println("\n--- TreeMap vs HashMap ---");
        int n = 100000;

        long start = System.nanoTime();
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            treeMap.put(i, i);
        }
        long treePutTime = System.nanoTime() - start;

        start = System.nanoTime();
        java.util.HashMap<Integer, Integer> hashMap = new java.util.HashMap<>();
        for (int i = 0; i < n; i++) {
            hashMap.put(i, i);
        }
        long hashPutTime = System.nanoTime() - start;

        System.out.println("TreeMap put: " + treePutTime / 1_000_000 + "ms (O(log n))");
        System.out.println("HashMap put: " + hashPutTime / 1_000_000 + "ms (O(1))");

        // 8. null支持
        System.out.println("\n--- null支持 ---");
        try {
            TreeMap<String, Integer> nullMap = new TreeMap<>();
            nullMap.put(null, 1);  // TreeMap不允许null键
        } catch (Exception e) {
            System.out.println("TreeMap不允许null键");
        }
        System.out.println("HashMap允许一个null键");

        // 9. 使用场景
        System.out.println("\n--- 使用场景 ---");
        System.out.println("1. 需要按键自动排序");
        System.out.println("2. 需要导航操作（lower, floor, higher, ceiling）");
        System.out.println("3. 需要范围查询（subMap, headMap, tailMap）");
        System.out.println("4. 需要first/last操作");
        System.out.println("5. 需要按照键的有序遍历");
    }
}
