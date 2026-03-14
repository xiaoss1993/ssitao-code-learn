package com.ssitao.code.designpattern.iterator.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 迭代器模式基础示例
 *
 * 迭代器模式特点：
 * 1. 提供一种方法顺序访问集合对象元素，不暴露内部表示
 * 2. 将遍历行为从集合中分离出来
 * 3. 支持多次遍历
 *
 * JDK中的应用：
 * - java.util.Iterator
 * - java.util.ListIterator
 * - java.util.Enumeration
 */
public class BasicIteratorDemo {

    public static void main(String[] args) {
        System.out.println("=== 迭代器模式基础示例 ===\n");

        // 创建集合
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");

        // 1. 使用JDK迭代器
        System.out.println("1. 使用JDK Iterator");
        useIterator(names);

        // 2. 使用增强for循环（语法糖）
        System.out.println("\n2. 使用增强for循环");
        useForEach(names);

        // 3. 使用索引遍历
        System.out.println("\n3. 使用索引遍历");
        useIndex(names);

        // 4. 模拟自定义迭代器
        System.out.println("\n4. 自定义迭代器");
        useCustomIterator();
    }

    /**
     * 使用JDK Iterator
     */
    private static void useIterator(List<String> list) {
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            System.out.println("读取: " + item);
        }
    }

    /**
     * 使用增强for循环
     */
    private static void useForEach(List<String> list) {
        for (String item : list) {
            System.out.println("读取: " + item);
        }
    }

    /**
     * 使用索引遍历
     */
    private static void useIndex(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println("索引 " + i + ": " + list.get(i));
        }
    }

    /**
     * 自定义迭代器实现
     */
    private static void useCustomIterator() {
        // 创建自定义集合
        BookCollection collection = new BookCollection();
        collection.addBook("《设计模式》");
        collection.addBook("《重构》");
        collection.addBook("《代码整洁之道》");

        // 使用自定义迭代器
        BookIterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            System.out.println("书籍: " + iterator.next());
        }
    }
}

/**
 * 自定义书籍集合
 */
class BookCollection {
    private List<String> books = new ArrayList<>();

    public void addBook(String book) {
        books.add(book);
    }

    public int size() {
        return books.size();
    }

    public String get(int index) {
        return books.get(index);
    }

    // 返回迭代器
    public BookIterator iterator() {
        return new BookIterator(this);
    }
}

/**
 * 自定义书籍迭代器
 */
class BookIterator {
    private BookCollection collection;
    private int position = 0;

    public BookIterator(BookCollection collection) {
        this.collection = collection;
    }

    public boolean hasNext() {
        return position < collection.size();
    }

    public String next() {
        String book = collection.get(position);
        position++;
        return book;
    }
}
