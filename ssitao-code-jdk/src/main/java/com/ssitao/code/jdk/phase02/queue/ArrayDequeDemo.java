package com.ssitao.code.jdk.phase02.queue;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ArrayDeque 示例代码
 * 演示ArrayDeque作为栈、队列、双端队列的使用
 */
public class ArrayDequeDemo {

    public static void main(String[] args) {
        System.out.println("=== ArrayDeque Demo ===\n");

        // 1. 作为Stack使用（LIFO）
        System.out.println("--- 作为Stack使用 ---");
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Stack after pushes: " + stack);
        System.out.println("peek: " + stack.peek());
        System.out.println("pop: " + stack.pop());
        System.out.println("pop: " + stack.pop());
        System.out.println("Stack after pops: " + stack);

        // 2. 作为Queue使用（FIFO）
        System.out.println("\n--- 作为Queue使用 ---");
        ArrayDeque<String> queue = new ArrayDeque<>();
        queue.offer("first");
        queue.offer("second");
        queue.offer("third");
        System.out.println("Queue after offers: " + queue);
        System.out.println("peek: " + queue.peek());
        System.out.println("poll: " + queue.poll());
        System.out.println("Queue after poll: " + queue);

        // 3. 作为Deque使用
        System.out.println("\n--- 作为Deque使用 ---");
        ArrayDeque<String> deque = new ArrayDeque<>();

        // 队首操作
        deque.addFirst("a");
        deque.offerFirst("b");
        System.out.println("After addFirst a, offerFirst b: " + deque);
        System.out.println("getFirst: " + deque.getFirst());
        System.out.println("peekFirst: " + deque.peekFirst());
        System.out.println("pollFirst: " + deque.pollFirst());
        System.out.println("After pollFirst: " + deque);

        // 队尾操作
        deque.addLast("x");
        deque.offerLast("y");
        System.out.println("After addLast x, offerLast y: " + deque);
        System.out.println("getLast: " + deque.getLast());
        System.out.println("peekLast: " + deque.peekLast());
        System.out.println("pollLast: " + deque.pollLast());
        System.out.println("After pollLast: " + deque);

        // 4. ArrayDeque vs LinkedList
        System.out.println("\n--- ArrayDeque vs LinkedList ---");
        int n = 100000;

        long start = System.nanoTime();
        ArrayDeque<Integer> adq = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            adq.addLast(i);
        }
        long adqAddTime = System.nanoTime() - start;

        start = System.nanoTime();
        java.util.LinkedList<Integer> ll = new java.util.LinkedList<>();
        for (int i = 0; i < n; i++) {
            ll.addLast(i);
        }
        long llAddTime = System.nanoTime() - start;

        System.out.println("ArrayDeque addLast: " + adqAddTime / 1_000_000 + "ms");
        System.out.println("LinkedList addLast: " + llAddTime / 1_000_000 + "ms");
        System.out.println("ArrayDeque usually faster due to better cache locality");

        // 5. ArrayDeque vs Stack
        System.out.println("\n--- ArrayDeque vs Stack ---");
        System.out.println("ArrayDeque is faster than Stack (Vector-based)");
        System.out.println("Stack methods: push, pop, peek");
        System.out.println("ArrayDeque methods: push/pop/peek (same), addFirst/offerFirst");

        // 6. 容量
        System.out.println("\n--- 容量特点 ---");
        ArrayDeque<Integer> capacityDeque = new ArrayDeque<>(10);
        System.out.println("Initial capacity is at least specified size");
        System.out.println("容量会自动扩容（翻倍）");
        System.out.println("使用循环数组实现，head和tail指针");

        // 7. 不可用null
        System.out.println("\n--- null支持 ---");
        try {
            ArrayDeque<String> nullDeque = new ArrayDeque<>();
            nullDeque.add(null);
            System.out.println("ArrayDeque不允许null");
        } catch (Exception e) {
            System.out.println("ArrayDeque不允许null元素（JDK 7+）");
        }

        // 8. 使用场景
        System.out.println("\n--- 使用场景 ---");
        System.out.println("1. 栈实现（替代Stack）：性能更好");
        System.out.println("2. 队列实现：推荐ArrayDeque");
        System.out.println("3. 双端队列：需要两端操作");
        System.out.println("4. 一般情况下作为Queue/Deque的首选");
    }
}
