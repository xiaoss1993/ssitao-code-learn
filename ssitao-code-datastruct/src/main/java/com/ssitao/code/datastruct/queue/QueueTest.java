package com.ssitao.code.datastruct.queue;

/**
 * 队列测试类 - 演示队列的基本操作
 */
public class QueueTest {

    public static void main(String[] args) {
        System.out.println("========== 队列学习 ==========\n");

        // 测试循环队列
        System.out.println("=== 循环队列 ===");
        CircleQueue<Integer> queue1 = new CircleQueue<>(3);
        testQueue(queue1);

        // 测试链表队列
        System.out.println("\n=== 链表队列 ===");
        LinkedListQueue<Integer> queue2 = new LinkedListQueue<>();
        testQueue(queue2);

        // 循环队列扩容演示
        System.out.println("\n========== 循环队列扩容演示 ==========\n");
        CircleQueue<String> queue3 = new CircleQueue<>(3);
        System.out.println("初始容量: " + queue3.getCapacity());

        queue3.enqueue("A");
        queue3.enqueue("B");
        queue3.enqueue("C");
        System.out.println("添加3个元素后: " + queue3);

        queue3.enqueue("D");  // 触发扩容
        System.out.println("添加D后: " + queue3);

        // 循环队列循环特性演示
        System.out.println("\n========== 循环特性演示 ==========\n");
        CircleQueue<Integer> queue4 = new CircleQueue<>(5);
        System.out.println("入队: 1, 2, 3, 4, 5");
        for (int i = 1; i <= 5; i++) {
            queue4.enqueue(i);
        }
        System.out.println(queue4);

        System.out.println("\n出队3次: " + queue4.dequeue() + ", " + queue4.dequeue() + ", " + queue4.dequeue());
        System.out.println(queue4);

        System.out.println("\n再入队: 6, 7");
        queue4.enqueue(6);
        queue4.enqueue(7);
        System.out.println(queue4);
        System.out.println("可以看到队列'循环'利用了前面的空间");

        // BFS广度优先搜索示例
        System.out.println("\n========== BFS应用示例 ==========\n");
        bfsExample();
    }

    // 测试队列的基本操作
    private static void testQueue(Queue<Integer> queue) {
        System.out.println("isEmpty: " + queue.isEmpty() + ", size: " + queue.size());

        // 入队
        System.out.println("\n--- 入队 enqueue ---");
        queue.enqueue(10);
        System.out.println("enqueue(10): " + queue);

        queue.enqueue(20);
        System.out.println("enqueue(20): " + queue);

        queue.enqueue(30);
        System.out.println("enqueue(30): " + queue);

        // 查看队首
        System.out.println("\n--- 查看队首 getFront ---");
        System.out.println("getFront(): " + queue.getFront());
        System.out.println("队列状态未变: " + queue);

        // 出队
        System.out.println("\n--- 出队 dequeue ---");
        System.out.println("dequeue(): " + queue.dequeue());
        System.out.println("dequeue()后: " + queue);

        System.out.println("dequeue(): " + queue.dequeue());
        System.out.println("dequeue()后: " + queue);

        System.out.println("dequeue(): " + queue.dequeue());
        System.out.println("dequeue()后: " + queue);

        System.out.println("\nisEmpty: " + queue.isEmpty() + ", size: " + queue.size());
    }

    // BFS广度优先搜索示例
    private static void bfsExample() {
        // 模拟图的广度优先搜索
        //    1
        //   /|\
        //  2 3 4
        //  | | |
        //  5 6 7

        // 邻接表表示
        int[][] graph = {
            {},              // 0号节点不使用
            {2, 3, 4},       // 1的邻居
            {5},             // 2的邻居
            {6},             // 3的邻居
            {7},             // 4的邻居
            {},              // 5没有邻居
            {},              // 6没有邻居
            {}               // 7没有邻居
        };

        System.out.println("BFS遍历顺序（从节点1开始）:");
        System.out.println("预期: 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7");

        Queue<Integer> queue = new LinkedListQueue<>();
        boolean[] visited = new boolean[8];  // 节点1-7

        int start = 1;
        queue.enqueue(start);
        visited[start] = true;

        System.out.print("实际: ");
        while (!queue.isEmpty()) {
            int node = queue.dequeue();
            System.out.print(node);

            for (int neighbor : graph[node]) {
                if (!visited[neighbor]) {
                    queue.enqueue(neighbor);
                    visited[neighbor] = true;
                }
            }

            if (!queue.isEmpty()) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
}