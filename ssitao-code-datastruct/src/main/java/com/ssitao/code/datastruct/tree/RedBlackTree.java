package com.ssitao.code.datastruct.tree;

/**
 * 红黑树（自平衡二叉搜索树）
 *
 * 红黑树性质：
 * 1. 每个节点非红即黑
 * 2. 根节点是黑色
 * 3. 叶子节点（NIL）是黑色
 * 4. 红节点的子节点都是黑色（不能有两个连续的红节点）
 * 5. 从任一节点到其每个叶子的路径上，黑高相同
 *
 * 这些性质保证了红黑树的高度大致为 O(log n)
 *
 * 旋转和变色：
 * - 插入：通常通过变色和旋转修复
 * - 删除：更复杂的修复过程
 *
 * 时间复杂度：所有操作 O(log n)
 */
public class RedBlackTree<E> {

    // 颜色常量
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        E value;
        boolean color;
        Node left;
        Node right;
        Node parent;  // 父节点

        Node(E value, boolean color) {
            this.value = value;
            this.color = color;
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }

    private Node root;
    private int size;
    private Comparator<E> comparator;

    public interface Comparator<E> {
        int compare(E a, E b);
    }

    public RedBlackTree() {
        this(null);
    }

    public RedBlackTree(Comparator<E> comparator) {
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

    // 获取父节点
    private Node parentOf(Node node) {
        return node == null ? null : node.parent;
    }

    // 获取节点颜色
    private boolean colorOf(Node node) {
        return node == null ? BLACK : node.color;
    }

    // 设置节点颜色
    private void setColor(Node node, boolean color) {
        if (node != null) {
            node.color = color;
        }
    }

    // 获取左子节点
    private Node leftOf(Node node) {
        return node == null ? null : node.left;
    }

    // 获取右子节点
    private Node rightOf(Node node) {
        return node == null ? null : node.right;
    }

    // 判断节点是否为红
    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    // 判断节点是否为黑
    private boolean isBlack(Node node) {
        return node == null || node.color == BLACK;
    }

    // 左旋
    //    p               p
    //    |               |
    //    x               y
    //   / \             / \
    //  T1  y     -->    x   T3
    //     / \         / \
    //    T2  T3      T1  T2
    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;

        if (y.left != null) {
            y.left.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    // 右旋
    //      p           p
    //      |           |
    //      y           x
    //     / \         / \
    //    x   T3  --> T1  y
    //   / \             / \
    //  T1  T2          T2  T3
    private void rightRotate(Node y) {
        Node x = y.left;
        y.left = x.right;

        if (x.right != null) {
            x.right.parent = y;
        }

        x.parent = y.parent;

        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }

        x.right = y;
        y.parent = x;
    }

    // 添加元素
    public void add(E value) {
        Node node = new Node(value, RED);
        add(node);
    }

    private void add(Node node) {
        if (root == null) {
            root = node;
            size++;
            setColor(root, BLACK);  // 根节点必须是黑色
            return;
        }

        // 找到插入位置
        Node parent = null;
        Node current = root;
        int cmp = 0;

        while (current != null) {
            parent = current;
            cmp = compare(node.value, current.value);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                // 相等，不重复添加
                return;
            }
        }

        node.parent = parent;

        if (cmp < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }

        size++;

        // 修复红黑树性质
        addFixUp(node);
    }

    // 插入修复
    // 情况1：叔叔节点是红色
    // 情况2：叔叔节点是黑色，且当前节点是右子树
    // 情况3：叔叔节点是黑色，且当前节点是左子树
    private void addFixUp(Node node) {
        while (node != null && !isBlack(parentOf(node))) {
            Node father = parentOf(node);
            Node grandfather = parentOf(father);

            if (father == leftOf(grandfather)) {
                Node uncle = rightOf(grandfather);

                // 情况1：叔叔节点是红色
                if (isRed(uncle)) {
                    setColor(father, BLACK);
                    setColor(uncle, BLACK);
                    setColor(grandfather, RED);
                    node = grandfather;
                    continue;
                }

                // 情况2：叔叔是黑色，当前是右子树
                if (node == rightOf(father)) {
                    node = father;
                    leftRotate(node);
                    father = parentOf(node);
                    grandfather = parentOf(father);
                }

                // 情况3：叔叔是黑色，当前是左子树
                setColor(father, BLACK);
                setColor(grandfather, RED);
                rightRotate(grandfather);

            } else {
                // 对称情况：父亲是祖父的右子树
                Node uncle = leftOf(grandfather);

                // 情况1：叔叔是红色
                if (isRed(uncle)) {
                    setColor(father, BLACK);
                    setColor(uncle, BLACK);
                    setColor(grandfather, RED);
                    node = grandfather;
                    continue;
                }

                // 情况2：叔叔是黑色，当前是左子树
                if (node == leftOf(father)) {
                    node = father;
                    rightRotate(node);
                    father = parentOf(node);
                    grandfather = parentOf(father);
                }

                // 情况3：叔叔是黑色，当前是右子树
                setColor(father, BLACK);
                setColor(grandfather, RED);
                leftRotate(grandfather);
            }
        }

        // 确保根节点是黑色
        setColor(root, BLACK);
    }

    // 删除元素
    public void remove(E value) {
        Node node = findNode(value);
        if (node == null) {
            return;
        }
        remove(node);
    }

    private void remove(Node node) {
        Node replacement;
        Node parent = node.parent;
        boolean nodeColor = node.color;

        // 情况1：节点没有左子树
        if (node.left == null) {
            replacement = node.right;
            transplant(node, node.right);
        }
        // 情况2：节点没有右子树
        else if (node.right == null) {
            replacement = node.left;
            transplant(node, node.left);
        }
        // 情况3：节点有两个子树
        else {
            // 找到右子树的最小节点（后继）
            Node successor = minNode(node.right);
            nodeColor = successor.color;
            replacement = successor.right;

            if (successor.parent == node) {
                if (replacement != null) {
                    replacement.parent = successor;
                }
            } else {
                transplant(successor, successor.right);
                successor.right = node.right;
                successor.right.parent = successor;
            }

            transplant(node, successor);
            successor.left = node.left;
            successor.left.parent = successor;
            setColor(successor, node.color);
        }

        size--;

        // 如果删除的是黑节点，需要修复
        if (nodeColor == BLACK && replacement != null) {
            removeFixUp(replacement);
        }
    }

    // 用v替换u的位置
    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }

        if (v != null) {
            v.parent = u.parent;
        }
    }

    // 删除修复
    private void removeFixUp(Node node) {
        while (node != root && isBlack(node)) {
            if (node == leftOf(parentOf(node))) {
                Node sibling = rightOf(parentOf(node));

                // 情况1：兄弟节点是红色
                if (isRed(sibling)) {
                    setColor(sibling, BLACK);
                    setColor(parentOf(node), RED);
                    leftRotate(parentOf(node));
                    sibling = rightOf(parentOf(node));
                }

                // 情况2：兄弟节点是黑色，两个侄子都是黑色
                if (isBlack(leftOf(sibling)) && isBlack(rightOf(sibling))) {
                    setColor(sibling, RED);
                    node = parentOf(node);
                } else {
                    // 情况3：兄弟是黑色，左侄子是红色
                    if (isBlack(rightOf(sibling))) {
                        setColor(leftOf(sibling), BLACK);
                        setColor(sibling, RED);
                        rightRotate(sibling);
                        sibling = rightOf(parentOf(node));
                    }

                    // 情况4：兄弟是黑色，右侄子是红色
                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), BLACK);
                    setColor(rightOf(sibling), BLACK);
                    leftRotate(parentOf(node));
                    node = root;
                }
            } else {
                // 对称情况
                Node sibling = leftOf(parentOf(node));

                if (isRed(sibling)) {
                    setColor(sibling, BLACK);
                    setColor(parentOf(node), RED);
                    rightRotate(parentOf(node));
                    sibling = leftOf(parentOf(node));
                }

                if (isBlack(leftOf(sibling)) && isBlack(rightOf(sibling))) {
                    setColor(sibling, RED);
                    node = parentOf(node);
                } else {
                    if (isBlack(leftOf(sibling))) {
                        setColor(rightOf(sibling), BLACK);
                        setColor(sibling, RED);
                        leftRotate(sibling);
                        sibling = leftOf(parentOf(node));
                    }

                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), BLACK);
                    setColor(leftOf(sibling), BLACK);
                    rightRotate(parentOf(node));
                    node = root;
                }
            }
        }

        setColor(node, BLACK);
    }

    // 查找最小节点
    private Node minNode(Node node) {
        if (node == null || node.left == null) {
            return node;
        }
        return minNode(node.left);
    }

    // 查找节点
    private Node findNode(E value) {
        Node current = root;
        while (current != null) {
            int cmp = compare(value, current.value);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    // 查找元素
    public boolean contains(E value) {
        return findNode(value) != null;
    }

    // 前序遍历
    public void preOrder() {
        preOrder(root);
    }

    private void preOrder(Node node) {
        if (node == null) {
            return;
        }
        System.out.print(node.value + "(" + (node.color ? "R" : "B") + ") ");
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
        System.out.print(node.value + "(" + (node.color ? "R" : "B") + ") ");
        inOrder(node.right);
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
        sb.append(node.value).append("(").append(node.color ? "R" : "B").append(")\n");
        generateTreeString(node.left, depth + 1, sb);
        generateTreeString(node.right, depth + 1, sb);
    }
}