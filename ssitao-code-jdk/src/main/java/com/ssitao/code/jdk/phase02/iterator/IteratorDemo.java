package com.ssitao.code.jdk.phase02.iterator;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Iterator 示例代码
 * 演示迭代器的使用和fail-fast机制
 */
public class IteratorDemo {

    public static void main(String[] args) {
        System.out.println("=== Iterator Demo ===\n");

        // 1. 基本Iterator使用
        System.out.println("--- 基本Iterator使用 ---");
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));
        Iterator<String> it = list.iterator();

        while (it.hasNext()) {
            String s = it.next();
            System.out.print(s + " ");
        }
        System.out.println();

        // 2. 安全删除
        System.out.println("\n--- 安全删除 ---");
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println("Before: " + numbers);
        Iterator<Integer> numIt = numbers.iterator();
        while (numIt.hasNext()) {
            Integer n = numIt.next();
            if (n % 2 == 0) {
                numIt.remove();  // 安全删除
            }
        }
        System.out.println("After removing evens: " + numbers);

        // 3. ListIterator - 双向遍历
        System.out.println("\n--- ListIterator 双向遍历 ---");
        List<String> list2 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        ListIterator<String> lit = list2.listIterator();

        System.out.print("Forward: ");
        while (lit.hasNext()) {
            System.out.print(lit.nextIndex() + ":" + lit.next() + " ");
        }
        System.out.println();

        System.out.print("Backward: ");
        while (lit.hasPrevious()) {
            System.out.print(lit.previousIndex() + ":" + lit.previous() + " ");
        }
        System.out.println();

        // 4. ListIterator - 添加和修改
        System.out.println("\n--- ListIterator 添加和修改 ---");
        List<String> list3 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        ListIterator<String> lit3 = list3.listIterator();
        while (lit3.hasNext()) {
            String s = lit3.next();
            if (s.equals("b")) {
                lit3.set("bb");  // 修改
            }
        }
        System.out.println("After set: " + list3);

        ListIterator<String> lit4 = list3.listIterator();
        while (lit4.hasNext()) {
            String s = lit4.next();
            if (s.equals("bb")) {
                lit4.add("new");  // 添加
            }
        }
        System.out.println("After add: " + list3);

        // 5. fail-fast演示
        System.out.println("\n--- fail-fast机制 ---");
        System.out.println("ConcurrentModificationException演示：");

        List<String> failFastList = new ArrayList<>(Arrays.asList("a", "b", "c"));

        try {
            for (String s : failFastList) {
                if (s.equals("b")) {
                    failFastList.remove(s);  // 会抛出ConcurrentModificationException
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("抛出ConcurrentModificationException!");
            System.out.println("原因：在Iterator遍历过程中直接修改了集合");
        }

        System.out.println("正确做法：使用Iterator删除");
        Iterator<String> safeIt = failFastList.iterator();
        while (safeIt.hasNext()) {
            if (safeIt.next().equals("b")) {
                safeIt.remove();  // 安全删除
            }
        }
        System.out.println("After safe remove: " + failFastList);

        // 6. fail-safe
        System.out.println("\n--- fail-safe ---");
        System.out.println("CopyOnWriteArrayList是fail-safe的：");
        CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>(Arrays.asList("a", "b", "c"));
        for (String s : cowList) {
            cowList.remove("b");  // 不会抛出异常
        }
        System.out.println("After remove during iteration: " + cowList);

        // 7. Map遍历
        System.out.println("\n--- Map遍历 ---");
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        // entrySet遍历（推荐）
        System.out.print("entrySet: ");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.print(entry.getKey() + "=" + entry.getValue() + " ");
        }
        System.out.println();

        // keySet遍历
        System.out.print("keySet: ");
        for (String key : map.keySet()) {
            System.out.print(key + ":" + map.get(key) + " ");
        }
        System.out.println();

        // 8. Enumeration（遗留）
        System.out.println("\n--- Enumeration (legacy) ---");
        Vector<String> vector = new Vector<>(Arrays.asList("x", "y", "z"));
        Enumeration<String> enumeration = vector.elements();
        System.out.print("Enumeration: ");
        while (enumeration.hasMoreElements()) {
            System.out.print(enumeration.nextElement() + " ");
        }
        System.out.println();

        // 9. forEachRemaining
        System.out.println("\n--- forEachRemaining (JDK 8+) ---");
        List<String> list5 = new ArrayList<>(Arrays.asList("1", "2", "3"));
        Iterator<String> it5 = list5.iterator();
        it5.next();
        it5.forEachRemaining(s -> System.out.print(s + " "));
        System.out.println();

        // 10. 遍历方式总结
        System.out.println("\n--- 遍历方式总结 ---");
        System.out.println("List: fori, for-each, Iterator, ListIterator, forEach, stream");
        System.out.println("Set: for-each, Iterator, forEach, stream");
        System.out.println("Map: keySet, entrySet, values, forEach");
    }
}
