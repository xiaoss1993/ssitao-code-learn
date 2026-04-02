package com.ssitao.code.datastruct.tree;

import com.ssitao.code.datastruct.queue.ArrayQueue;
import com.ssitao.code.datastruct.queue.Queue;

/**
 * 二叉搜索树测试类 - 演示BST的基本操作
 */
public class BSTTest {

    public static void main(String[] args) {
        System.out.println("========== 二叉搜索树学习 ==========\n");

        // 创建BST，使用整数（已实现Comparable）
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        // 添加元素
        System.out.println("--- 添加元素 ---");
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) {
            bst.add(v);
            System.out.println("add(" + v + "), size = " + bst.size());
        }

        // 树结构
        System.out.println("\n--- 树结构 ---");
        System.out.println(bst);

        // 遍历
        System.out.println("\n--- 遍历 ---");
        System.out.print("前序遍历 (根-左-右): ");
        bst.preOrder();
        System.out.println();

        System.out.print("中序遍历 (左-根-右): ");
        bst.inOrder();
        System.out.println();

        System.out.print("后序遍历 (左-右-根): ");
        bst.postOrder();
        System.out.println();

        System.out.print("层序遍历 (广度优先): ");
        bst.levelOrder();
        System.out.println();

        // 查找
        System.out.println("\n--- 查找 ---");
        System.out.println("contains(40): " + bst.contains(40));
        System.out.println("contains(45): " + bst.contains(45));

        // 删除
        System.out.println("\n--- 删除 ---");
        System.out.println("删除20 (叶子节点): ");
        bst.remove(20);
        System.out.println(bst);

        System.out.println("\n删除30 (有一个子节点): ");
        bst.remove(30);
        System.out.println(bst);

        System.out.println("\n删除50 (根节点，有两个子节点): ");
        bst.remove(50);
        System.out.println(bst);

        // 使用自定义比较器
        System.out.println("\n========== 自定义比较器示例 ==========\n");
        BinarySearchTree<String> bst2 = new BinarySearchTree<>(new BinarySearchTree.Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                // 按字符串长度排序
                return a.length() - b.length();
            }
        });

        String[] words = {"cat", "hi", "hello", "a", "ok"};
        for (String w : words) {
            bst2.add(w);
        }

        System.out.println("按字符串长度排序的BST:");
        System.out.print("中序遍历: ");
        bst2.inOrder();
        System.out.println();

        // BST性质验证
        System.out.println("\n========== BST性质验证 ==========\n");
        System.out.println("isBST(): " + bst.contains(20));  // 删除后不在了

        // 复杂度分析
        System.out.println("\n========== 复杂度分析 ==========\n");
        printComplexity();
    }

    private static void printComplexity() {
        System.out.println("二叉搜索树操作复杂度:");
        System.out.println(repeat("-", 40));
        System.out.println("| 操作    | 平均   | 最坏   |");
        System.out.println(repeat("-", 40));
        System.out.println("| 查找    | O(log n) | O(n)   |");
        System.out.println("| 插入    | O(log n) | O(n)   |");
        System.out.println("| 删除    | O(log n) | O(n)   |");
        System.out.println(repeat("-", 40));

        System.out.println("\n最坏情况 O(n):");
        System.out.println("- 数据有序插入时退化为链表");
        System.out.println("- 解决：使用平衡二叉树（AVL、红黑树）");

        System.out.println("\nBST vs 其他数据结构:");
        System.out.println("- 数组：查找 O(log n) 二分，但插入/删除 O(n)");
        System.out.println("- 链表：插入/删除 O(1)，但查找 O(n)");
        System.out.println("- BST：综合两者优点，但最坏情况仍是 O(n)");
    }

    // 字符串重复工具方法（兼容Java 8）
    private static String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}