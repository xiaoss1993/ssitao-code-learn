package com.learn.crazyjava.lesson05_collection;

import java.util.*;

/**
 * 第5课：集合框架 - List操作
 */
public class ListDemo {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add("banana");
        list.add("orange");
        list.add(1, "grape");

        System.out.println("第一个元素：" + list.get(0));
        System.out.println("大小：" + list.size());

        // 遍历
        System.out.println("=== 增强for遍历 ===");
        for (String s : list) {
            System.out.println(s);
        }

        // 使用迭代器
        System.out.println("=== 迭代器遍历 ===");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String s = it.next();
            System.out.println(s);
        }

        // JDK 8 forEach
        System.out.println("=== forEach遍历 ===");
        list.forEach(System.out::println);

        // 使用ListIterator逆序
        System.out.println("=== 逆序遍历 ===");
        ListIterator<String> lit = list.listIterator(list.size());
        while (lit.hasPrevious()) {
            System.out.println(lit.previous());
        }
    }
}
