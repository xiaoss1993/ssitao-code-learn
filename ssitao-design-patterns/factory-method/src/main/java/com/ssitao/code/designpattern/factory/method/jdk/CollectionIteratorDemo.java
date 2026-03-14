package com.ssitao.code.designpattern.factory.method.jdk;

import java.util.*;

/**
 * JDK 工厂方法模式示例 - Collection.iterator()
 *
 * Collection 接口定义了 iterator() 工厂方法，
 * 返回 Iterator 接口的具体实现（ArrayListIterator, LinkedListIterator等）
 *
 * 工厂方法模式体现：
 * - 产品接口：Iterator
 * - 具体产品：ArrayListIterator, LinkedListIterator 等
 * - 工厂接口：Collection
 * - 具体工厂：ArrayList, LinkedList 等
 */
public class CollectionIteratorDemo {

    public static void main(String[] args) {
        // ArrayList 工厂方法 - 返回 ArrayListIterator
        List<String> arrayList = new ArrayList<>();
        arrayList.add("Apple");
        arrayList.add("Banana");
        arrayList.add("Orange");

        Iterator<String> arrayListIterator = arrayList.iterator();
        System.out.println("ArrayList Iterator: " + arrayListIterator.getClass().getSimpleName());
        printIterator(arrayListIterator);

        // LinkedList 工厂方法 - 返回 LinkedListIterator
        List<String> linkedList = new LinkedList<>();
        linkedList.add("Dog");
        linkedList.add("Cat");
        linkedList.add("Bird");

        Iterator<String> linkedListIterator = linkedList.iterator();
        System.out.println("\nLinkedList Iterator: " + linkedListIterator.getClass().getSimpleName());
        printIterator(linkedListIterator);

        // HashSet 工厂方法 - 返回 HashMap.KeyIterator
        Set<String> hashSet = new HashSet<>();
        hashSet.add("Red");
        hashSet.add("Green");
        hashSet.add("Blue");

        Iterator<String> hashSetIterator = hashSet.iterator();
        System.out.println("\nHashSet Iterator: " + hashSetIterator.getClass().getSimpleName());
        printIterator(hashSetIterator);
    }

    private static void printIterator(Iterator<String> iterator) {
        while (iterator.hasNext()) {
            System.out.println("  Element: " + iterator.next());
        }
    }
}
