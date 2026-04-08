package com.ssitao.code.jdk.phase01.utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 第一阶段步骤4: 常用工具类 - Collections工具类演示
 */
public class CollectionsDemo {

    public static void main(String[] args) {
        System.out.println("=== Collections工具类 ===\n");

        // 1. 基本操作
        System.out.println("--- 1. 基本操作 ---");
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
        System.out.println("原集合: " + list);
        System.out.println("max: " + Collections.max(list));
        System.out.println("min: " + Collections.min(list));
        System.out.println("frequency('a'): " + Collections.frequency(list, "a"));
        System.out.println();

        // 2. 排序与打乱
        System.out.println("--- 2. 排序与打乱 ---");
        List<Integer> nums = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9));
        System.out.println("排序前: " + nums);

        Collections.sort(nums);
        System.out.println("正序: " + nums);

        Collections.sort(nums, Comparator.reverseOrder());
        System.out.println("降序: " + nums);

        Collections.shuffle(nums);
        System.out.println("打乱后: " + nums);
        System.out.println();

        // 3. 查找
        System.out.println("--- 3. 查找 ---");
        Collections.sort(nums);  // 二分查找必须先排序
        System.out.println("排序后: " + nums);
        System.out.println("binarySearch(5): " + Collections.binarySearch(nums, 5));
        System.out.println();

        // 4. 替换与交换
        System.out.println("--- 4. 替换与交换 ---");
        List<String> list2 = new ArrayList<>(Arrays.asList("a", "b", "a", "c"));
        System.out.println("原集合: " + list2);

        Collections.replaceAll(list2, "a", "x");
        System.out.println("replaceAll('a'->'x'): " + list2);

        Collections.swap(list2, 0, 1);
        System.out.println("swap(0,1): " + list2);
        System.out.println();

        // 5. 不可变集合
        System.out.println("--- 5. 不可变集合 ---");
        List<String> immutable = Collections.unmodifiableList(list);
        System.out.println("unmodifiableList: " + immutable);
        System.out.println();

        // 6. 单元素集合
        System.out.println("--- 6. 单元素集合 ---");
        List<String> singleton = Collections.singletonList("only");
        System.out.println("singletonList: " + singleton);
        System.out.println();

        // 7. 集合运算
        System.out.println("--- 7. 集合运算 ---");
        Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));

        // 交集
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("交集: " + intersection);

        // 并集
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("并集: " + union);

        // 差集
        Set<Integer> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        System.out.println("差集(set1-set2): " + difference);
    }
}
