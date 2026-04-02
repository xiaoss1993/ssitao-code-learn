package com.ssitao.code.datastruct.linkedlist;

/**
 * 双链表测试类 - 演示双链表的基本操作
 */
public class DoubleLinkedListTest {

    public static void main(String[] args) {
        System.out.println("========== 双链表学习 ==========\n");

        DoubleLinkedList<Integer> list = new DoubleLinkedList<>();

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
        System.out.println("get(4) = " + list.get(4));  // 尾部元素

        // 修改元素
        System.out.println("\n--- 修改元素 ---");
        list.set(1, 25);
        System.out.println("设置索引1为25: " + list);

        // 查找
        System.out.println("\n--- 查找元素 ---");
        System.out.println("是否包含20: " + list.contains(20));
        System.out.println("20的索引: " + list.find(20));

        // 删除
        System.out.println("\n--- 删除元素 ---");
        System.out.println("删除索引2: " + list.remove(2));
        System.out.println("删除后: " + list);

        System.out.println("删除第一个: " + list.removeFirst());
        System.out.println("删除后: " + list);

        System.out.println("删除最后一个: " + list.removeLast());
        System.out.println("删除后: " + list);

        System.out.println("\n========== 双链表 vs 单链表 ==========");
        System.out.println("双链表有prev指针，可以双向遍历");
        System.out.println("删除操作：双链表 O(1)，单链表 O(n)");
        System.out.println("（因为单链表删除需要先找到前驱节点）");
        System.out.println("\n双链表适合需要反向遍历的场景，如：");
        System.out.println("- 浏览器前进/后退功能");
        System.out.println("- 文本编辑器撤销/重做功能");
    }
}