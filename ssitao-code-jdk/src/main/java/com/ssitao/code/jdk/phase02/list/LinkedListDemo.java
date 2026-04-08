package com.ssitao.code.jdk.phase02.list;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * LinkedList 示例代码
 * 演示LinkedList作为List、Deque、Stack的使用
 */
public class LinkedListDemo {

    public static void main(String[] args) {
        System.out.println("=== LinkedList Demo ===\n");

        LinkedList<String> list = new LinkedList<>();

        // 1. 作为List使用
        System.out.println("--- 作为List使用 ---");
        list.add("apple");
        list.add("banana");
        list.add("orange");
        System.out.println("List: " + list);
        System.out.println("Get first: " + list.getFirst());
        System.out.println("Get last: " + list.getLast());
        System.out.println("Get(1): " + list.get(1));

        // 2. 作为Deque使用（队首操作）
        System.out.println("\n--- Deque 队首操作 ---");
        list.addFirst("grape");     // 队首添加
        list.offerFirst("peach");   // 队首添加（推荐）
        System.out.println("After addFirst: " + list);
        System.out.println("pollFirst: " + list.pollFirst());  // 取出并删除
        System.out.println("After pollFirst: " + list);

        // 3. 作为Deque使用（队尾操作）
        System.out.println("\n--- Deque 队尾操作 ---");
        list.addLast("melon");     // 队尾添加
        list.offerLast("watermelon"); // 队尾添加（推荐）
        System.out.println("After addLast: " + list);
        System.out.println("pollLast: " + list.pollLast());
        System.out.println("After pollLast: " + list);

        // 4. 作为Stack使用（LIFO）
        System.out.println("\n--- 作为Stack使用 ---");
        LinkedList<Integer> stack = new LinkedList<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Stack push 1,2,3: " + stack);
        System.out.println("Stack peek: " + stack.peek());
        System.out.println("Stack pop: " + stack.pop());
        System.out.println("Stack pop: " + stack.pop());
        System.out.println("After pops: " + stack);

        // 5. 作为Queue使用（FIFO）
        System.out.println("\n--- 作为Queue使用 ---");
        LinkedList<String> queue = new LinkedList<>();
        queue.offer("first");
        queue.offer("second");
        queue.offer("third");
        System.out.println("Queue offer: " + queue);
        System.out.println("Queue peek: " + queue.peek());
        System.out.println("Queue poll: " + queue.poll());
        System.out.println("After poll: " + queue);

        // 6. 移除操作
        System.out.println("\n--- 移除操作 ---");
        LinkedList<String> list2 = new LinkedList<>(Arrays.asList("a", "b", "c", "d", "e"));
        System.out.println("Original: " + list2);
        System.out.println("removeFirst: " + list2.removeFirst());
        System.out.println("removeLast: " + list2.removeLast());
        System.out.println("After removals: " + list2);

        // 7. 获取但不移除
        System.out.println("\n--- 获取但不移除 ---");
        System.out.println("peekFirst: " + list2.peekFirst());
        System.out.println("peekLast: " + list2.peekLast());
        System.out.println("After peek: " + list2);

        // 8. LinkedList vs ArrayList 性能对比
        System.out.println("\n--- 性能对比 ---");
        int n = 100000;

        // ArrayList头部操作
        long start = System.nanoTime();
        java.util.ArrayList<Integer> al = new java.util.ArrayList<>();
        for (int i = 0; i < n; i++) {
            al.add(0, i);  // 头部插入，慢
        }
        long alAdd = System.nanoTime() - start;

        // LinkedList头部操作
        start = System.nanoTime();
        LinkedList<Integer> ll = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            ll.addFirst(i);  // 头部插入，快
        }
        long llAdd = System.nanoTime() - start;

        System.out.println("ArrayList add(0,i) time: " + alAdd / 1_000_000 + "ms");
        System.out.println("LinkedList addFirst time: " + llAdd / 1_000_000 + "ms");

        // 9. 遍历性能对比
        System.out.println("\n--- 遍历性能 ---");

        // ArrayList随机访问
        start = System.nanoTime();
        int sum = 0;
        for (int i = 0; i < al.size(); i++) {
            sum += al.get(i);  // O(1)，快
        }
        long alGet = System.nanoTime() - start;

        // LinkedList随机访问
        start = System.nanoTime();
        sum = 0;
        for (int i = 0; i < ll.size(); i++) {
            sum += ll.get(i);  // O(n)，慢
        }
        long llGet = System.nanoTime() - start;

        // LinkedList迭代器访问
        start = System.nanoTime();
        sum = 0;
        for (Integer i : ll) {
            sum += i;  // O(n)，快
        }
        long llIter = System.nanoTime() - start;

        System.out.println("ArrayList get(i) time: " + alGet / 1_000_000 + "ms");
        System.out.println("LinkedList get(i) time: " + llGet / 1_000_000 + "ms (slow!)");
        System.out.println("LinkedList iterator time: " + llIter / 1_000_000 + "ms");

        // 10. 使用场景总结
        System.out.println("\n--- 使用场景总结 ---");
        System.out.println("LinkedList适合：频繁增删头尾操作");
        System.out.println("ArrayList适合：随机访问、尾部操作为主");
        System.out.println("一般情况默认使用ArrayList");
    }
}
