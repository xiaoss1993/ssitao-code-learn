package com.ssitao.code.datastruct.tree;

import com.ssitao.code.datastruct.queue.ArrayQueue;
import com.ssitao.code.datastruct.queue.Queue;

/**
 * 二叉搜索树（Binary Search Tree, BST）
 *
 * 二叉搜索树性质：
 * - 左子树上所有节点的值 < 根节点的值
 * - 右子树上所有节点的值 > 根节点的值
 * - 左右子树也分别是二叉搜索树
 *
 * 时间复杂度（平均）：
 * - 查找: O(log n)
 * - 插入: O(log n)
 * - 删除: O(log n)
 *
 * 时间复杂度（最坏，退化为链表）：
 * - O(n) - 当数据有序插入时
 */
public class BinarySearchTree<E> {

    // 树节点
    private class Node {
        E value;
        Node left;   // 左子树
        Node right;  // 右子树

        Node(E value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;  // 根节点
    private int size;    // 节点数量
    private Comparator<E> comparator;  // 比较器

    // 函数式接口用于比较
    public interface Comparator<E> {
        int compare(E a, E b);
    }

    public BinarySearchTree() {
        this(null);
    }

    public BinarySearchTree(Comparator<E> comparator) {
        this.root = null;
        this.size = 0;
        this.comparator = comparator;
    }

    // 获取树中节点数量
    public int size() {
        return size;
    }

    // 判断树是否为空
    public boolean isEmpty() {
        return size == 0;
    }

    // 比较两个元素
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        // 如果没有比较器，强制要求元素实现Comparable接口
        @SuppressWarnings("unchecked")
        Comparable<E> comparable = (Comparable<E>) a;
        return comparable.compareTo(b);
    }

    // 添加元素
    public void add(E value) {
        root = add(root, value);
    }

    private Node add(Node node, E value) {
        if (node == null) {
            size++;
            return new Node(value);
        }

        int cmp = compare(value, node.value);

        if (cmp < 0) {
            // value < node.value，插入左子树
            node.left = add(node.left, value);
        } else if (cmp > 0) {
            // value > node.value，插入右子树
            node.right = add(node.right, value);
        }
        // cmp == 0，不重复添加

        return node;
    }

    // 查找元素是否存在
    public boolean contains(E value) {
        return contains(root, value);
    }

    private boolean contains(Node node, E value) {
        if (node == null) {
            return false;
        }

        int cmp = compare(value, node.value);

        if (cmp < 0) {
            return contains(node.left, value);
        } else if (cmp > 0) {
            return contains(node.right, value);
        } else {
            return true;
        }
    }

    // 前序遍历（根-左-右）
    public void preOrder() {
        preOrder(root);
    }

    private void preOrder(Node node) {
        if (node == null) {
            return;
        }
        System.out.print(node.value + " ");
        preOrder(node.left);
        preOrder(node.right);
    }

    // 中序遍历（左-根-右），结果有序
    public void inOrder() {
        inOrder(root);
    }

    private void inOrder(Node node) {
        if (node == null) {
            return;
        }
        inOrder(node.left);
        System.out.print(node.value + " ");
        inOrder(node.right);
    }

    // 后序遍历（左-右-根）
    public void postOrder() {
        postOrder(root);
    }

    private void postOrder(Node node) {
        if (node == null) {
            return;
        }
        postOrder(node.left);
        postOrder(node.right);
        System.out.print(node.value + " ");
    }

    // 层序遍历（广度优先）
    public void levelOrder() {
        if (root == null) {
            return;
        }

        Queue<Node> queue = new ArrayQueue<>();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            Node node = queue.dequeue();
            System.out.print(node.value + " ");

            if (node.left != null) {
                queue.enqueue(node.left);
            }
            if (node.right != null) {
                queue.enqueue(node.right);
            }
        }
    }

    // 删除最小节点
    public E removeMin() {
        if (root == null) {
            throw new IllegalArgumentException("树为空");
        }
        Node result = findMin(root);
        root = removeMin(root);
        size--;
        return result.value;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            // node没有左子树，node本身是最小节点
            return node.right;
        }
        node.left = removeMin(node.left);
        return node;
    }

    // 删除最大节点
    public E removeMax() {
        if (root == null) {
            throw new IllegalArgumentException("树为空");
        }
        Node result = findMax(root);
        root = removeMax(root);
        size--;
        return result.value;
    }

    private Node removeMax(Node node) {
        if (node.right == null) {
            return node.left;
        }
        node.right = removeMax(node.right);
        return node;
    }

    // 删除任意节点
    public void remove(E value) {
        root = remove(root, value);
    }

    private Node remove(Node node, E value) {
        if (node == null) {
            return null;
        }

        int cmp = compare(value, node.value);

        if (cmp < 0) {
            node.left = remove(node.left, value);
            return node;
        } else if (cmp > 0) {
            node.right = remove(node.right, value);
            return node;
        } else {
            // 找到要删除的节点
            size--;

            if (node.left == null) {
                // 只有右子树，直接返回右子树
                return node.right;
            }
            if (node.right == null) {
                // 只有左子树，直接返回左子树
                return node.left;
            }

            // 左右子树都存在
            // 找到右子树的最小节点（后继）
            Node successor = findMin(node.right);
            successor.right = removeMin(node.right);
            successor.left = node.left;
            return successor;
        }
    }

    // 查找最小节点
    private Node findMin(Node node) {
        if (node.left == null) {
            return node;
        }
        return findMin(node.left);
    }

    // 查找最大节点
    private Node findMax(Node node) {
        if (node.right == null) {
            return node;
        }
        return findMax(node.right);
    }

    // 获取树高度
    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // 判断是否为二分搜索树（验证BST性质）
    public boolean isBST() {
        if (root == null) {
            return true;
        }
        return isBST(root, null, null);
    }

    @SuppressWarnings("unchecked")
    private boolean isBST(Node node, E min, E max) {
        if (node == null) {
            return true;
        }

        if (min != null && compare(node.value, min) <= 0) {
            return false;
        }
        if (max != null && compare(node.value, max) >= 0) {
            return false;
        }

        return isBST(node.left, min, node.value) && isBST(node.right, node.value, max);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        generateTreeString(root, 0, sb);
        return sb.toString();
    }

    private void generateTreeString(Node node, int depth, StringBuilder sb) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        sb.append(node.value).append("\n");
        generateTreeString(node.left, depth + 1, sb);
        generateTreeString(node.right, depth + 1, sb);
    }
}