package com.ssitao.code.designpattern.producer.consumer.blocking;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞队列详解
 *
 * BlockingQueue核心方法：
 * - put(e): 放入元素，队列满则阻塞
 * - take(): 取走元素，队列空则阻塞
 * - offer(e): 放入元素，队列满返回false
 * - poll(): 取走元素，队列空返回null
 * - offer(e, timeout): 限时放入
 * - poll(timeout): 限时取走
 */
public class BlockingQueueDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== 阻塞队列示例 ===\n");

        // 1. ArrayBlockingQueue - 有界队列
        System.out.println("1. ArrayBlockingQueue");
        arrayBlockingQueueDemo();

        // 2. LinkedBlockingQueue - 无界队列
        System.out.println("\n2. LinkedBlockingQueue");
        linkedBlockingQueueDemo();

        // 3. SynchronousQueue - 同步队列
        System.out.println("\n3. SynchronousQueue");
        synchronousQueueDemo();

        // 4. PriorityBlockingQueue - 优先级队列
        System.out.println("\n4. PriorityBlockingQueue");
        priorityBlockingQueueDemo();
    }

    /**
     * ArrayBlockingQueue - 有界队列
     */
    private static void arrayBlockingQueueDemo() throws Exception {
        // 创建容量为3的阻塞队列
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);

        // 添加元素
        queue.put("元素1");
        System.out.println("添加元素1，当前大小: " + queue.size());

        queue.offer("元素2");
        System.out.println("添加元素2，当前大小: " + queue.size());

        // 尝试添加超过容量的元素（会失败）
        boolean added = queue.offer("元素3");
        System.out.println("添加元素3: " + added + "，当前大小: " + queue.size());

        // 队列满后再添加会返回false
        added = queue.offer("元素4");
        System.out.println("添加元素4: " + added + "，当前大小: " + queue.size());

        // 获取元素
        String element = queue.poll();
        System.out.println("取出元素: " + element + "，当前大小: " + queue.size());

        // 阻塞获取
        System.out.println("准备阻塞获取...");
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                queue.offer("新元素");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        String blockElement = queue.take();
        System.out.println("阻塞获取: " + blockElement + "，当前大小: " + queue.size());
    }

    /**
     * LinkedBlockingQueue - 无界队列
     */
    private static void linkedBlockingQueueDemo() throws Exception {
        // 默认无界
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        // 添加元素
        for (int i = 1; i <= 5; i++) {
            queue.put("元素" + i);
        }
        System.out.println("添加5个元素，当前大小: " + queue.size());

        // 一次性取完
        java.util.List<String> all = new java.util.ArrayList<>();
        queue.drainTo(all);
        System.out.println("drainTo取出: " + all.size() + "个元素");
    }

    /**
     * SynchronousQueue - 同步队列
     * 每个put必须等待一个take
     */
    private static void synchronousQueueDemo() throws Exception {
        BlockingQueue<String> queue = new SynchronousQueue<>();

        // 生产者线程
        Thread producer = new Thread(() -> {
            try {
                System.out.println("生产者准备放入...");
                queue.put("数据");
                System.out.println("生产者放入成功");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Producer");

        // 消费者线程
        Thread consumer = new Thread(() -> {
            try {
                System.out.println("消费者准备取出...");
                String data = queue.take();
                System.out.println("消费者取出: " + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Consumer");

        // 启动顺序很重要
        producer.start();
        Thread.sleep(100); // 确保生产者先运行
        consumer.start();
    }

    /**
     * PriorityBlockingQueue - 优先级队列
     */
    private static void priorityBlockingQueueDemo() throws Exception {
        // 使用自定义比较器
        BlockingQueue<Task> queue = new PriorityBlockingQueue<>(10, (t1, t2) -> {
            // 优先级高的先执行
            return Integer.compare(t2.getPriority(), t1.getPriority());
        });

        // 添加任务
        queue.put(new Task("任务C", 3));
        queue.put(new Task("任务A", 1));
        queue.put(new Task("任务B", 2));

        System.out.println("按优先级取出:");
        while (!queue.isEmpty()) {
            Task task = queue.take();
            System.out.println("处理: " + task.getName() + " (优先级: " + task.getPriority() + ")");
        }
    }
}

/**
 * 任务
 */
class Task {
    private String name;
    private int priority;

    public Task(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public String getName() { return name; }
    public int getPriority() { return priority; }
}
