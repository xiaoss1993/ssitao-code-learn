package com.ssitao.code.datastruct.linkedlist;

/**
 * 单链表测试类 - 演示链表的基本操作
 */
public class LinkedListTest {

    public static void main(String[] args) {
        System.out.println("========== 单链表学习 ==========\n");

        LinkedList<Integer> list = new LinkedList<>();

        // 添加元素
        System.out.println("--- 添加元素 ---");
        list.addLast(10);
        System.out.println("尾部添加10: " + list);

        list.addLast(20);
        System.out.println("尾部添加20: " + list);

        list.addLast(30);
        System.out.println("尾部添加30: " + list);

        list.addFirst(5);
        System.out.println("头部添加5: " + list);

        list.add(2, 15);
        System.out.println("在索引2插入15: " + list);

        // 访问元素
        System.out.println("\n--- 访问元素 ---");
        System.out.println("get(0) = " + list.get(0));
        System.out.println("get(3) = " + list.get(3));

        // 修改元素
        System.out.println("\n--- 修改元素 ---");
        list.set(1, 25);
        System.out.println("设置索引1为25: " + list);

        // 查找
        System.out.println("\n--- 查找元素 ---");
        System.out.println("是否包含20: " + list.contains(20));
        System.out.println("20的索引: " + list.find(20));
        System.out.println("100的索引: " + list.find(100));

        // 删除
        System.out.println("\n--- 删除元素 ---");
        System.out.println("删除索引2: " + list.remove(2));
        System.out.println("删除后: " + list);

        System.out.println("删除第一个: " + list.removeFirst());
        System.out.println("删除后: " + list);

        System.out.println("删除最后一个: " + list.removeLast());
        System.out.println("删除后: " + list);

        // 删除指定元素
        System.out.println("\n--- 删除指定元素 ---");
        list.addLast(40);
        list.addLast(50);
        System.out.println("添加40, 50: " + list);

        list.removeElement(25);
        System.out.println("删除25: " + list);

        System.out.println("\n========== 链表 vs 数组 对比 ==========");
        System.out.println("            |  数组   |  链表");
        System.out.println("------------|---------|--------");
        System.out.println("访问        |  O(1)   |  O(n)");
        System.out.println("头部插入    |  O(n)   |  O(1)");
        System.out.println("尾部插入    |  O(1)*  |  O(n)**");
        System.out.println("中间插入    |  O(n)   |  O(n)");
        System.out.println("删除        |  O(n)   |  O(n)***");
        System.out.println("------------|---------|--------");
        System.out.println("* 数组头部插入需要移动所有元素");
        System.out.println("** 链表尾部插入需要遍历，除非维护尾指针");
        System.out.println("*** 链表删除需要先找到前一个节点");
    }
}