package com.ssitao.code.jdk.phase02.set;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * HashSet 示例代码
 * 演示HashSet的常用操作和去重原理
 */
public class HashSetDemo {

    public static void main(String[] args) {
        System.out.println("=== HashSet Demo ===\n");

        // 1. 基本操作
        Set<String> set = new HashSet<>();
        set.add("apple");
        set.add("banana");
        set.add("apple");  // 重复元素会被忽略
        set.add("orange");
        System.out.println("HashSet: " + set);
        System.out.println("Size: " + set.size());  // 3，不是4

        // 2. 常用方法
        System.out.println("\n--- 常用方法 ---");
        System.out.println("contains('apple'): " + set.contains("apple"));
        System.out.println("contains('grape'): " + set.contains("grape"));
        System.out.println("isEmpty(): " + set.isEmpty());

        set.remove("banana");
        System.out.println("After remove('banana'): " + set);

        // 3. 遍历
        System.out.println("\n--- 遍历方式 ---");

        // 3.1 for-each
        System.out.print("For-each: ");
        for (String s : set) {
            System.out.print(s + " ");
        }
        System.out.println();

        // 3.2 Iterator
        System.out.print("Iterator: ");
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        // 3.3 forEach (JDK 8+)
        System.out.print("forEach: ");
        set.forEach(s -> System.out.print(s + " "));
        System.out.println();

        // 3.4 Stream
        System.out.print("Stream: ");
        set.stream().forEach(s -> System.out.print(s + " "));
        System.out.println();

        // 4. 批量操作
        System.out.println("\n--- 批量操作 ---");
        Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(4, 5, 6, 7, 8));

        System.out.println("set1: " + set1);
        System.out.println("set2: " + set2);

        // 交集
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection: " + intersection);

        // 并集
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("Union: " + union);

        // 差集 (set1 - set2)
        Set<Integer> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        System.out.println("Difference (set1 - set2): " + difference);

        // 对称差集 (set1 ∪ set2 - set1 ∩ set2)
        Set<Integer> symDiff = new HashSet<>(set1);
        symDiff.addAll(set2);
        symDiff.removeAll(intersection);
        System.out.println("Symmetric Difference: " + symDiff);

        // 5. 去重功能
        System.out.println("\n--- 去重功能 ---");
        java.util.List<String> listWithDups = Arrays.asList("a", "b", "c", "a", "d", "b");
        Set<String> unique = new HashSet<>(listWithDups);
        System.out.println("Original list: " + listWithDups);
        System.out.println("After removing duplicates: " + unique);

        // 6. 性能演示
        System.out.println("\n--- 性能演示 ---");
        int n = 100000;
        Set<Integer> perfSet = new HashSet<>();

        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            perfSet.add(i);
        }
        long addTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            perfSet.contains(i);
        }
        long containsTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            perfSet.remove(i);
        }
        long removeTime = System.nanoTime() - start;

        System.out.println("Add " + n + " elements: " + addTime / 1_000_000 + "ms");
        System.out.println("Contains " + n + " times: " + containsTime / 1_000_000 + "ms");
        System.out.println("Remove " + n + " elements: " + removeTime / 1_000_000 + "ms");

        // 7. HashSet的哈希原理
        System.out.println("\n--- 哈希原理 ---");
        System.out.println("HashSet内部使用HashMap存储");
        System.out.println("元素作为HashMap的key，value是固定的PRESENT对象");
        System.out.println("添加元素时计算hashCode，确定桶位置");
        System.out.println("查找时先比较hash，再比较equals");

        // 8. 重要规则
        System.out.println("\n--- 重要规则 ---");
        System.out.println("放入HashSet的自定义类必须正确重写equals()和hashCode()");
        System.out.println("相等的对象必须有相同的hashCode");
        System.out.println("HashSet允许一个null元素");

        // 9. 自定义对象放入HashSet
        System.out.println("\n--- 自定义对象 ---");
        Set<Person> people = new HashSet<>();
        people.add(new Person("Alice", 25));
        people.add(new Person("Bob", 30));
        people.add(new Person("Alice", 25));  // 会被视为重复
        System.out.println("People size: " + people.size());  // 2，不是3
    }

    static class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age && name.equals(person.name);
        }

        @Override
        public int hashCode() {
            return 31 * name.hashCode() + age;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + '}';
        }
    }
}
