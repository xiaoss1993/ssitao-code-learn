package com.ssitao.code.designpattern.iterator.jdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Enumeration;
import java.util.Vector;

/**
 * JDK迭代器使用示例
 *
 * JDK中的迭代器接口：
 * 1. Iterator - 通用迭代器
 * 2. ListIterator - List专用双向迭代器
 * 3. Enumeration - 传统迭代器（遗留类）
 *
 * 迭代器注意：
 * - 迭代过程中不能修改集合（ConcurrentModificationException）
 * - 可以使用Iterator的remove()方法安全删除
 */
public class JdkIteratorDemo {

    public static void main(String[] args) {
        System.out.println("=== JDK迭代器使用示例 ===\n");

        // 1. ListIterator双向迭代
        System.out.println("1. ListIterator双向迭代");
        listIteratorDemo();

        // 2. 使用迭代器删除元素
        System.out.println("\n2. 使用迭代器安全删除");
        iteratorRemoveDemo();

        // 3. Map迭代
        System.out.println("\n3. Map迭代");
        mapIteratorDemo();

        // 4. Set迭代
        System.out.println("\n4. Set迭代");
        setIteratorDemo();

        // 5. Enumeration（遗留API）
        System.out.println("\n5. Enumeration迭代");
        enumerationDemo();

        // 6. 模拟forEachRemaining
        System.out.println("\n6. forEachRemaining");
        forEachRemainingDemo();
    }

    /**
     * ListIterator双向迭代
     */
    private static void listIteratorDemo() {
        List<String> list = Arrays.asList("A", "B", "C", "D", "E");

        // 获取ListIterator
        ListIterator<String> iterator = list.listIterator();

        // 正向遍历
        System.out.println("正向遍历:");
        while (iterator.hasNext()) {
            int index = iterator.nextIndex();
            String value = iterator.next();
            System.out.println("  索引 " + index + ": " + value);
        }

        // 反向遍历
        System.out.println("反向遍历:");
        while (iterator.hasPrevious()) {
            int index = iterator.previousIndex();
            String value = iterator.previous();
            System.out.println("  索引 " + index + ": " + value);
        }

        // 可以在遍历时添加元素
        List<String> list2 = new ArrayList<>(Arrays.asList("1", "2", "3"));
        ListIterator<String> it2 = list2.listIterator();
        it2.next();
        it2.add("1.5");
        System.out.println("添加元素后: " + list2);
    }

    /**
     * 使用迭代器安全删除元素
     */
    private static void iteratorRemoveDemo() {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        System.out.println("原始列表: " + numbers);

        // 错误方式：会抛出ConcurrentModificationException
        // for (Integer num : numbers) {
        //     if (num % 2 == 0) {
        //         numbers.remove(num);
        //     }
        // }

        // 正确方式：使用迭代器删除
        Iterator<Integer> iterator = numbers.iterator();
        while (iterator.hasNext()) {
            Integer num = iterator.next();
            if (num % 2 == 0) {
                iterator.remove();
            }
        }

        System.out.println("删除偶数后: " + numbers);
    }

    /**
     * Map迭代
     */
    private static void mapIteratorDemo() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 10);
        map.put("Banana", 20);
        map.put("Orange", 30);

        // 方式1: 迭代key
        System.out.println("迭代key:");
        for (String key : map.keySet()) {
            System.out.println("  key: " + key);
        }

        // 方式2: 迭代value
        System.out.println("迭代value:");
        for (Integer value : map.values()) {
            System.out.println("  value: " + value);
        }

        // 方式3: 迭代entry
        System.out.println("迭代entry:");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }

        // 方式4: 使用Iterator
        System.out.println("使用Iterator:");
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }
    }

    /**
     * Set迭代
     */
    private static void setIteratorDemo() {
        Set<String> set = new HashSet<>(Arrays.asList("Red", "Green", "Blue"));

        // 使用Iterator
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            System.out.println("颜色: " + iterator.next());
        }

        // 使用forEach
        System.out.println("使用forEach:");
        set.forEach(color -> System.out.println("  颜色: " + color));
    }

    /**
     * Enumeration迭代（遗留API）
     */
    private static void enumerationDemo() {
        // Vector是遗留类，支持Enumeration
        Vector<String> vector = new Vector<>(Arrays.asList("One", "Two", "Three"));

        // 获取Enumeration
        Enumeration<String> enumeration = vector.elements();

        // 遍历
        while (enumeration.hasMoreElements()) {
            System.out.println("枚举: " + enumeration.nextElement());
        }
    }

    /**
     * forEachRemaining使用
     */
    private static void forEachRemainingDemo() {
        List<String> list = Arrays.asList("a", "b", "c", "d");

        Iterator<String> iterator = list.iterator();

        // 跳过前两个
        iterator.next();
        iterator.next();

        // 使用forEachRemaining处理剩余元素
        System.out.println("剩余元素:");
        iterator.forEachRemaining(item -> System.out.println("  " + item));
    }
}
