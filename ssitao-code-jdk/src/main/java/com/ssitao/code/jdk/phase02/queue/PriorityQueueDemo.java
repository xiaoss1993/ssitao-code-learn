package com.ssitao.code.jdk.phase02.queue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * PriorityQueue 示例代码
 * 演示优先级队列的特性
 */
public class PriorityQueueDemo {

    public static void main(String[] args) {
        System.out.println("=== PriorityQueue Demo ===\n");

        // 1. 基本操作（最小堆）
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.add(5);
        minHeap.add(2);
        minHeap.add(8);
        minHeap.add(1);
        minHeap.add(9);
        System.out.println("MinHeap elements (heap order): " + minHeap);
        System.out.println("peek (min): " + minHeap.peek());

        System.out.print("poll order: ");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.poll() + " ");
        }
        System.out.println();

        // 2. 最大堆（降序）
        System.out.println("\n--- 最大堆 (reverseOrder) ---");
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.addAll(Arrays.asList(5, 2, 8, 1, 9));
        System.out.print("MaxHeap poll order: ");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.poll() + " ");
        }
        System.out.println();

        // 3. 自定义比较器 - 按字符串长度
        System.out.println("\n--- 按字符串长度排序 ---");
        PriorityQueue<String> lengthPQ = new PriorityQueue<>(Comparator.comparingInt(String::length));
        lengthPQ.addAll(Arrays.asList("apple", "pie", "banana", "hi", "orange"));
        System.out.print("Poll by length: ");
        while (!lengthPQ.isEmpty()) {
            System.out.print(lengthPQ.poll() + " ");
        }
        System.out.println();

        // 4. 自定义对象
        System.out.println("\n--- 自定义对象 ---");
        PriorityQueue<Task> taskPQ = new PriorityQueue<>(Comparator.comparingInt(t -> t.priority));
        taskPQ.add(new Task("写代码", 2));
        taskPQ.add(new Task("开会", 1));
        taskPQ.add(new Task("吃饭", 3));
        System.out.println("Tasks by priority:");
        while (!taskPQ.isEmpty()) {
            Task t = taskPQ.poll();
            System.out.println("  Priority " + t.priority + ": " + t.name);
        }

        // 5. Top K问题
        System.out.println("\n--- Top K问题 ---");
        int[] arr = {4, 5, 1, 6, 2, 7, 3, 8};
        int k = 3;
        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("Top " + k + " largest:");
        PriorityQueue<Integer> topK = new PriorityQueue<>(k);  // 小顶堆
        for (int n : arr) {
            if (topK.size() < k) {
                topK.offer(n);
            } else if (n > topK.peek()) {
                topK.poll();
                topK.offer(n);
            }
        }
        System.out.println("Result: " + topK);  // 应该是最大的3个数

        // 6. 合并有序数组
        System.out.println("\n--- 合并有序数组 ---");
        int[] a1 = {1, 3, 5, 7};
        int[] a2 = {2, 4, 6, 8};
        System.out.println("Array1: " + Arrays.toString(a1));
        System.out.println("Array2: " + Arrays.toString(a2));

        PriorityQueue<Integer> merged = new PriorityQueue<>();
        for (int n : a1) merged.offer(n);
        for (int n : a2) merged.offer(n);

        System.out.print("Merged: ");
        while (!merged.isEmpty()) {
            System.out.print(merged.poll() + " ");
        }
        System.out.println();

        // 7. 性能特点
        System.out.println("\n--- 性能特点 ---");
        System.out.println("底层实现：二叉堆（数组存储）");
        System.out.println("入队时间：O(log n)");
        System.out.println("出队时间：O(log n)");
        System.out.println("查看时间：O(1)");
        System.out.println("不是FIFO，是按优先级");

        // 8. 使用场景
        System.out.println("\n--- 使用场景 ---");
        System.out.println("1. 任务调度（按优先级）");
        System.out.println("2. Top K问题");
        System.out.println("3. 合并有序流");
        System.out.println("4. 中位数问题（两个堆）");
    }

    static class Task {
        String name;
        int priority;

        Task(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }
    }
}
