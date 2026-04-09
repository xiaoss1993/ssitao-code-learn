package com.concurrency.chapter6.task;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * PriorityBlockingQueue优先级队列示例
 */
public class PriorityBlockingQueueDemo {

    public static void demo() throws InterruptedException {
        PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();

        // 添加任务（优先级高的先出队）
        queue.put(new Task("任务C", 3));
        queue.put(new Task("任务A", 1));
        queue.put(new Task("任务B", 2));
        queue.put(new Task("任务D", 5));
        queue.put(new Task("任务E", 4));

        System.out.println("按优先级顺序取出所有任务：");
        while (!queue.isEmpty()) {
            Task task = queue.take();
            System.out.println("取出: " + task.name + " (优先级: " + task.priority + ")");
        }
    }

    static class Task implements Comparable<Task> {
        String name;
        int priority;

        Task(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }

        @Override
        public int compareTo(Task o) {
            return this.priority - o.priority; // 优先级低的先出队
        }
    }
}
