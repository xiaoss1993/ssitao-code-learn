package com.ssitao.code.jdk.phase02.set;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * TreeSet 示例代码
 * 演示TreeSet的有序特性和导航操作
 */
public class TreeSetDemo {

    public static void main(String[] args) {
        System.out.println("=== TreeSet Demo ===\n");

        // 1. 基本操作
        TreeSet<Integer> set = new TreeSet<>();
        set.add(5);
        set.add(2);
        set.add(8);
        set.add(1);
        set.add(9);
        System.out.println("TreeSet (sorted): " + set);
        System.out.println("First: " + set.first());
        System.out.println("Last: " + set.last());

        // 2. 导航操作
        System.out.println("\n--- 导航操作 ---");
        System.out.println("lower(5): " + set.lower(5));      // < 5的最大元素
        System.out.println("floor(5): " + set.floor(5));      // <= 5的最大元素
        System.out.println("higher(5): " + set.higher(5));    // > 5的最小元素
        System.out.println("ceiling(5): " + set.ceiling(5));  // >= 5的最小元素

        // 3. 子集操作
        System.out.println("\n--- 子集操作 ---");
        System.out.println("set: " + set);
        System.out.println("subSet(2, 8): " + set.subSet(2, 8));      // [2, 8)
        System.out.println("subSet(2, true, 8, true): " + set.subSet(2, true, 8, true)); // [2, 8]
        System.out.println("headSet(5): " + set.headSet(5));          // < 5
        System.out.println("headSet(5, true): " + set.headSet(5, true)); // <= 5
        System.out.println("tailSet(5): " + set.tailSet(5));          // >= 5
        System.out.println("tailSet(5, false): " + set.tailSet(5, false)); // > 5

        // 4. 删除操作
        System.out.println("\n--- 删除操作 ---");
        TreeSet<Integer> set2 = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        System.out.println("Original: " + set2);
        System.out.println("pollFirst(): " + set2.pollFirst());  // 移除并返回第一个
        System.out.println("pollLast(): " + set2.pollLast());   // 移除并返回最后一个
        System.out.println("After polls: " + set2);

        // 5. 自定义比较器 - 降序
        System.out.println("\n--- 降序排列 ---");
        TreeSet<Integer> descSet = new TreeSet<>(Comparator.reverseOrder());
        descSet.addAll(Arrays.asList(5, 2, 8, 1, 9, 3, 7));
        System.out.println("Descending: " + descSet);

        // 6. 按字符串长度排序
        System.out.println("\n--- 按字符串长度排序 ---");
        TreeSet<String> lengthSet = new TreeSet<>(Comparator.comparingInt(String::length));
        lengthSet.addAll(Arrays.asList("apple", "pie", "banana", "hi"));
        System.out.println("By length: " + lengthSet);

        // 7. 自定义对象排序
        System.out.println("\n--- 自定义对象排序 ---");
        TreeSet<Person> people = new TreeSet<>(Comparator.comparingInt(p -> p.age));
        people.add(new Person("Alice", 25));
        people.add(new Person("Bob", 30));
        people.add(new Person("Charlie", 20));
        System.out.println("People (by age): " + people);

        // 8. TreeSet vs HashSet
        System.out.println("\n--- TreeSet vs HashSet ---");
        int n = 100000;

        long start = System.nanoTime();
        TreeSet<Integer> treeSet = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            treeSet.add(i);
        }
        long treeSetAddTime = System.nanoTime() - start;

        start = System.nanoTime();
        java.util.HashSet<Integer> hashSet = new java.util.HashSet<>();
        for (int i = 0; i < n; i++) {
            hashSet.add(i);
        }
        long hashSetAddTime = System.nanoTime() - start;

        System.out.println("TreeSet add time: " + treeSetAddTime / 1_000_000 + "ms (O(log n))");
        System.out.println("HashSet add time: " + hashSetAddTime / 1_000_000 + "ms (O(1))");

        // 9. null支持
        System.out.println("\n--- null支持 ---");
        TreeSet<String> withNull = new TreeSet<>();
        try {
            withNull.add(null);  // TreeSet不允许null（会抛异常）
        } catch (Exception e) {
            System.out.println("TreeSet不允许null值（第一个添加的null除外）");
        }
        System.out.println("HashSet允许一个null值");

        // 10. 使用场景
        System.out.println("\n--- 使用场景 ---");
        System.out.println("1. 需要元素自动排序");
        System.out.println("2. 需要导航操作（lower, floor, higher, ceiling）");
        System.out.println("3. 需要范围查询（subSet, headSet, tailSet）");
        System.out.println("4. 需要first/last操作");
    }

    static class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + '}';
        }
    }
}
