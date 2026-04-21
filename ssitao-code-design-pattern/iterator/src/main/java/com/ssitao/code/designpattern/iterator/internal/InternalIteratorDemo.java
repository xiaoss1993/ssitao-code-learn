package com.ssitao.code.designpattern.iterator.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 内部迭代器示例（类似Java Stream）
 *
 * 外部迭代器 vs 内部迭代器：
 * - 外部迭代器：由客户端控制遍历（Iterator）
 * - 内部迭代器：由集合本身控制遍历（forEach, Stream）
 *
 * 优点：
 * - 代码更简洁
 * - 更好的并行优化
 * - 延迟执行
 */
public class InternalIteratorDemo {

    public static void main(String[] args) {
        System.out.println("=== 内部迭代器示例 ===\n");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 1. forEach内部迭代
        System.out.println("1. forEach内部迭代");
        numbers.forEach(n -> System.out.println("  " + n));

        // 2. 使用Stream
        System.out.println("\n2. Stream API");
        streamDemo(numbers);

        // 3. 自定义内部迭代器
        System.out.println("\n3. 自定义内部迭代器");
        customInternalIterator();

        // 4. 组合迭代器
        System.out.println("\n4. 树形结构迭代器");
        treeIteratorDemo();
    }

    /**
     * Stream API示例
     */
    private static void streamDemo(List<Integer> numbers) {
        // 过滤
        System.out.println("过滤偶数:");
        numbers.stream()
            .filter(n -> n % 2 == 0)
            .forEach(n -> System.out.println("  " + n));

        // 映射
        System.out.println("\n平方运算:");
        numbers.stream()
            .map(n -> n * n)
            .forEach(n -> System.out.println("  " + n));

        // 汇总
        System.out.println("\n求和: " + numbers.stream().mapToInt(Integer::intValue).sum());

        // 分组
        System.out.println("\n按奇偶分组:");
        Map<String, List<Integer>> grouped = numbers.stream()
            .reduce(
                new HashMap<String, List<Integer>>(),
                (map, n) -> {
                    String key = n % 2 == 0 ? "偶数" : "奇数";
                    map.computeIfAbsent(key, k -> new ArrayList<>()).add(n);
                    return map;
                },
                (map1, map2) -> {
                    map2.forEach((k, v) -> map1.merge(k, v, (l1, l2) -> {
                        l1.addAll(l2);
                        return l1;
                    }));
                    return map1;
                }
            );
        System.out.println("  " + grouped);
    }

    /**
     * 自定义内部迭代器
     */
    private static void customInternalIterator() {
        // 创建集合
        StringCollection collection = new StringCollection();
        collection.add("Hello");
        collection.add("World");
        collection.add("Iterator");

        // 使用内部迭代（类似forEach）
        System.out.println("内部迭代:");
        collection.forEach(s -> System.out.println("  " + s));

        // 带条件的内部迭代
        System.out.println("\n过滤后的内部迭代:");
        collection.filter(s -> s.length() > 5)
            .forEach(s -> System.out.println("  " + s));
    }

    /**
     * 树形结构迭代器
     */
    private static void treeIteratorDemo() {
        // 创建树形结构
        TreeNode root = new TreeNode("root");
        TreeNode child1 = new TreeNode("child1");
        TreeNode child2 = new TreeNode("child2");
        TreeNode grand11 = new TreeNode("grand11");
        TreeNode grand12 = new TreeNode("grand12");
        TreeNode grand21 = new TreeNode("grand21");

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(grand11);
        child1.addChild(grand12);
        child2.addChild(grand21);

        // 前序遍历
        System.out.println("前序遍历:");
        for (String node : new TreeIterator(root, TraversalOrder.PRE_ORDER)) {
            System.out.println("  " + node);
        }

        // 后序遍历
        System.out.println("\n后序遍历:");
        for (String node : new TreeIterator(root, TraversalOrder.POST_ORDER)) {
            System.out.println("  " + node);
        }

        // 层级遍历
        System.out.println("\n层级遍历:");
        for (String node : new TreeIterator(root, TraversalOrder.LEVEL_ORDER)) {
            System.out.println("  " + node);
        }
    }
}

/**
 * 支持内部迭代的字符串集合
 */
class StringCollection {
    private List<String> elements = new ArrayList<>();

    public void add(String s) {
        elements.add(s);
    }

    public int size() {
        return elements.size();
    }

    // 内部迭代方法
    public void forEach(Consumer<String> action) {
        for (String element : elements) {
            action.accept(element);
        }
    }

    // 返回支持链式操作的Filterable
    public FilterableStringCollection filter(Predicate<String> predicate) {
        FilterableStringCollection result = new FilterableStringCollection();
        for (String element : elements) {
            if (predicate.test(element)) {
                result.add(element);
            }
            // 为了链式调用，需要返回一个特殊类型
        }
        return result;
    }
}

/**
 * 可过滤的集合（模拟Stream）
 */
class FilterableStringCollection extends StringCollection {
    // 继承父类的forEach方法
}

/**
 * 树节点
 */
class TreeNode {
    private String value;
    private List<TreeNode> children = new ArrayList<>();

    public TreeNode(String value) {
        this.value = value;
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

    public String getValue() {
        return value;
    }

    public List<TreeNode> getChildren() {
        return children;
    }
}

/**
 * 遍历顺序
 */
enum TraversalOrder {
    PRE_ORDER,   // 根 -> 左 -> 右
    POST_ORDER,  // 左 -> 右 -> 根
    LEVEL_ORDER // 层级遍历
}

/**
 * 树迭代器 - 外部迭代器实现
 */
class TreeIterator implements Iterator<String>, Iterable<String> {
    private TreeNode root;
    private TraversalOrder order;
    private List<TreeNode> queue = new ArrayList<>();
    private int index = 0;

    public TreeIterator(TreeNode root, TraversalOrder order) {
        this.root = root;
        this.order = order;
        initialize();
    }

    private void initialize() {
        if (order == TraversalOrder.LEVEL_ORDER) {
            if (root != null) {
                queue.add(root);
            }
        } else if (order == TraversalOrder.PRE_ORDER) {
            buildPreOrderList(root);
        } else if (order == TraversalOrder.POST_ORDER) {
            buildPostOrderList(root);
        }
    }

    private void buildPreOrderList(TreeNode node) {
        if (node == null) return;
        queue.add(node);
        for (TreeNode child : node.getChildren()) {
            buildPreOrderList(child);
        }
    }

    private void buildPostOrderList(TreeNode node) {
        if (node == null) return;
        for (TreeNode child : node.getChildren()) {
            buildPostOrderList(child);
        }
        queue.add(node);
    }

    @Override
    public boolean hasNext() {
        if (order == TraversalOrder.LEVEL_ORDER) {
            return index < queue.size();
        }
        return index < queue.size();
    }

    @Override
    public String next() {
        if (order == TraversalOrder.LEVEL_ORDER) {
            TreeNode node = queue.get(index++);
            if (index < queue.size() + 1) {
                // 添加子节点
                for (TreeNode child : node.getChildren()) {
                    queue.add(child);
                }
            }
            return node.getValue();
        }
        return queue.get(index++).getValue();
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }
}
