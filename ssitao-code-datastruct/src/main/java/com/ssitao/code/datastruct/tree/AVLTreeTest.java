package com.ssitao.code.datastruct.tree;

/**
 * AVL树和红黑树测试类
 */
public class AVLTreeTest {

    public static void main(String[] args) {
        System.out.println("========== AVL树 vs 红黑树 学习 ==========\n");

        // AVL树测试
        System.out.println("=== AVL树测试 ===\n");
        testAVL();

        // 红黑树测试
        System.out.println("\n========== 红黑树测试 ==========\n");
        testRedBlack();

        // 性能对比
        System.out.println("\n========== AVL vs 红黑 对比 ==========\n");
        compareTrees();
    }

    // 测试AVL树
    private static void testAVL() {
        AVLTree<Integer> avl = new AVLTree<>();

        // 有序插入会导致BST退化为链表，但AVL会保持平衡
        int[] values = {10, 20, 30, 40, 50, 60, 70, 80, 90};
        System.out.println("依次添加（有序）: " + toString(values));
        for (int v : values) {
            avl.add(v);
        }

        System.out.println("\n树结构:");
        System.out.println(avl);

        System.out.println("树高度: " + avl.height());
        System.out.println("是否为BST: " + avl.isBST());
        System.out.println("是否平衡: " + avl.isBalanced());

        System.out.println("\n中序遍历（有序）: ");
        avl.inOrder();
        System.out.println();

        // 删除测试
        System.out.println("\n删除40后:");
        avl.remove(40);
        System.out.println("树高度: " + avl.height());
        System.out.println("是否平衡: " + avl.isBalanced());
        System.out.println("中序遍历: ");
        avl.inOrder();
        System.out.println();
    }

    // 测试红黑树
    private static void testRedBlack() {
        RedBlackTree<Integer> rbt = new RedBlackTree<>();

        int[] values = {10, 20, 30, 40, 50, 60, 70, 80, 90};
        System.out.println("依次添加（有序）: " + toString(values));
        for (int v : values) {
            rbt.add(v);
        }

        System.out.println("\n树结构 (R=红, B=黑):");
        System.out.println(rbt);

        System.out.println("树高度: " + rbt.height());
        System.out.println("中序遍历（有序）: ");
        rbt.inOrder();
        System.out.println();

        // 查找测试
        System.out.println("\n查找测试:");
        System.out.println("contains(50): " + rbt.contains(50));
        System.out.println("contains(55): " + rbt.contains(55));

        // 删除测试
        System.out.println("\n删除50后:");
        rbt.remove(50);
        System.out.println("树结构:");
        System.out.println(rbt);
        System.out.println("中序遍历: ");
        rbt.inOrder();
        System.out.println();
    }

    // 对比AVL和红黑树
    private static void compareTrees() {
        System.out.println(repeat("-", 50));
        System.out.println("| 特性       | AVL树          | 红黑树           |");
        System.out.println(repeat("-", 50));
        System.out.println("| 平衡标准   | 高度差<=1      | 黑高相同         |");
        System.out.println("| 插入/删除  | 可能多次旋转   | 最多2次旋转      |");
        System.out.println("| 查找性能   | 更高（更平衡） | 稍差（允许1倍差） |");
        System.out.println("| 适用场景   | 查找多          | 插入/删除多      |");
        System.out.println("| 高度范围   | <= 1.44*log n  | <= 2*log n       |");
        System.out.println(repeat("-", 50));

        System.out.println("\n【AVL树适用场景】");
        System.out.println("- 查找操作非常频繁");
        System.out.println("- 数据相对静态（插入删除少）");
        System.out.println("- 如：数据库索引、文件系统");

        System.out.println("\n【红黑树适用场景】");
        System.out.println("- 插入删除操作频繁");
        System.out.println("- 需要综合性能");
        System.out.println("- 如：JDK的TreeMap、ConcurrentHashMap");

        System.out.println("\n【为什么JDK用红黑树而不是AVL？】");
        System.out.println("1. 红黑树插入删除最多2次旋转，AVL可能需要O(log n)次");
        System.out.println("2. 虽然AVL查找更快，但现代CPU查找差异不明显");
        System.out.println("3. 红黑树的'近似平衡'在实际应用中足够好");
    }

    // 工具方法
    private static String toString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
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