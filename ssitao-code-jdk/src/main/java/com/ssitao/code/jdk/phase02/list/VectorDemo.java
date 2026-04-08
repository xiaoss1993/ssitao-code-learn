package com.ssitao.code.jdk.phase02.list;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Vector 示例代码
 * 演示Vector的常用操作和线程安全特性
 */
public class VectorDemo {

    public static void main(String[] args) {
        System.out.println("=== Vector Demo ===\n");

        // 1. 基本操作
        Vector<String> vector = new Vector<>();
        vector.add("apple");
        vector.add("banana");
        vector.add("orange");
        System.out.println("Vector: " + vector);
        System.out.println("Size: " + vector.size());
        System.out.println("Capacity: " + vector.capacity());  // 默认容量10

        // 2. 指定容量
        Vector<String> vector2 = new Vector<>(20);
        System.out.println("Vector2 capacity: " + vector2.capacity());

        // 3. 线程安全演示
        System.out.println("\n--- 线程安全 ---");
        System.out.println("Vector methods are synchronized (thread-safe but slow)");
        System.out.println("Better alternatives:");
        System.out.println("1. ArrayList + Collections.synchronizedList()");
        System.out.println("2. CopyOnWriteArrayList (read-heavy scenarios)");

        // 4. synchronizedList vs Vector
        System.out.println("\n--- 性能对比 ---");
        int n = 100000;

        long start = System.nanoTime();
        Vector<Integer> v = new Vector<>();
        for (int i = 0; i < n; i++) {
            v.add(i);
        }
        long vectorTime = System.nanoTime() - start;

        start = System.nanoTime();
        List<Integer> syncList = Collections.synchronizedList(new java.util.ArrayList<>());
        for (int i = 0; i < n; i++) {
            syncList.add(i);
        }
        long syncListTime = System.nanoTime() - start;

        System.out.println("Vector add time: " + vectorTime / 1_000_000 + "ms");
        System.out.println("synchronizedList add time: " + syncListTime / 1_000_000 + "ms");

        // 5. Stack继承
        System.out.println("\n--- Stack操作 ---");
        java.util.Stack<Integer> stack = new java.util.Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Stack: " + stack);
        System.out.println("peek: " + stack.peek());
        System.out.println("pop: " + stack.pop());
        System.out.println("pop: " + stack.pop());
        System.out.println("empty: " + stack.empty());

        // 6. 推荐：使用ArrayDeque代替Stack
        System.out.println("\n--- 推荐：ArrayDeque代替Stack ---");
        java.util.ArrayDeque<Integer> deque = new java.util.ArrayDeque<>();
        deque.push(1);
        deque.push(2);
        deque.push(3);
        System.out.println("ArrayDeque as stack: " + deque);
        System.out.println("pop: " + deque.pop());
        System.out.println("After pop: " + deque);

        // 7. 容量管理
        System.out.println("\n--- 容量管理 ---");
        Vector<String> v3 = new Vector<>(5, 10);  // 初始5，增量10
        System.out.println("Initial capacity: " + v3.capacity());
        for (int i = 0; i < 10; i++) {
            v3.add("item" + i);
        }
        System.out.println("After adding 10 items: " + v3.capacity());

        // 8. 遍历
        System.out.println("\n--- 遍历 ---");
        Vector<String> v4 = new Vector<>(Arrays.asList("a", "b", "c", "d", "e"));
        System.out.print("For-each: ");
        for (String s : v4) {
            System.out.print(s + " ");
        }
        System.out.println();

        System.out.print("Iterator: ");
        java.util.Iterator<String> it = v4.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        System.out.print("forEach: ");
        v4.forEach(s -> System.out.print(s + " "));
        System.out.println();
    }
}
