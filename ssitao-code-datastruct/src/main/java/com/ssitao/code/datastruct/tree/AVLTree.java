package com.ssitao.code.datastruct.tree;

/**
 * AVL树（自平衡二叉搜索树）
 *
 * AVL树性质：
 * - 本身是一棵二叉搜索树
 * - 任意节点的左右子树高度差（平衡因子）<= 1
 * - 插入/删除后通过旋转保持平衡
 *
 * 旋转操作：
 * - 右旋（LL型）：对左子树的左插入导致不平衡
 * - 左旋（RR型）：对右子树的右插入导致不平衡
 * - 左右旋（LR型）：对左子树的右插入导致不平衡
 * - 右左旋（RL型）：对右子树的左插入导致不平衡
 *
 * 时间复杂度：所有操作 O(log n)
 */
public class AVLTree<E> {

    private class Node {
        E value;
        Node left;
        Node right;
        int height;  // 节点高度

        Node(E value) {
            this.value = value;
            this.left = null;
            this.right = null;
            this.height = 1;
        }
    }

    private Node root;
    private int size;
    private Comparator<E> comparator;

    public interface Comparator<E> {
        int compare(E a, E b);
    }

    public AVLTree() {
        this(null);
    }

    public AVLTree(Comparator<E> comparator) {
        this.root = null;
        this.size = 0;
        this.comparator = comparator;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        @SuppressWarnings("unchecked")
        Comparable<E> comparable = (Comparable<E>) a;
        return comparable.compareTo(b);
    }

    // 获取节点高度（空节点高度为0）
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    // 获取平衡因子（左右子树高度差）
    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    // 更新节点高度
    private void updateHeight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    // 判断是否为二分搜索树
    public boolean isBST() {
        return isBST(root, null, null);
    }

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

    // 判断是否为AVL树（所有节点平衡）
    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(Node node) {
        if (node == null) {
            return true;
        }
        int balance = Math.abs(balanceFactor(node));
        if (balance > 1) {
            return false;
        }
        return isBalanced(node.left) && isBalanced(node.right);
    }

    // 右旋（LL型旋转）
    //    y               x
    //   / \             / \
    //  x   T4   -->    T1  y
    // / \                 / \
    //T1  T3              T3  T4
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T3 = x.right;

        // 旋转
        x.right = y;
        y.left = T3;

        // 更新高度（注意：先更新y，再更新x）
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // 左旋（RR型旋转）
    //    y                 x
    //   / \               / \
    //  T1  x      -->     y   T4
    //     / \           / \
    //    T3  T4        T1  T3
    private Node leftRotate(Node y) {
        Node x = y.right;
        Node T3 = x.left;

        // 旋转
        x.left = y;
        y.right = T3;

        // 更新高度
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // 获取树的最小节点
    private Node minNode(Node node) {
        if (node == null || node.left == null) {
            return node;
        }
        return minNode(node.left);
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
            node.left = add(node.left, value);
        } else if (cmp > 0) {
            node.right = add(node.right, value);
        } else {
            // 相等，不重复添加
            return node;
        }

        // 更新高度
        updateHeight(node);

        // 检查并维护平衡
        return balance(node);
    }

    // 维护平衡
    private Node balance(Node node) {
        int balance = balanceFactor(node);

        // LL型：左子树的左子树导致不平衡，右旋
        if (balance > 1 && balanceFactor(node.left) >= 0) {
            return rightRotate(node);
        }

        // RR型：右子树的右子树导致不平衡，左旋
        if (balance < -1 && balanceFactor(node.right) <= 0) {
            return leftRotate(node);
        }

        // LR型：左子树的右子树导致不平衡，先左旋后右旋
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL型：右子树的左子树导致不平衡，先右旋后左旋
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // 删除元素
    public void remove(E value) {
        root = remove(root, value);
    }

    private Node remove(Node node, E value) {
        if (node == null) {
            return null;
        }

        Node result = null;
        int cmp = compare(value, node.value);

        if (cmp < 0) {
            node.left = remove(node.left, value);
            result = node;
        } else if (cmp > 0) {
            node.right = remove(node.right, value);
            result = node;
        } else {
            // 找到要删除的节点
            size--;

            if (node.left == null) {
                result = node.right;
            } else if (node.right == null) {
                result = node.left;
            } else {
                // 左右子树都存在，用后继替换
                Node successor = minNode(node.right);
                successor.right = remove(node.right, successor.value);
                successor.left = node.left;
                result = successor;
            }
        }

        if (result == null) {
            return null;
        }

        updateHeight(result);
        return balance(result);
    }

    // 查找元素
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

    // 前序遍历
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

    // 中序遍历
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

    // 获取树高度
    public int height() {
        return height(root);
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
        sb.append(node.value).append("(h=").append(node.height).append(")\n");
        generateTreeString(node.left, depth + 1, sb);
        generateTreeString(node.right, depth + 1, sb);
    }
}