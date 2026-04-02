package com.ssitao.code.datastruct.array;

/**
 * 数组测试类 - 演示数组的基本操作
 */
public class ArrayTest {

    public static void main(String[] args) {
        System.out.println("========== 数组学习 ==========\n");

        // 创建数组
        Array<Integer> arr = new Array<>(5);
        System.out.println("创建容量为5的空数组: " + arr);

        // 添加元素
        System.out.println("\n--- 添加元素 ---");
        arr.addLast(10);
        System.out.println("添加10: " + arr);

        arr.addLast(20);
        System.out.println("添加20: " + arr);

        arr.addLast(30);
        System.out.println("添加30: " + arr);

        arr.addFirst(5);
        System.out.println("头部添加5: " + arr);

        arr.add(2, 15);  // 在索引2位置插入15
        System.out.println("在索引2插入15: " + arr);

        // 访问元素
        System.out.println("\n--- 访问元素 ---");
        System.out.println("get(0) = " + arr.get(0));
        System.out.println("get(3) = " + arr.get(3));

        // 修改元素
        System.out.println("\n--- 修改元素 ---");
        arr.set(1, 25);
        System.out.println("设置索引1为25: " + arr);

        // 查找
        System.out.println("\n--- 查找元素 ---");
        System.out.println("是否包含20: " + arr.contains(20));
        System.out.println("20的索引: " + arr.find(20));
        System.out.println("100的索引: " + arr.find(100));

        // 删除
        System.out.println("\n--- 删除元素 ---");
        System.out.println("删除元素: " + arr.remove(2));
        System.out.println("删除后: " + arr);

        System.out.println("删除第一个: " + arr.removeFirst());
        System.out.println("删除后: " + arr);

        System.out.println("删除最后一个: " + arr.removeLast());
        System.out.println("删除后: " + arr);

        // 扩容演示
        System.out.println("\n--- 扩容演示 ---");
        Array<String> arr2 = new Array<>(2);
        arr2.addLast("A");
        arr2.addLast("B");
        arr2.addLast("C");  // 触发扩容
        arr2.addLast("D");
        System.out.println(arr2);

        // 缩容演示
        System.out.println("\n--- 缩容演示 ---");
        arr2.removeFirst();
        arr2.removeFirst();  // 触发缩容
        System.out.println(arr2);

        System.out.println("\n========== 数组复杂度总结 ==========");
        System.out.println("访问: O(1)    - 数组支持随机访问");
        System.out.println("插入: O(n)    - 需要移动元素");
        System.out.println("删除: O(n)    - 需要移动元素");
        System.out.println("搜索: O(n)    - 需要遍历");
    }
}